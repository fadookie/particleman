package com.eliotlash.molang.math;

import com.eliotlash.molang.variables.ExecutionContext;

/**
 * Negative operator class
 * <p>
 * This class is responsible for inverting given value
 */
public record Negative(IValue value) implements IValue {

	@Override
	public double evaluate(ExecutionContext ctx) {
		return -this.value.evaluate(ctx);
	}

	@Override
	public String toString() {
		return "-" + this.value.toString();
	}

	@Override
	public boolean isConstant() {
		return this.value.isConstant();
	}
}
