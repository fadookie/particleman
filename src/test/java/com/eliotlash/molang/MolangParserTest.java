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
		assertNotNull(parser.getVariable("bar"));
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
}
