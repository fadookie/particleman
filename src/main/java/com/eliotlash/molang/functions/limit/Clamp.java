package com.eliotlash.molang.functions.limit;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.expressions.Expr;
import com.eliotlash.molang.functions.Function;
import com.eliotlash.molang.utils.MathUtils;

public class Clamp extends Function {
	public Clamp(Expr[] values, String name) throws Exception {
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
