package com.eliotlash.molang;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.eliotlash.molang.ast.*;

public class ParserTest extends TestBase {

	@Test
	void testStmt() {
		Expr setThing = call("v", "set_thing", access("q", "thing"));

		assertEquals(new Stmt.Expression(setThing), s("v.set_thing(q.thing);"));
		assertThrows(ParseException.class, () -> s("v.set_thing(q.thing)"));

		assertEquals(new Stmt.Return(c(20)), s("return 20;"));
		assertThrows(ParseException.class, () -> s("return 20"));

		assertEquals(new Stmt.Loop(c(20), setThing), s("loop(20, v.set_thing(q.thing));"));
		assertThrows(ParseException.class, () -> s("loop(20, v.set_thing(q.thing))"));

		assertEquals(new Stmt.Break(), s("break;"));
		assertThrows(ParseException.class, () -> s("break"));

		assertEquals(new Stmt.Continue(), s("continue;"));
		assertThrows(ParseException.class, () -> s("continue"));
	}

	@Test
	void testNumber() {
		assertEquals(c(20), e("20"));
		assertEquals(new Expr.Negate(c(20)), e("-20"));
	}

	@Test
	void testOperators() {
		for (Operator value : Operator.values()) {
			assertEquals(new Expr.BinOp(value, c(20), c(20)), e("20 " + value.sign + " 20"));
		}
	}

	@Test
	void testMath() {
		Expr.Constant twenty = c(20);
		assertEquals(op(twenty, Operator.MUL, paren(op(twenty, Operator.ADD, twenty))), e("20 * (20 + 20)"));
		assertEquals(op(op(twenty, Operator.MUL, twenty), Operator.ADD, twenty), e("20 * 20 + 20"));
		assertEquals(op(paren(op(twenty, Operator.MUL, twenty)), Operator.ADD, twenty), e("(20 * 20) + 20"));
	}

	@Test
	void testPrecedence() {
		Expr.Constant one = c(1);
		Expr.Variable a = v("a");
		Expr.Access acc = access("q", "test");

		// assignment/disjunction
		assertEquals(new Expr.Assignment(acc, op(one, Operator.OR, one)), e("q.test = 1 || 1"));

		// disjunction/conjunction
		assertEquals(op(one, Operator.OR, op(one, Operator.AND, one)), e("1 || 1 && 1"));
		assertEquals(op(op(one, Operator.AND, one), Operator.OR, one), e("1 && 1 || 1"));

		// conjunction/equality
		assertEquals(op(one, Operator.AND, op(one, Operator.EQ, one)), e("1 && 1 == 1"));
		assertEquals(op(op(one, Operator.NEQ, one), Operator.AND, one), e("1 != 1 && 1"));

		// equality/comparison
		assertEquals(op(one, Operator.EQ, op(one, Operator.LT, one)), e("1 == 1 < 1"));
		assertEquals(op(op(one, Operator.GT, one), Operator.EQ, one), e("1 > 1 == 1"));

		// comparison/coalesce
		assertEquals(op(one, Operator.LT, new Expr.Coalesce(a, one)), e("1 < a ?? 1"));
		assertEquals(op(new Expr.Coalesce(a, one), Operator.LT, one), e("a ?? 1 < 1"));

		// coalesce/term
		assertEquals(new Expr.Coalesce(a, op(one, Operator.SUB, one)), e("a ?? 1 - 1"));
		assertEquals(new Expr.Coalesce(op(one, Operator.ADD, a), one), e("1 + a ?? 1"));

		// term/factor
		assertEquals(op(op(one, Operator.MUL, one), Operator.ADD, one), e("1 * 1 + 1"));
		assertEquals(op(one, Operator.SUB, op(one, Operator.DIV, one)), e("1 - 1 / 1"));

		// factor/exponentiation
		assertEquals(op(op(one, Operator.POW, one), Operator.MUL, one), e("1 ^ 1 * 1"));
		assertEquals(op(one, Operator.DIV, op(one, Operator.POW, one)), e("1 / 1 ^ 1"));

		// exponentiation/unary
		assertEquals(op(one, Operator.POW, new Expr.Negate(one)), e("1 ^ -1"));
		assertEquals(op(new Expr.Negate(one), Operator.POW, new Expr.Negate(one)), e("-1 ^ -1"));
		assertEquals(op(new Expr.Negate(one), Operator.POW, one), e("-1 ^ 1"));
	}

	@Test
	void testCoalesce() {
		assertEquals(new Expr.Coalesce(access("q", "test1"), access("q", "test2")), e("q.test1 ?? q.test2"));
		assertEquals(op(c(2), Operator.ADD, paren(new Expr.Coalesce(access("q", "test1"), access("q", "test2")))), e("2 + (q.test1 ?? q.test2)"));
	}

	@Test
	void testGrouping() {
		assertEquals(new Expr.Group(c(20)), e("(20)"));
		assertEquals(new Expr.Negate(new Expr.Group(new Expr.Negate(c(20)))), e("-(-20)"));

		assertThrows(ParseException.class, () -> e("(20"));
		assertThrows(ParseException.class, () -> e("20)"));
		assertThrows(ParseException.class, () -> e("(((((((20))))))"));
	}

	@Test
	void testVariables() {
		assertEquals(v("test"), e("test"));
		assertEquals(v("test_other"), e("test_other"));
	}

	@Test
	void testAccess() {
		assertEquals(access("query", "test"), e("query.test"));
		assertEquals(access("q", "test"), e("q.test"));
	}

	@Test
	void testAssignment() {
		Expr.Constant twenty = c(20);
		Expr.Access acc = access("query", "test");

		assertEquals(new Expr.Assignment(acc, twenty), e("query.test = 20"));
		assertEquals(new Expr.Assignment(acc, new Expr.Assignment(acc, twenty)), e("query.test = query.test = 20"));
		assertEquals(new Expr.Assignment(acc, op(twenty, Operator.ADD, twenty)), e("query.test = 20 + 20"));
		assertThrows(ParseException.class, () -> e("fail = 20"));
		assertThrows(ParseException.class, () -> e("20 = 20"));
		assertThrows(ParseException.class, () -> e("(query.fail) = 20"));
	}

	@Test
	void testCall() {
		assertEquals(new Expr.Call(v("q"), "count", List.of(c(20))), e("q.count(20)"));
		assertEquals(new Expr.Call(
				v("q"),
				"count",
				List.of(c(20),
						c(40),
						op(c(20), Operator.MUL, c(40)))
		), e("q.count(20, 40, 20 * 40)"));
	}

	@Test
	void testBlock() {
		assertEquals(new Expr.Block(List.of()), e("{ }"));
		assertEquals(new Expr.Block(List.of(new Stmt.Expression(new Expr.Block(List.of())))), e("{ { }; }"));

		Expr.Access tTest = access("t", "test");
		List<Stmt> stmts = List.of(
				new Stmt.Expression(new Expr.Assignment(tTest, c(20))),
				new Stmt.Return(tTest)
		);
		assertEquals(new Expr.Block(stmts), e("{ t.test = 20; return t.test; }"));
	}

	/**
	 * Parses the input as an expression.
	 */
	Expr e(String expr) {
		return Molang.parseExpression(expr);
	}

	/**
	 * Parses the input as a statement.
	 */
	Stmt s(String expr) {
		return Molang.parseSingle(expr);
	}
}
