package com.eliotlash.molang.functions.limit;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.ast.Expr;
import com.eliotlash.molang.functions.Function;

public class Min extends Function {
	public Min(Expr[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 2;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return Math.min(this.evaluateArgument(ctx, 0), this.evaluateArgument(ctx, 1));
	}
}
