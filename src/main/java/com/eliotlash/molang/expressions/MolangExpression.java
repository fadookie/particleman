package com.eliotlash.molang.expressions;

import com.eliotlash.molang.math.Constant;
import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.Operator;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public abstract class MolangExpression implements IValue {

	@Override
	public boolean isConstant() {
		return false;
	}

	public JsonElement toJson() {
		return new JsonPrimitive(this.toString());
	}
}
