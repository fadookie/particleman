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

	record BinOpTest(Operator op, double lhs, double rhs, double expectedResult) {
	}

	static Stream<BinOpTest> bounceTestProvider() {
		return Stream.of(new BinOpTest(Operator.ADD, 1, 2, 3),
				new BinOpTest(Operator.SUB, 1, 2, -1),
				new BinOpTest(Operator.MUL, 1, 2, 2),
				new BinOpTest(Operator.DIV, 1, 2, 0.5),
				new BinOpTest(Operator.MOD, 1, 2, 1),
				// POW
				new BinOpTest(Operator.POW, 1, 2, 1),
				new BinOpTest(Operator.POW, 2, 4, 16),
				new BinOpTest(Operator.POW, 2, 0, 1),
				// AND
				new BinOpTest(Operator.AND, 1, 1, 1),
				new BinOpTest(Operator.AND, 0, 1, 0),
				new BinOpTest(Operator.AND, 1, 0, 0),
				new BinOpTest(Operator.AND, 0, 0, 0),
				// OR
				new BinOpTest(Operator.OR, 1, 1, 1),
				new BinOpTest(Operator.OR, 0, 1, 1),
				new BinOpTest(Operator.OR, 1, 0, 1),
				new BinOpTest(Operator.OR, 0, 0, 0),
				// EQ
				new BinOpTest(Operator.EQ, 1, 1, 1),
				new BinOpTest(Operator.EQ, 1, 0, 0),
				new BinOpTest(Operator.EQ, 20, 20, 1),
				new BinOpTest(Operator.EQ, 20, 40, 0),
				// NEQ
				new BinOpTest(Operator.NEQ, 1, 1, 0),
				new BinOpTest(Operator.NEQ, 1, 0, 1),
				new BinOpTest(Operator.NEQ, 20, 20, 0),
				new BinOpTest(Operator.NEQ, 20, 40, 1),
				// LT
				new BinOpTest(Operator.LT, 1, 2, 1),
				new BinOpTest(Operator.LT, 1, 1, 0),
				new BinOpTest(Operator.LT, 1, 0, 0),
				// GT
				new BinOpTest(Operator.GT, 1, 2, 0),
				new BinOpTest(Operator.GT, 1, 1, 0),
				new BinOpTest(Operator.GT, 1, 0, 1),
				// LEQ
				new BinOpTest(Operator.LEQ, 1, 2, 1),
				new BinOpTest(Operator.LEQ, 1, 1, 1),
				new BinOpTest(Operator.LEQ, 1, 0, 0),
				// GEQ
				new BinOpTest(Operator.GEQ, 1, 2, 0),
				new BinOpTest(Operator.GEQ, 1, 1, 1),
				new BinOpTest(Operator.GEQ, 1, 0, 1)
		);
	}

	@ParameterizedTest
	@MethodSource("bounceTestProvider")
	void visitBinOp(BinOpTest args) {
		Double result = new Expr.BinOp(args.op, c(args.lhs), c(args.rhs)).accept(eval);
		assertEquals(args.expectedResult, result, 0.000001);
	}
}
