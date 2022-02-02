package com.eliotlash.mclib.math.functions;

import com.eliotlash.mclib.math.IValue;

@FunctionalInterface
public interface FunctionFactory {
	Function create(IValue[] args, String name) throws Exception;
}
