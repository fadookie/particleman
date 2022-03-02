package com.eliotlash.molang.ast;

public interface Stmt {

	<R> R accept(Visitor<R> visitor);

	/**
	 * expr;
	 */
	record Expression(Expr expr) implements Stmt {
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitExpression(this);
		}
	}

	/**
	 * return expr;
	 */
	record Return(Expr value) implements Stmt {
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitReturn(this);
		}
	}

	/**
	 * break;
	 */
	record Break() implements Stmt {
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitBreak(this);
		}
	}

	/**
	 * continue;
	 */
	record Continue() implements Stmt {
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitContinue(this);
		}
	}

	/**
	 * loop(expr, expr);
	 */
	record Loop(Expr count, Expr expr) implements Stmt {
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitLoop(this);
		}
	}

	interface Visitor<R> {
		default R visit(Stmt stmt) {
			return stmt.accept(this);
		}

		R visitExpression(Expression stmt);
		R visitReturn(Return stmt);
		R visitBreak(Break stmt);
		R visitContinue(Continue stmt);
		R visitLoop(Loop stmt);
	}
}
