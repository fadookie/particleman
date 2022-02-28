package com.eliotlash.molang;

import com.eliotlash.molang.ast.Expr;
import com.eliotlash.molang.ast.Operator;

public class TestBase {
	protected static Expr.Access access(String var, String member) {
		return new Expr.Access(new Expr.Variable(var), member);
	}

	protected static Expr.BinOp op(Expr left, Operator op, Expr right) {
		return new Expr.BinOp(op, left, right);
	}

	protected static Expr.Group paren(Expr expr) {
		return new Expr.Group(expr);
	}

	protected static Expr.Constant c(double constant) {
		return new Expr.Constant(constant);
	}
}
