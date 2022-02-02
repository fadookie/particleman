package com.eliotlash.molang.expressions;

import com.eliotlash.mclib.math.IValue;
import com.eliotlash.molang.MolangParser;

public class ReturnStatement extends MolangValue {
	public ReturnStatement(IValue value) {
		super(value);
	}

	@Override
	public String toString() {
		return MolangParser.RETURN + super.toString();
	}
}
