package com.eliotlash.molang.expressions;

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
	public double get() {
		return this.value.get();
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public JsonElement toJson() {
		if (this.value instanceof Constant) {
			return new JsonPrimitive(this.value.get());
		}

		return super.toJson();
	}
}
