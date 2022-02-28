package com.eliotlash.molang.ast;

public interface Visitor<R> {
	default R visit(Expr node) {
		return node.accept(this);
	}

	R visitAccess(Expr.Access expr);
	R visitAssignment(Expr.Assignment expr);
	R visitBinOp(Expr.BinOp expr);
	R visitCall(Expr.Call expr);
	R visitConstant(Expr.Constant expr);
	R visitGroup(Expr.Group expr);
	R visitNegate(Expr.Negate expr);
	R visitNot(Expr.Not expr);
	R visitTernary(Expr.Ternary expr);
	R visitVariable(Expr.Variable expr);
}
