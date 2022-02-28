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
	void testPrecedence() {
		Expr.Constant one = c(1);
		Expr.Variable a = v("a");
		Expr.Access acc = access("q", "test");

		// assignment/disjunction
		assertEquals(new Expr.Assignment(acc, op(one, Operator.OR, one)), parse("q.test = 1 || 1"));

		// disjunction/conjunction
		assertEquals(op(one, Operator.OR, op(one, Operator.AND, one)), parse("1 || 1 && 1"));
		assertEquals(op(op(one, Operator.AND, one), Operator.OR, one), parse("1 && 1 || 1"));

		// conjunction/equality
		assertEquals(op(one, Operator.AND, op(one, Operator.EQ, one)), parse("1 && 1 == 1"));
		assertEquals(op(op(one, Operator.NEQ, one), Operator.AND, one), parse("1 != 1 && 1"));

		// equality/comparison
		assertEquals(op(one, Operator.EQ, op(one, Operator.LT, one)), parse("1 == 1 < 1"));
		assertEquals(op(op(one, Operator.GT, one), Operator.EQ, one), parse("1 > 1 == 1"));

		// comparison/coalesce
		assertEquals(op(one, Operator.LT, new Expr.Coalesce(a, one)), parse("1 < a ?? 1"));
		assertEquals(op(new Expr.Coalesce(a, one), Operator.LT, one), parse("a ?? 1 < 1"));

		// coalesce/term
		assertEquals(new Expr.Coalesce(a, op(one, Operator.SUB, one)), parse("a ?? 1 - 1"));
		assertEquals(new Expr.Coalesce(op(one, Operator.ADD, a), one), parse("1 + a ?? 1"));

		// term/factor
		assertEquals(op(op(one, Operator.MUL, one), Operator.ADD, one), parse("1 * 1 + 1"));
		assertEquals(op(one, Operator.SUB, op(one, Operator.DIV, one)), parse("1 - 1 / 1"));

		// factor/exponentiation
		assertEquals(op(op(one, Operator.POW, one), Operator.MUL, one), parse("1 ^ 1 * 1"));
		assertEquals(op(one, Operator.DIV, op(one, Operator.POW, one)), parse("1 / 1 ^ 1"));

		// exponentiation/unary
		assertEquals(op(one, Operator.POW, new Expr.Negate(one)), parse("1 ^ -1"));
		assertEquals(op(new Expr.Negate(one), Operator.POW, new Expr.Negate(one)), parse("-1 ^ -1"));
		assertEquals(op(new Expr.Negate(one), Operator.POW, one), parse("-1 ^ 1"));
	}

	@Test
	void testCoalesce() {
		assertEquals(new Expr.Coalesce(access("q", "test1"), access("q", "test2")), parse("q.test1 ?? q.test2"));
		assertEquals(op(c(2), Operator.ADD, paren(new Expr.Coalesce(access("q", "test1"), access("q", "test2")))), parse("2 + (q.test1 ?? q.test2)"));
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
		assertEquals(v("test"), parse("test"));
		assertEquals(v("test_other"), parse("test_other"));
	}

	@Test
	void testAccess() {
		assertEquals(access("query", "test"), parse("query.test"));
		assertEquals(access("q", "test"), parse("q.test"));
	}

	@Test
	void testAssignment() {
		Expr.Constant twenty = c(20);
		Expr.Access acc = access("query", "test");

		assertEquals(new Expr.Assignment(acc, twenty), parse("query.test = 20"));
		assertEquals(new Expr.Assignment(acc, new Expr.Assignment(acc, twenty)), parse("query.test = query.test = 20"));
		assertEquals(new Expr.Assignment(acc, op(twenty, Operator.ADD, twenty)), parse("query.test = 20 + 20"));
		assertThrows(ParseException.class, () -> parse("fail = 20"));
		assertThrows(ParseException.class, () -> parse("20 = 20"));
		assertThrows(ParseException.class, () -> parse("(query.fail) = 20"));
	}

	@Test
	void testCall() {
		assertEquals(new Expr.Call(v("q"), "count", List.of(c(20))), parse("q.count(20)"));
		assertEquals(new Expr.Call(
				v("q"),
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
