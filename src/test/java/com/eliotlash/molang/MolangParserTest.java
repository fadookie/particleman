package com.eliotlash.molang;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.eliotlash.molang.expressions.MolangExpression;
import com.google.gson.JsonPrimitive;

class MolangParserTest {
	MolangParser parser;

	@BeforeEach
	void setUp() {
		parser = new MolangParser();
	}

	@Test
	void setValue() throws MolangException {
		MolangExpression expr = parser.parseOneLine("foo + 1");
		parser.setValue("foo", 1.0);
		assertEquals(2.0, expr.get());
	}

	@Test
	void getVariable() throws MolangException {
		assertNotNull(parser.getOrCreateVariable("bar"));
	}

	@Test
	void parseJson() throws MolangException {
		JsonPrimitive primitive = new JsonPrimitive("2 + 2");
		MolangExpression expr = parser.parseJson(primitive);
		assertEquals(4.0, expr.get());
	}

	@Test
	void parseExpression() throws MolangException {
		MolangExpression expr = parser.parseExpression("temp.baz = 1; temp.baz + 1;");
		assertEquals(2.0, expr.get());
	}

	@Test
	void parseOneLine() throws MolangException {
		MolangExpression expr = parser.parseOneLine("2 + 2");
		assertEquals(4.0, expr.get());
	}

	@Test
	void parseComplexExpression() throws MolangException {
		MolangExpression mulAdd = parser.parseOneLine("2 + 2 * 2");
		MolangExpression addMul = parser.parseOneLine("(2 + 2) * 2");
		assertEquals(6.0, mulAdd.get());
		assertEquals(8.0, addMul.get());
	}

	@Test
	void parseWadoosExpr() throws MolangException {
		MolangExpression expr = parser.parseOneLine("1 + math.sin(-90) * 0.02");
		assertEquals(0.98, expr.get());
	}

	@Test
	void isOperator() {
		assertTrue(parser.isOperator("+"));
		assertTrue(parser.isOperator("-"));
		assertTrue(parser.isOperator("/"));
		assertTrue(parser.isOperator("*"));
		assertTrue(parser.isOperator("%"));
		assertFalse(parser.isOperator("0"));
	}


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
		return parser.parse(expression).get();
	}

	private void assertConstant(String expression) throws Exception {
		assertTrue(parser.parse(expression).isConstant());
	}

	private void assertNotConstant(String expression) throws Exception {
		assertFalse(parser.parse(expression).isConstant());
	}
}
