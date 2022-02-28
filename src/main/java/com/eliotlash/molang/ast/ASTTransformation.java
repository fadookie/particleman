package com.eliotlash.molang.ast;

import java.util.stream.Collectors;

public class ASTTransformation implements Visitor<Expr> {

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
	public Expr visitCall(Expr.Call expr) {
		return new Expr.Call((Expr.Variable) visit(expr.target()), expr.member(), expr.arguments().stream().map(this::visit).collect(Collectors.toList()));
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
