package com.eliotlash.molang.expressions;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.math.Constant;
import com.eliotlash.molang.math.IValue;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class MolangValue extends MolangExpression {
	public IValue value;

	public MolangValue(IValue value) {
		this.value = value;
	}

	@Override
	public double evaluate(ExecutionContext ctx) {
		return this.value.evaluate(ctx);
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public JsonElement toJson() {
		if (this.value instanceof Constant c) {
			return new JsonPrimitive(c.value());
		}

		return super.toJson();
	}

	@Override
	public boolean isConstant() {
		return value.isConstant();
	}
}
