package com.eliotlash.molang;

import com.eliotlash.molang.expressions.Expr;

public class Molang {
	public static Expr parse(String expression) {
		return new Parser(Lexer.tokenize(expression)).parse();
	}
}
