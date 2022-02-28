package com.eliotlash.molang.ast;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.eliotlash.molang.TestBase;

class EvaluatorTest extends TestBase {

	private Evaluator eval;

	@BeforeEach
	void setUp() throws Exception {
		eval = new Evaluator();
	}

	@Test
	void visitAccess() {
	}

	@Test
	void visitAssignment() {
	}

	@Test
	void visitCall() {
	}

	@Test
	void visitConstant() {
		assertEquals(20, eval.visitConstant(c(20)));
	}

	@Test
	void visitGroup() {
		assertEquals(20, eval.visitGroup(paren(c(20))));
	}

	@Test
	void visitNegate() {
		assertEquals(-20, eval.visitNegate(new Expr.Negate(c(20))));
	}

	@Test
	void visitNot() {
		assertEquals(0, eval.visitNot(new Expr.Not(c(20))));
		assertEquals(1, eval.visitNot(new Expr.Not(c(0))));
	}

	@Test
	void visitTernary() {
		assertEquals(10, eval.visitTernary(new Expr.Ternary(c(1), c(10), c(30))));
		assertEquals(30, eval.visitTernary(new Expr.Ternary(c(0), c(10), c(30))));
	}

	public static Stream<BinOpTest> binOpTestProvider() {
		return BinOpTest.provider();
	}

	@ParameterizedTest
	@MethodSource("binOpTestProvider")
	void visitBinOp(BinOpTest args) {
		Double result = new Expr.BinOp(args.op(), c(args.lhs()), c(args.rhs())).accept(eval);
		assertEquals(args.expectedResult(), result, 0.000001);
	}
}
