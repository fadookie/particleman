package com.eliotlash.molang.math.functions.limit;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.functions.Function;
import com.eliotlash.molang.utils.MathUtils;

public class Clamp extends Function {
	public Clamp(IValue[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 3;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return MathUtils.clamp(this.evaluateArgument(ctx, 0), this.evaluateArgument(ctx, 1), this.evaluateArgument(ctx, 2));
	}
}
