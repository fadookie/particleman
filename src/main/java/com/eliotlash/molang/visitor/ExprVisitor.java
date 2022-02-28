package com.eliotlash.molang.visitor;

import com.eliotlash.molang.expressions.*;

public interface ExprVisitor<R> {
	R visitAccess(Expr.Access expr);
	R visitAssignment(Expr.Assignment expr);
	R visitBinaryOperation(Expr.BinOp expr);
	R visitCall(Expr.Call expr);
	R visitConstant(Expr.Constant expr);
	R visitGroup(Expr.Group expr);
	R visitNegate(Expr.Negate expr);
	R visitNot(Expr.Not expr);
	R visitTernary(Expr.Ternary expr);
	R visitVariable(Expr.Variable expr);
}
