package com.eliotlash.molang.math;

import com.eliotlash.molang.variables.ExecutionContext;

/**
 * Ternary operator class
 * <p>
 * This value implementation allows to return different values depending on
 * given condition value
 */
public class Ternary implements IValue {
	public final IValue condition;
	public final IValue ifTrue;
	public final IValue ifFalse;

	public Ternary(IValue condition, IValue ifTrue, IValue ifFalse) {
		this.condition = condition;
		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return this.condition.evaluate(ctx) != 0 ? this.ifTrue.evaluate(ctx) : this.ifFalse.evaluate(ctx);
	}

	@Override
	public String toString() {
		return this.condition.toString() + " ? " + this.ifTrue.toString() + " : " + this.ifFalse.toString();
	}

	@Override
	public boolean isConstant() {
		return condition.isConstant() && ifTrue.isConstant() && ifFalse.isConstant();
	}
}
