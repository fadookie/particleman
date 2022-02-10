package com.eliotlash.molang;

import java.util.HashMap;
import java.util.Map;

import com.eliotlash.molang.functions.CosDegrees;
import com.eliotlash.molang.functions.SinDegrees;
import com.eliotlash.molang.math.functions.FunctionFactory;
import com.eliotlash.molang.math.functions.classic.*;
import com.eliotlash.molang.math.functions.limit.Clamp;
import com.eliotlash.molang.math.functions.limit.Max;
import com.eliotlash.molang.math.functions.limit.Min;
import com.eliotlash.molang.math.functions.rounding.Ceil;
import com.eliotlash.molang.math.functions.rounding.Floor;
import com.eliotlash.molang.math.functions.rounding.Round;
import com.eliotlash.molang.math.functions.rounding.Trunc;
import com.eliotlash.molang.math.functions.utility.Lerp;
import com.eliotlash.molang.math.functions.utility.LerpRotate;
import com.eliotlash.molang.math.functions.utility.Random;

public class BuiltinFunctions {

	/**
	 * Map of functions which can be used in the math expressions
	 */
	public Map<String, FunctionFactory> functions = new HashMap<>();

	public BuiltinFunctions() {
		/* Rounding functions */
		this.functions.put("math.floor", Floor::new);
		this.functions.put("math.round", Round::new);
		this.functions.put("math.ceil", Ceil::new);
		this.functions.put("math.trunc", Trunc::new);

		/* Selection and limit functions */
		this.functions.put("math.clamp", Clamp::new);
		this.functions.put("math.max", Max::new);
		this.functions.put("math.min", Min::new);

		/* Classical functions */
		this.functions.put("math.abs", Abs::new);
		this.functions.put("math.cos", CosDegrees::new);
		this.functions.put("math.sin", SinDegrees::new);
		this.functions.put("math.exp", Exp::new);
		this.functions.put("math.ln", Ln::new);
		this.functions.put("math.sqrt", Sqrt::new);
		this.functions.put("math.mod", Mod::new);
		this.functions.put("math.pow", Pow::new);

		/* Utility functions */
		this.functions.put("math.lerp", Lerp::new);
		this.functions.put("math.lerprotate", LerpRotate::new);
		this.functions.put("math.random", Random::new);
	}

	public boolean isFunction(String name) {
		return this.functions.containsKey(name);
	}

	public FunctionFactory get(String name) {
		return this.functions.get(name);
	}
}
