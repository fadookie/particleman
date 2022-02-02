package com.eliotlash.molang.math.functions.limit;

import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.functions.Function;
import com.eliotlash.molang.utils.MathUtils;

public class Clamp extends Function {
	public Clamp(IValue[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 3;
	}

	@Override
	public double get() {
		return MathUtils.clamp(this.getArg(0), this.getArg(1), this.getArg(2));
	}
}
