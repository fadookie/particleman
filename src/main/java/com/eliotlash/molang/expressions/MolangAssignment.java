package com.eliotlash.molang.expressions;

import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.Variable;

public class MolangAssignment extends MolangExpression {
	public Variable variable;
	public IValue expression;

	public MolangAssignment(Variable variable, IValue expression) {
		this.variable = variable;
		this.expression = expression;
	}

	@Override
	public double get() {
		double value = this.expression.get();

		this.variable.set(value);

		return value;
	}

	@Override
	public String toString() {
		return this.variable.getName() + " = " + this.expression.toString();
	}
}
