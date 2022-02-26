package com.eliotlash.molang;

import static com.eliotlash.molang.TokenType.*;

import java.util.List;
import java.util.Locale;

import com.eliotlash.molang.expressions.MolangAssignment;
import com.eliotlash.molang.math.*;

public class Parser {


	private final List<Token> input;
	private int current = 0;

	public Parser(List<Token> input) {
		this.input = input;
	}

	public IValue parse() {
		IValue expr = expression();
		consume(EOF, "Expect end of expression.");
		return expr;
	}

	private IValue expression() {
		return assignment();
	}

	private IValue assignment() {
		IValue expr = equality();

		if (match(EQUALS)) {
			Token equals = previous();
			IValue value = assignment();

			if (expr instanceof Variable) {
				return new MolangAssignment((Variable) expr, value);
			}

			throw new MolangException("Invalid assignment target.");
		}

		return expr;
	}

	private IValue equality() {
		IValue expr = comparison();

		while (match(BANG_EQUAL, EQUAL_EQUAL)) {
			Token operator = previous();
			IValue right = comparison();
			expr = new BinaryOperation(Operator.from(operator), expr, right);
		}

		return expr;
	}

	private IValue comparison() {
		var expr = term();

		while (match(GREATER_THAN, GREATER_EQUAL, LESS_THAN, LESS_EQUAL)) {
			Token operator = previous();
			IValue right = term();
			expr = new BinaryOperation(Operator.from(operator), expr, right);
		}

		return expr;
	}

	private IValue term() {
		var expr = factor();

		while (match(MINUS, PLUS)) {
			Token operator = previous();
			IValue right = factor();
			expr = new BinaryOperation(Operator.from(operator), expr, right);
		}
		return expr;
	}

	private IValue factor() {
		var expr = unary();
		while (match(STAR, SLASH, PERCENT)) {
			Token operator = previous();
			IValue right = unary();
			expr = new BinaryOperation(Operator.from(operator), expr, right);
		}
		return expr;
	}

	private IValue unary() {
		if (match(NOT)) {
			return new BooleanNot(primary());
		}
		if (match(MINUS)) {
			return new Negative(primary());
		}
		return primary();
	}



	private IValue primary() {
		if (match(IDENTIFIER)) {
			var base = previous();
			if (match(DOT) && match(IDENTIFIER)) {
				var extension = previous();

				return new Variable(base.lexeme() + '.' + extension.lexeme());
			}

			throw new MolangException("Expected '.' after identifier.");
		}

		if (match(NUMERAL)) {
			return new Constant(Double.parseDouble(previous().lexeme()));
		}

		if (match(OPEN_PAREN)) {
			IValue expr = expression();
			consume(CLOSE_PAREN, "Expect ')' after expression.");
			return new Group(expr);
		}

		throw new MolangException("Expect expression.");
	}

	private Token consume(TokenType required, String error) {
		if (check(required)) {
			return advance();
		}

		throw new MolangException(error);
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
