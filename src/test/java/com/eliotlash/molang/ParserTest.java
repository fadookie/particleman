package com.eliotlash.molang;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.eliotlash.molang.ast.*;

public class ParserTest extends TestBase {

	@Test
	void testNumber() {
		assertEquals(c(20), parse("20"));
		assertEquals(new Expr.Negate(c(20)), parse("-20"));
	}

	@Test
	void testOperators() {
		for (Operator value : Operator.values()) {
			assertEquals(new Expr.BinOp(value, c(20), c(20)), parse("20 " + value.sign + " 20"));
		}
	}

	@Test
	void testMath() {
		Expr.Constant twenty = c(20);
		assertEquals(op(twenty, Operator.MUL, paren(op(twenty, Operator.ADD, twenty))), parse("20 * (20 + 20)"));
		assertEquals(op(op(twenty, Operator.MUL, twenty), Operator.ADD, twenty), parse("20 * 20 + 20"));
		assertEquals(op(paren(op(twenty, Operator.MUL, twenty)), Operator.ADD, twenty), parse("(20 * 20) + 20"));
	}

	@Test
	void testGrouping() {
		assertEquals(new Expr.Group(c(20)), parse("(20)"));
		assertEquals(new Expr.Negate(new Expr.Group(new Expr.Negate(c(20)))), parse("-(-20)"));

		assertThrows(ParseException.class, () -> parse("(20"));
		assertThrows(ParseException.class, () -> parse("20)"));
		assertThrows(ParseException.class, () -> parse("(((((((20))))))"));
	}

	@Test
	void testVariables() {
		assertEquals(new Expr.Variable("test"), parse("test"));
		assertEquals(new Expr.Variable("test_other"), parse("test_other"));
	}

	@Test
	void testAccess() {
		assertEquals(access("query", "test"), parse("query.test"));
		assertEquals(access("q", "test"), parse("q.test"));
	}

	@Test
	void testAssignment() {
		assertEquals(new Expr.Assignment(access("query", "test"), c(20)), parse("query.test = 20"));
		assertEquals(new Expr.Assignment(access("query", "test"), op(c(20), Operator.ADD, c(20))), parse("query.test = 20 + 20"));
	}

	@Test
	void testCall() {
		assertEquals(new Expr.Call(new Expr.Variable("q"), "count", List.of(c(20))), parse("q.count(20)"));
		assertEquals(new Expr.Call(
				new Expr.Variable("q"),
				"count",
				List.of(c(20),
						c(40),
						op(c(20), Operator.MUL, c(40)))
		), parse("q.count(20, 40, 20 * 40)"));
	}

	Expr parse(String expr) {
		var p = new Parser(Lexer.tokenize(expr));
		return p.parse();
	}
}
