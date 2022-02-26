package com.eliotlash.molang.math;

import com.eliotlash.molang.variables.ExecutionContext;

/**
 * Negate operator class
 * <p>
 * This class is responsible for negating given value
 */
public class BooleanNot implements IValue {
	public final IValue value;

	public BooleanNot(IValue value) {
		this.value = value;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return this.value.evaluate(ctx) == 0 ? 1 : 0;
	}

	@Override
	public String toString() {
		return "!" + this.value.toString();
	}

	@Override
	public boolean isConstant() {
		return this.value.isConstant();
	}
}
