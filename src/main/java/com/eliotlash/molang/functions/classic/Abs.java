package com.eliotlash.molang.functions.classic;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.ast.Expr;
import com.eliotlash.molang.functions.Function;

/**
 * Absolute value function
 */
public class Abs extends Function {
	public Abs(Expr[] values, String name) throws Exception {
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
