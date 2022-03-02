package com.eliotlash.molang;

import java.util.List;

import com.eliotlash.molang.ast.Expr;
import com.eliotlash.molang.ast.Stmt;
import com.eliotlash.molang.lexer.Lexer;

public class Molang {
	/**
	 * Parses the input, expecting it to be a list of statements.
	 * @return The parsed list of statements.
	 */
	public static List<Stmt> parse(String expression) {
		return new Parser(Lexer.tokenize(expression)).parse();
	}

	/**
	 * Parses the input, expecting it to be a single statement.
	 * @return The parsed statement.
	 */
	public static Stmt parseSingle(String expr) {
		return new Parser(Lexer.tokenize(expr)).parseStatement();
	}

	/**
	 * Parses the input, expecting it to be a single expression.
	 * @return The parsed expression.
	 */
	public static Expr parseExpression(String expression) {
		return new Parser(Lexer.tokenize(expression)).parseExpression();
	}
}
