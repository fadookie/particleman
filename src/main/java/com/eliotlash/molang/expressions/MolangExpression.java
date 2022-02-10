package com.eliotlash.molang.expressions;

import com.eliotlash.molang.math.Constant;
import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.Operator;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public abstract class MolangExpression implements IValue {

	public static boolean isZero(MolangExpression expression) {
		return isConstant(expression, 0);
	}

	public static boolean isOne(MolangExpression expression) {
		return isConstant(expression, 1);
	}

	public static boolean isConstant(MolangExpression expression, double x) {
		if (expression instanceof MolangValue value) {

			return value.value instanceof Constant && Operator.equals(value.value.get(), x);
		}

		return false;
	}

	public static boolean isExpressionConstant(MolangExpression expression) {
		if (expression instanceof MolangValue value) {

			return value.value instanceof Constant;
		}

		return false;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	public JsonElement toJson() {
		return new JsonPrimitive(this.toString());
	}
}
