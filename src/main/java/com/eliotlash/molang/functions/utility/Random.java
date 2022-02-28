package com.eliotlash.molang.functions.utility;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.ast.Expr;
import com.eliotlash.molang.functions.Function;

public class Random extends Function {
	public java.util.Random random;

	public Random(Expr[] values, String name) throws Exception {
		super(values, name);

		this.random = new java.util.Random();
	}

	public double evaluate(ExecutionContext ctx) {
		double random = 0;

		if (this.arguments.length >= 3) {
			this.random.setSeed((long) this.evaluateArgument(ctx, 2));
			random = this.random.nextDouble();
		} else {
			random = Math.random();
		}

		if (this.arguments.length >= 2) {
			double a = this.evaluateArgument(ctx, 0);
			double b = this.evaluateArgument(ctx, 1);

			double min = Math.min(a, b);
			double max = Math.max(a, b);

			random = random * (max - min) + min;
		} else if (this.arguments.length >= 1) {
			random = random * this.evaluateArgument(ctx, 0);
		}

		return random;
	}
}
