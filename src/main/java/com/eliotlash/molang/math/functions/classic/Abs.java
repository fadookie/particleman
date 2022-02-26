package com.eliotlash.molang.math.functions.classic;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.functions.Function;

/**
 * Absolute value function
 */
public class Abs extends Function {
	public Abs(IValue[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 1;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return Math.abs(this.evaluateArgument(ctx, 0));
	}
}
