package com.eliotlash.molang.math;

import java.util.Objects;

import com.eliotlash.molang.variables.ExecutionContext;

/**
 * Constant class
 * <p>
 * This class simply returns supplied in the constructor value
 */
public record Constant(double value) implements IValue {

	@Override
	public double evaluate(ExecutionContext ctx) {
		return this.value;
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}

	@Override
	public boolean isConstant() {
		return true;
	}
}
