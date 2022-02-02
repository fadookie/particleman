package com.eliotlash.molang.math.functions.utility;

import com.eliotlash.molang.math.IValue;
import com.eliotlash.molang.math.functions.Function;
import com.eliotlash.molang.utils.Interpolations;

public class LerpRotate extends Function {
	public LerpRotate(IValue[] values, String name) throws Exception {
		super(values, name);
	}

	@Override
	public int getRequiredArguments() {
		return 3;
	}

	@Override
	public double get() {
		return Interpolations.lerpYaw(this.getArg(0), this.getArg(1), this.getArg(2));
	}
}
