package com.eliotlash.molang.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MathBuilderTest {

	MathBuilder mathBuilder;

	@BeforeEach
	void setup() {
		mathBuilder = new MathBuilder();
	}

	@Test
	void testTrig() throws Exception {
		assertEquals(1.0, evaluate("sin(PI/2)"), 0.0001);
		assertEquals(0.0, evaluate("sin(0)"), 0.0001);

		assertEquals(0.0, evaluate("cos(PI/2)"), 0.0001);
		assertEquals(1.0, evaluate("cos(0)"), 0.0001);
	}

	@Test
	void testRounding() throws Exception {
		assertEquals(5.0, evaluate("floor(5.4)"));
		assertEquals(5.0, evaluate("floor(5.9)"));

		assertEquals(5.0, evaluate("round(5.1)"));
		assertEquals(5.0, evaluate("round(4.9)"));

		assertEquals(5.0, evaluate("ceil(4.1)"));
		assertEquals(5.0, evaluate("ceil(4.9)"));

		assertEquals(5.0, evaluate("trunc(5.1)"));
		assertEquals(-5.0, evaluate("trunc(-5.1)"));
	}

	@Test
	void testSelection() throws Exception {
		assertEquals(0.0, evaluate("clamp(-1, 0, 1)"));
		assertEquals(1.0, evaluate("clamp(20, 0, 1)"));
		assertEquals(0.5, evaluate("clamp(0.5, 0, 1)"));

		assertEquals(1.0, evaluate("max(0, 1)"));
		assertEquals(1.0, evaluate("max(1, 0)"));
		assertEquals(1.0, evaluate("max(1, -10)"));
		assertEquals(-20.0, evaluate("max(-20, -70)"));

		assertEquals(0.0, evaluate("min(0, 1)"));
		assertEquals(0.0, evaluate("min(1, 0)"));
		assertEquals(-10.0, evaluate("min(1, -10)"));
		assertEquals(-70.0, evaluate("min(-20, -70)"));
	}

	@Test
	void testMiscFunctions() throws Exception {
		assertEquals(10.0, evaluate("abs(-10)"));
		assertEquals(10.0, evaluate("abs(10)"));

		assertEquals(Math.E, evaluate("exp(1)"));
		assertEquals(0.0, evaluate("ln(1)"));
		assertEquals(2.0, evaluate("sqrt(4)"));
		assertEquals(2.0, evaluate("mod(5, 3)"));
		assertEquals(100.0, evaluate("pow(10, 2)"));
	}

	private double evaluate(String expression) throws Exception {
		return mathBuilder.parse(expression).get();
	}
}
