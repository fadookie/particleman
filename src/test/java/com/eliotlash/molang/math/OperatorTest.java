package com.eliotlash.molang.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class OperatorTest {

	@Test
	void isOperatorTest() {
		assertTrue(Operator.isOperator("+"));
		assertTrue(Operator.isOperator("-"));
		assertTrue(Operator.isOperator("*"));
		assertTrue(Operator.isOperator("/"));
		assertTrue(Operator.isOperator("%"));
		assertTrue(Operator.isOperator("^"));
		assertTrue(Operator.isOperator("&&"));
		assertTrue(Operator.isOperator("||"));
		assertTrue(Operator.isOperator("<"));
		assertTrue(Operator.isOperator("<="));
		assertTrue(Operator.isOperator(">="));
		assertTrue(Operator.isOperator(">"));
		assertTrue(Operator.isOperator("=="));
		assertTrue(Operator.isOperator("!="));
		assertFalse(Operator.isOperator(""));
		assertFalse(Operator.isOperator("//"));
		assertFalse(Operator.isOperator("\\"));
		assertFalse(Operator.isOperator("25"));
	}
}
