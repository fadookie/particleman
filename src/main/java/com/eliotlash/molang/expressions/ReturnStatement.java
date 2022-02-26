package com.eliotlash.molang.expressions;

import com.eliotlash.molang.math.IValue;

public class ReturnStatement extends MolangValue {
	public ReturnStatement(IValue value) {
		super(value);
	}

	@Override
	public String toString() {
		return "return " + super.toString();
	}
}
