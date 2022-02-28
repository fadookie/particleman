package com.eliotlash.molang;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.eliotlash.molang.ast.Expr;
import com.eliotlash.molang.ast.Transformations;

class FunctionsTest {

	@Test
	void testConstant() throws Exception {
		assertConstant("PI");
		assertConstant("E");
		assertConstant("500");
		assertConstant("math.sqrt(2)");
		assertConstant("math.floor(2.5)");
		assertConstant("math.round(2.5)");
		assertConstant("math.ceil(2.5)");
		assertConstant("math.trunc(2.5)");
		assertConstant("math.clamp(10, 0, 1)");
		assertConstant("math.max(1, 2)");
		assertConstant("math.min(1, 2)");
		assertConstant("math.abs(-20)");
		assertConstant("math.cos(-20)");
		assertConstant("math.sin(60)");
		assertConstant("math.exp(5)");
		assertConstant("math.ln(E)");
		assertConstant("math.mod(10, 3)");
		assertConstant("math.pow(20, 2)");
	}

	@Test
	void testNotConstant() throws Exception {
		assertNotConstant("math.random()");
		assertNotConstant("math.sin(math.random())");
	}

	@Test
	void testTrig() throws Exception {
		assertEquals(1.0, evaluate("math.sin(90)"), 0.0001);
		assertEquals(0.0, evaluate("math.sin(0)"), 0.0001);

		assertEquals(0.0, evaluate("math.cos(90)"), 0.0001);
		assertEquals(1.0, evaluate("math.cos(0)"), 0.0001);
	}

	@Test
	void testRounding() throws Exception {
		assertEquals(5.0, evaluate("math.floor(5.4)"));
		assertEquals(5.0, evaluate("math.floor(5.9)"));

		assertEquals(5.0, evaluate("math.round(5.1)"));
		assertEquals(5.0, evaluate("math.round(4.9)"));

		assertEquals(5.0, evaluate("math.ceil(4.1)"));
		assertEquals(5.0, evaluate("math.ceil(4.9)"));

		assertEquals(5.0, evaluate("math.trunc(5.1)"));
		assertEquals(-5.0, evaluate("math.trunc(-5.1)"));
	}

	@Test
	void testSelection() throws Exception {
		assertEquals(0.0, evaluate("math.clamp(-1, 0, 1)"));
		assertEquals(1.0, evaluate("math.clamp(20, 0, 1)"));
		assertEquals(0.5, evaluate("math.clamp(0.5, 0, 1)"));

		assertEquals(1.0, evaluate("math.max(0, 1)"));
		assertEquals(1.0, evaluate("math.max(1, 0)"));
		assertEquals(1.0, evaluate("math.max(1, -10)"));
		assertEquals(-20.0, evaluate("math.max(-20, -70)"));

		assertEquals(0.0, evaluate("math.min(0, 1)"));
		assertEquals(0.0, evaluate("math.min(1, 0)"));
		assertEquals(-10.0, evaluate("math.min(1, -10)"));
		assertEquals(-70.0, evaluate("math.min(-20, -70)"));
	}

	@Test
	void testMiscFunctions() throws Exception {
		assertEquals(10.0, evaluate("math.abs(-10)"));
		assertEquals(10.0, evaluate("math.abs(10)"));

		assertEquals(Math.E, evaluate("math.exp(1)"));
		assertEquals(0.0, evaluate("math.ln(1)"));
		assertEquals(2.0, evaluate("math.sqrt(4)"));
		assertEquals(2.0, evaluate("math.mod(5, 3)"));
		assertEquals(100.0, evaluate("math.pow(10, 2)"));
	}

	private double evaluate(String expression) throws Exception {
		return 0.0;//parser.parse(expression).evaluate(ctx);
	}

	private void assertConstant(String expression) throws Exception {
		Expr expr = Molang.parseExpression(expression)
				.accept(Transformations.SIMPLIFY_CONSTANTS);

		assertTrue(expr instanceof Expr.Constant);
	}

	private void assertNotConstant(String expression) throws Exception {
		Expr expr = Molang.parseExpression(expression)
				.accept(Transformations.SIMPLIFY_CONSTANTS);

		assertFalse(expr instanceof Expr.Constant);
	}
}
