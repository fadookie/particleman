package com.eliotlash.molang.math.functions;

import com.eliotlash.molang.math.IValue;

@FunctionalInterface
public interface FunctionFactory {
	Function create(IValue[] args, String name) throws Exception;
}
