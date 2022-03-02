package com.eliotlash.molang.ast;

public class Evaluator implements Expr.Visitor<Double>, Stmt.Visitor<Void> {

	@Override
	public Void visitExpression(Stmt.Expression stmt) {
		return null;
	}

	@Override
	public Void visitReturn(Stmt.Return stmt) {
		return null;
	}

	@Override
	public Void visitBreak(Stmt.Break stmt) {
		return null;
	}

	@Override
	public Void visitContinue(Stmt.Continue stmt) {
		return null;
	}

	@Override
	public Void visitLoop(Stmt.Loop stmt) {
		return null;
	}

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
	public Double visitBlock(Expr.Block expr) {
		return 0.0;
	}

	@Override
	public Double visitCall(Expr.Call expr) {
		return null;
	}

	@Override
	public Double visitCoalesce(Expr.Coalesce expr) {
		var value = evaluate(expr.value());
		return value == null ? evaluate(expr.fallback()) : value;
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
