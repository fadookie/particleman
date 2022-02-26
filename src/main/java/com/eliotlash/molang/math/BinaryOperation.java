package com.eliotlash.molang.math;

import com.eliotlash.molang.variables.ExecutionContext;

/**
 * Operator class
 * <p>
 * This class is responsible for performing a calculation of two values
 * based on given operation.
 */
public class BinaryOperation implements IValue {
	public BinaryOperator operator;
	public IValue a;
	public IValue b;

	public BinaryOperation(BinaryOperator op, IValue a, IValue b) {
		this.operator = op;
		this.a = a;
		this.b = b;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return this.operator.apply(a.evaluate(ctx), b.evaluate(ctx));
	}

	@Override
	public String toString() {
		return operator.format(a.toString(), b.toString());
	}

	@Override
	public boolean isConstant() {
		return a.isConstant() && b.isConstant();
	}
}
