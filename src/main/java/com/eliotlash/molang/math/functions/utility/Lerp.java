package com.eliotlash.molang.math.functions.utility;

import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.functions.Function;
import com.eliotlash.molang.utils.Interpolations;

public class Lerp extends Function {
	public Lerp(IValue[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 3;
	}

	@Override
	public double get() {
		return Interpolations.lerp(this.getArg(0), this.getArg(1), this.getArg(2));
	}
}
