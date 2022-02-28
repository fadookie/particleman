package com.eliotlash.molang.ast;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.eliotlash.molang.Molang;
import com.eliotlash.molang.TestBase;

class TransformationsTest extends TestBase {

	@Test
	void inlineParens() {
		ASTTransformation t = Transformations.INLINE_PARENS;

		assertEquals(c(1), apply(t, "(1)"));
		assertEquals(c(1), apply(t, "((((((((1))))))))"));
		Expr.Constant twenty = c(20);
		assertEquals(op(twenty, Operator.MUL, twenty), apply(t, "(20 * 20)"));
		assertEquals(op(twenty, Operator.MUL, op(twenty, Operator.ADD, twenty)), apply(t, "20 * (20 + 20)"));
	}

	@Test
	void constantPropagation() {
		ASTTransformation t = Transformations.SIMPLIFY_CONSTANTS;
		assertEquals(c(1), apply(t, "(1)"));
		assertEquals(c(1), apply(t, "((((((((1))))))))"));
		assertEquals(c(420), apply(t, "(20 * 20) + 20"));
		assertEquals(c(800), apply(t, "20 * (20 + 20)"));

		assertEquals(c(-1), apply(t, "-1"));
		assertEquals(c(1), apply(t, "----1"));
		assertEquals(c(20), apply(t, "----20"));
		assertEquals(c(0), apply(t, "!1"));
		assertEquals(c(0), apply(t, "!20"));
		assertEquals(c(1), apply(t, "!0"));
	}

	public static Stream<BinOpTest> binOpTestProvider() {
		return BinOpTest.provider();
	}

	@ParameterizedTest
	@MethodSource("binOpTestProvider")
	void constantPropagationBinOp(BinOpTest args) {
		var input = args.lhs() + " " + args.op().sign + " " + args.rhs();
		assertEquals(c(args.expectedResult()), apply(Transformations.SIMPLIFY_CONSTANTS, input));
	}

	Expr apply(ASTTransformation t, String input) {
		Expr parse = Molang.parseExpression(input);

		return parse.accept(t);
	}
}
