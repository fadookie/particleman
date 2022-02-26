package com.eliotlash.molang.math;

import com.eliotlash.molang.variables.ExecutionContext;

/**
 * Group class
 * <p>
 * Simply wraps given {@link IValue} into parenthesis in the
 * {@link #toString()} method.
 */
public record Group(IValue value) implements IValue {

	@Override
	public double evaluate(ExecutionContext ctx) {
		return this.value.evaluate(ctx);
	}

	@Override
	public String toString() {
		return "(" + this.value.toString() + ")";
	}

	@Override
	public boolean isConstant() {
		return this.value.isConstant();
	}
}
