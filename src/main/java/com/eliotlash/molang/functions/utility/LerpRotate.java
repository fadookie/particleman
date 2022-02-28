package com.eliotlash.molang.functions.utility;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.ast.Expr;
import com.eliotlash.molang.functions.Function;
import com.eliotlash.molang.utils.Interpolations;

public class LerpRotate extends Function {
	public LerpRotate(Expr[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 3;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return Interpolations.lerpYaw(this.evaluateArgument(ctx, 0), this.evaluateArgument(ctx, 1), this.evaluateArgument(ctx, 2));
	}
}
