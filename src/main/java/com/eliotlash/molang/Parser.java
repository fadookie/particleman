package com.eliotlash.molang;

import static com.eliotlash.molang.TokenType.*;

import java.util.ArrayList;
import java.util.List;

import com.eliotlash.molang.ast.*;

public class Parser {


	private final List<Token> input;
	private int current = 0;

	public Parser(List<Token> input) {
		this.input = input;
	}

	public Expr parse() {
		Expr expr = expression();
		consume(EOF, "Expect end of expression.");
		return expr;
	}

	private Expr expression() {
		return assignment();
	}

	private Expr assignment() {
		Expr expr = boolOp();

		if (match(EQUALS)) {
			Token equals = previous();
			Expr value = assignment();

			if (expr instanceof Assignable v) {
				return new Expr.Assignment(v, value);
			}

			throw error(equals, "Invalid assignment target.");
		}

		return expr;
	}

	private Expr boolOp() {
		var expr = equality();

		while (match(AND, OR)) {
			Token operator = previous();
			Expr right = equality();
			expr = new Expr.BinOp(Operator.from(operator), expr, right);
		}
		return expr;
	}

	private Expr equality() {
		Expr expr = comparison();

		while (match(BANG_EQUAL, EQUAL_EQUAL)) {
			Token operator = previous();
			Expr right = comparison();
			expr = new Expr.BinOp(Operator.from(operator), expr, right);
		}

		return expr;
	}

	private Expr comparison() {
		var expr = term();

		while (match(GREATER_THAN, GREATER_EQUAL, LESS_THAN, LESS_EQUAL)) {
			Token operator = previous();
			Expr right = term();
			expr = new Expr.BinOp(Operator.from(operator), expr, right);
		}

		return expr;
	}

	private Expr term() {
		var expr = factor();

		while (match(MINUS, PLUS)) {
			Token operator = previous();
			Expr right = factor();
			expr = new Expr.BinOp(Operator.from(operator), expr, right);
		}
		return expr;
	}

	private Expr factor() {
		var expr = exponentiation();
		while (match(STAR, SLASH, PERCENT)) {
			Token operator = previous();
			Expr right = exponentiation();
			expr = new Expr.BinOp(Operator.from(operator), expr, right);
		}
		return expr;
	}

	private Expr exponentiation() {
		var expr = unary();
		while (match(CARET)) {
			Token operator = previous();
			Expr right = unary();
			expr = new Expr.BinOp(Operator.from(operator), expr, right);
		}
		return expr;
	}

	private Expr unary() {
		if (match(NOT)) {
			return new Expr.Not(unary());
		}
		if (match(MINUS)) {
			return new Expr.Negate(unary());
		}
		return access();
	}

	private Expr access() {
		Expr expr = primary();

		if (match(DOT)) {
			Token dot = previous();

			if (!(expr instanceof Expr.Variable v)) {
				throw error(dot, "Invalid access target.");
			}

			if (match(IDENTIFIER)) {
				return finishAccess(v);
			} else {
				throw error(dot, "Expect identifier after '.'.");
			}
		} else {
			return expr;
		}
	}

	private Expr finishAccess(Expr.Variable v) {
		Token name = previous();

		if (match(OPEN_PAREN)) {
			List<Expr> arguments = arguments();

			return new Expr.Call(v, name.lexeme(), arguments);
		} else {
			return new Expr.Access(v, name.lexeme());
		}
	}

	private List<Expr> arguments() {
		List<Expr> arguments = new ArrayList<>();
		if (!check(CLOSE_PAREN)) {
			do {
				arguments.add(expression());
			} while (match(COMMA));
		}

		consume(CLOSE_PAREN, "Expect ')' after arguments.");
		return arguments;
	}

	private Expr primary() {
		if (match(IDENTIFIER)) {
			return new Expr.Variable(previous().lexeme());
		}

		if (match(NUMERAL)) {
			return new Expr.Constant(Double.parseDouble(previous().lexeme()));
		}

		if (match(OPEN_PAREN)) {
			Expr expr = expression();
			consume(CLOSE_PAREN, "Expect ')' after expression.");
			return new Expr.Group(expr);
		}

		throw error(peek(), "Expect expression.");
	}

	private Token consume(TokenType required, String error) {
		if (check(required)) {
			return advance();
		}

		throw error(peek(), error);
	}

	private boolean matchKeyword(Keyword value) {
		if (check(IDENTIFIER) && value.matches(peek().lexeme())) {
			advance();
			return true;
		}

		return false;
	}

	private boolean match(TokenType... types) {
		for (TokenType type : types) {
			if (check(type)) {
				advance();
				return true;
			}
		}

		return false;
	}

	private ParseException error(Token faulty, String message) {
		return new ParseException(message);
	}

	private boolean isAtEnd() {
		return peek().tokenType() == EOF;
	}

	private boolean check(TokenType type) {
		return peek().tokenType() == type;
	}

	private boolean checkNext(TokenType type) {
		return peekNext().tokenType() == type;
	}

	private Token advance() {
		return input.get(current++);
	}

	private Token peek() {
		if (current >= input.size()) return input.get(input.size() - 1);
		return input.get(current);
	}

	private Token peekNext() {
		if (current + 1 >= input.size()) return input.get(input.size() - 1);
		return input.get(current + 1);
	}

	private Token previous() {
		return input.get(current - 1);
	}
}
