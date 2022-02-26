package com.eliotlash.molang.expressions;

import com.eliotlash.molang.variables.ExecutionContext;
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
	public double evaluate(ExecutionContext ctx) {
		double value = this.expression.evaluate(ctx);



		return value;
	}

	@Override
	public String toString() {
		return this.variable.getName() + " = " + this.expression.toString();
	}
}
