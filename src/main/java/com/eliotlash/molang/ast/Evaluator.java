package com.eliotlash.molang.ast;

public class Evaluator implements Visitor<Double> {
	@Override
	public Double visitAccess(Expr.Access expr) {
		return null;
	}

	@Override
	public Double visitAssignment(Expr.Assignment expr) {
		var rhs = evaluate(expr.expression());

		// TODO: Do assignment
		return rhs;
	}

	@Override
	public Double visitBinOp(Expr.BinOp expr) {
		return expr.operator().apply(() -> evaluate(expr.left()), () -> evaluate(expr.right()));
	}

	@Override
	public Double visitCall(Expr.Call expr) {

		return null;
	}

	@Override
	public Double visitConstant(Expr.Constant expr) {
		return expr.value();
	}

	@Override
	public Double visitGroup(Expr.Group expr) {
		return evaluate(expr.value());
	}

	@Override
	public Double visitNegate(Expr.Negate expr) {
		return -evaluate(expr.value());
	}

	@Override
	public Double visitNot(Expr.Not expr) {
		return evaluate(expr.value()) == 0 ? 1.0 : 0.0;
	}

	@Override
	public Double visitTernary(Expr.Ternary expr) {
		Expr branch = evaluate(expr.condition()) == 0 ? expr.ifFalse() : expr.ifTrue();
		return evaluate(branch);
	}

	@Override
	public Double visitVariable(Expr.Variable expr) {
		return null;
	}

	private Double evaluate(Expr expr) {
		return expr.accept(this);
	}
}
