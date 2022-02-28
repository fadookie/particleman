package com.eliotlash.molang.ast;

import java.util.stream.Stream;

record BinOpTest(Operator op, double lhs, double rhs, double expectedResult) {

	@Override
	public String toString() {
		return "BinOpTest[" + expectedResult + " = " + lhs + ' ' + op + ' ' + rhs + ']';
	}

	static Stream<BinOpTest> provider() {
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
}
