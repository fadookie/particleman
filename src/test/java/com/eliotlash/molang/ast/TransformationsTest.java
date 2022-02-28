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
		assertEquals(c(-1), apply(Transformations.CONSTANT_PROPAGATION, "-1"));
		assertEquals(c(1), apply(Transformations.CONSTANT_PROPAGATION, "----1"));
		assertEquals(c(20), apply(Transformations.CONSTANT_PROPAGATION, "----20"));
		assertEquals(c(0), apply(Transformations.CONSTANT_PROPAGATION, "!1"));
		assertEquals(c(0), apply(Transformations.CONSTANT_PROPAGATION, "!20"));
		assertEquals(c(1), apply(Transformations.CONSTANT_PROPAGATION, "!0"));
	}

	public static Stream<BinOpTest> binOpTestProvider() {
		return BinOpTest.provider();
	}

	@ParameterizedTest
	@MethodSource("binOpTestProvider")
	void constantPropagationBinOp(BinOpTest args) {
		var input = args.lhs() + " " + args.op().sign + " " + args.rhs();
		assertEquals(c(args.expectedResult()), apply(Transformations.CONSTANT_PROPAGATION, input));
	}

	Expr apply(ASTTransformation t, String input) {
		Expr parse = Molang.parse(input);

		return parse.accept(t);
	}
}
