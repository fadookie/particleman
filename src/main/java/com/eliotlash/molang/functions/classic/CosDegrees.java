package com.eliotlash.molang.functions.classic;

import com.eliotlash.molang.functions.Function;
import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.expressions.Expr;

public class CosDegrees extends Function {
	public CosDegrees(Expr[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 1;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return Math.cos(this.evaluateArgument(ctx, 0) / 180 * Math.PI);
	}
}
