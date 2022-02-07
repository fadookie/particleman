package com.eliotlash.molang.math;

/**
 * Negative operator class
 * <p>
 * This class is responsible for inverting given value
 */
public class Negative implements IValue {
	public IValue value;

	public Negative(IValue value) {
		this.value = value;
	}

	@Override
	public double get() {
		return -this.value.get();
	}

	@Override
	public String toString() {
		return "-(" + this.value.toString() + ")";
	}

	@Override
	public boolean isConstant() {
		return this.value.isConstant();
	}
}
