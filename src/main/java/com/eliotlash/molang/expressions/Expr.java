package com.eliotlash.molang.expressions;

import java.util.List;

import com.eliotlash.molang.visitor.ExprVisitor;

public interface Expr {

	<R> R accept(ExprVisitor<R> visitor);

	/**
	 * target.member
	 */
	record Access(Variable target, String member) implements Expr, Assignable {

		@Override
		public <R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitAccess(this);
		}
	}

	/**
	 * variable = expression
	 */
	record Assignment(Assignable variable, Expr expression) implements Expr {

		@Override
		public <R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitAssignment(this);
		}
	}

	/**
	 * left op right
	 */
	record BinOp(Operator operator, Expr left, Expr right) implements Expr {

		@Override
		public <R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitBinaryOperation(this);
		}
	}

	/**
	 * target.member(arguments)
	 */
	record Call(Variable target, String member, List<Expr> arguments) implements Expr {

		@Override
		public <R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitCall(this);
		}
	}

	/**
	 * 2.4
	 */
	record Constant(double value) implements Expr {

		@Override
		public <R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitConstant(this);
		}
	}

	/**
	 * ( expr )
	 */
	record Group(Expr value) implements Expr {

		@Override
		public <R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitGroup(this);
		}
	}

	/**
	 * -expr
	 */
	record Negate(Expr value) implements Expr {
		@Override
		public <R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitNegate(this);
		}
	}

	/**
	 * !expr
	 */
	record Not(Expr value) implements Expr {

		@Override
		public <R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitNot(this);
		}
	}

	/**
	 * condition ? ifTrue : ifFalse
	 */
	record Ternary(Expr condition, Expr ifTrue, Expr ifFalse) implements Expr {
		@Override
		public <R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitTernary(this);
		}
	}

	/**
	 * some_identifier
	 */
	record Variable(String name) implements Expr {

		@Override
		public <R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitVariable(this);
		}
	}
}
