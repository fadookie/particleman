package com.eliotlash.molang.math.functions.rounding;

import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.functions.Function;

public class Ceil extends Function {
	public Ceil(IValue[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 1;
	}

	@Override
	public double get() {
		return Math.ceil(this.getArg(0));
	}
}
