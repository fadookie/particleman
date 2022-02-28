package com.eliotlash.molang.functions.classic;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.expressions.Expr;
import com.eliotlash.molang.functions.Function;

public class Cos extends Function {
	public Cos(Expr[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 1;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return Math.cos(this.evaluateArgument(ctx, 0));
	}
}
