package com.eliotlash.molang.math;

import com.eliotlash.molang.variables.ExecutionContext;

/**
 * Variable class
 * <p>
 * This class is responsible for providing a mutable {@link IValue}
 * which can be modifier during runtime and still getting referenced in
 * the expressions parsed by {@link MathBuilder}.
 * <p>
 * But in practice, it's simply returns stored value and provides a
 * method to modify it.
 */
public record Variable(String name) implements IValue {

	@Override
	public double evaluate(ExecutionContext ctx) {
		return 0;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean isConstant() {
		return false;
	}
}
