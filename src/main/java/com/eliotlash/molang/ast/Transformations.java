package com.eliotlash.molang.ast;

public class Transformations {
	public static final ASTTransformation CONSTANT_PROPAGATION = new ASTTransformation() {

		@Override
		public Expr visitBinOp(Expr.BinOp expr) {
			var left = visit(expr.left());
			var right = visit(expr.right());
			var op = expr.operator();

			if (left instanceof Expr.Constant lhs && right instanceof Expr.Constant rhs) {
				return new Expr.Constant(op.apply(lhs::value, rhs::value));
			}

			return new Expr.BinOp(op, left, right);
		}

		@Override
		public Expr visitNegate(Expr.Negate expr) {
			var inner = visit(expr.value());
			if (inner instanceof Expr.Constant c) {
				return new Expr.Constant(-c.value());
			}
			return new Expr.Negate(inner);
		}

		@Override
		public Expr visitNot(Expr.Not expr) {
			var inner = visit(expr.value());
			if (inner instanceof Expr.Constant c) {
				return new Expr.Constant(c.value() == 0 ? 1 : 0);
			}
			return new Expr.Not(inner);
		}

		@Override
		public Expr visitTernary(Expr.Ternary expr) {
			var cond = visit(expr.condition());
			var tru = visit(expr.ifTrue());
			var fal = visit(expr.ifFalse());

			if (cond instanceof Expr.Constant c && tru instanceof Expr.Constant t && fal instanceof Expr.Constant f) {
				return new Expr.Constant(c.value() == 0 ? f.value() : t.value());
			}

			return new Expr.Ternary(cond, tru, fal);
		}
	};

	public static final ASTTransformation INLINE_PARENS = new ASTTransformation() {
		@Override
		public Expr visitGroup(Expr.Group expr) {
			return visit(expr.value());
		}
	};
}
