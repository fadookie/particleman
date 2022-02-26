package com.eliotlash.molang;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.eliotlash.molang.math.*;
import com.eliotlash.molang.variables.ExecutionContext;

public class ParserTest {

	ExecutionContext ctx;

	@BeforeEach
	public void setUp() {
		ctx = new ExecutionContext();
	}

	@Test
	void testNumber() {
		assertEquals(new Constant(20), parse("20"));
		assertEquals(new Negative(new Constant(20)), parse("-20"));
	}

	@Test
	void testGroupingError() {
		assertThrows(MolangException.class, () -> parse("(20"));
		assertThrows(MolangException.class, () -> parse("20)"));
	}

	@Test
	void testVariables() {
		assertEquals(new Variable("query.test"), parse("query.test"));
		assertThrows(MolangException.class, () -> parse("test"));
	}

	@Test
	void testGrouping() {
		assertEquals(new Group(new Constant(20)), parse("(20)"));
		assertEquals(new Negative(new Group(new Negative(new Constant(20)))), parse("-(-20)"));
	}

	IValue parse(String expr) {
		var p = new Parser(Lexer.tokenize(expr));
		return p.parse();
	}
}
