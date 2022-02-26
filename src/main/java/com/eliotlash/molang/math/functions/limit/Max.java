package com.eliotlash.molang.math.functions.limit;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.functions.Function;

public class Max extends Function {
	public Max(IValue[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 2;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return Math.max(this.evaluateArgument(ctx, 0), this.evaluateArgument(ctx, 1));
	}
}
