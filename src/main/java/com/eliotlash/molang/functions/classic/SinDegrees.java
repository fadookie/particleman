package com.eliotlash.molang.functions.classic;

import com.eliotlash.molang.functions.Function;
import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.expressions.Expr;

public class SinDegrees extends Function {
	public SinDegrees(Expr[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 1;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return Math.sin(this.evaluateArgument(ctx, 0) / 180 * Math.PI);
	}
}
