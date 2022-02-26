package com.eliotlash.molang.math.functions.rounding;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.functions.Function;

public class Round extends Function {
	public Round(IValue[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 1;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return Math.round(this.evaluateArgument(ctx, 0));
	}
}
