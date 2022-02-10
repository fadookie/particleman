package com.eliotlash.molang;

import java.util.HashMap;
import java.util.Map;

import com.eliotlash.molang.math.Constant;
import com.eliotlash.molang.math.IValue;

public class CompileConstants {

	/**
	 * Known, constant values that can be used in expressions.
	 */
	public Map<String, Constant> constants = new HashMap<>();

	public CompileConstants() {
		/* Some default values */
		this.registerConstant("PI", Math.PI);
		this.registerConstant("E", Math.E);
	}

	public void registerConstant(String name, double value) {
		this.constants.put(name, new Constant(value));
	}

	public IValue get(String name) {
		return this.constants.get(name);
	}
}
