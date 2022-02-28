package com.eliotlash.molang.ast;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.eliotlash.molang.Molang;
import com.eliotlash.molang.TestBase;

class TransformationTest extends TestBase {

	@Test
	void inlineParens() {
		assertEquals(c(1), apply(Transformation.INLINE_PARENS, "(1)"));
		assertEquals(c(1), apply(Transformation.INLINE_PARENS, "((((((((1))))))))"));
		Expr.Constant twenty = c(20);
		assertEquals(op(twenty, Operator.MUL, twenty), apply(Transformation.INLINE_PARENS, "(20 * 20)"));
		assertEquals(op(twenty, Operator.MUL, op(twenty, Operator.ADD, twenty)), apply(Transformation.INLINE_PARENS, "20 * (20 + 20)"));
	}

	Expr apply(Transformation t, String input) {
		Expr parse = Molang.parse(input);

		return parse.accept(t);
	}
}
