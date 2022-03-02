package com.eliotlash.molang.ast;

public class ASTTransformation implements Expr.Visitor<Expr>, Stmt.Visitor<Stmt> {

	@Override
	public Stmt visitExpression(Stmt.Expression stmt) {
		return new Stmt.Expression(visit(stmt.expr()));
	}

	@Override
	public Stmt visitReturn(Stmt.Return stmt) {
		return new Stmt.Return(visit(stmt.value()));
	}

	@Override
	public Stmt visitBreak(Stmt.Break stmt) {
		return stmt;
	}

	@Override
	public Stmt visitContinue(Stmt.Continue stmt) {
		return stmt;
	}

	@Override
	public Stmt visitLoop(Stmt.Loop stmt) {
		return new Stmt.Loop(visit(stmt.count()), visit(stmt.expr()));
	}

	@Override
	public Expr visitAccess(Expr.Access expr) {
		return new Expr.Access((Expr.Variable) visit(expr.target()), expr.member());
	}

	@Override
	public Expr visitAssignment(Expr.Assignment expr) {
		return new Expr.Assignment((Assignable) visit(expr.variable()), visit(expr.expression()));
	}

	@Override
	public Expr visitBinOp(Expr.BinOp expr) {
		return new Expr.BinOp(expr.operator(), visit(expr.left()), visit(expr.right()));
	}

	@Override
	public Expr visitBlock(Expr.Block expr) {
		return new Expr.Block(expr.statements().stream().map(this::visit).toList());
	}

	@Override
	public Expr visitCall(Expr.Call expr) {
		return new Expr.Call((Expr.Variable) visit(expr.target()), expr.member(), expr.arguments().stream().map(this::visit).toList());
	}

	@Override
	public Expr visitCoalesce(Expr.Coalesce expr) {
		return new Expr.Coalesce(visit(expr.value()), visit(expr.fallback()));
	}

	@Override
	public Expr visitConstant(Expr.Constant expr) {
		return expr;
	}

	@Override
	public Expr visitGroup(Expr.Group expr) {
		return new Expr.Group(visit(expr.value()));
	}

	@Override
	public Expr visitNegate(Expr.Negate expr) {
		return new Expr.Negate(visit(expr.value()));
	}

	@Override
	public Expr visitNot(Expr.Not expr) {
		return new Expr.Not(visit(expr.value()));
	}

	@Override
	public Expr visitTernary(Expr.Ternary expr) {
		return new Expr.Ternary(visit(expr.condition()), visit(expr.ifTrue()), visit(expr.ifFalse()));
	}

	@Override
	public Expr visitVariable(Expr.Variable expr) {
		return expr;
	}
}
