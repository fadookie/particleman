package com.eliotlash.molang.functions.classic;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.expressions.Expr;
import com.eliotlash.molang.functions.Function;

public class Pow extends Function {
	public Pow(Expr[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 2;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return Math.pow(this.evaluateArgument(ctx, 0), this.evaluateArgument(ctx, 1));
	}
}
