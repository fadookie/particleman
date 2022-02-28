package com.eliotlash.molang.functions.rounding;

import com.eliotlash.molang.functions.Function;
import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.ast.Expr;

public class Trunc extends Function {
	public Trunc(Expr[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 1;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		double value = this.evaluateArgument(ctx, 0);

		return value < 0 ? Math.ceil(value) : Math.floor(value);
	}
}
