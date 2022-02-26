package com.eliotlash.molang;

import com.eliotlash.molang.math.IValue;

public class Molang {
	public static IValue parse(String expression) {
		return new Parser(Lexer.tokenize(expression)).parse();
	}
}
