package com.eliotlash.molang.math.functions.classic;

import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.functions.Function;

public class Mod extends Function {
	public Mod(IValue[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 2;
	}

	@Override
	public double get() {
		return this.getArg(0) % this.getArg(1);
	}
}
