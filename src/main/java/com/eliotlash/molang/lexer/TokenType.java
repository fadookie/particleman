package com.eliotlash.molang.lexer;

public enum TokenType {

	/**
	 * A token representing a number.
	 */
	NUMERAL,

	/**
	 * An identifier, i.e. a variable name or a keyword.
	 * "query" or "break" or "return" or "foo_bar"
	 */
	IDENTIFIER,

	// Single character tokens:

	// '!'
	NOT,
	// '='
	EQUALS,
	// '<'
	LESS_THAN,
	// '>'
	GREATER_THAN,
	// '+'
	PLUS,
	// '-'
	MINUS,
	// '*'
	STAR,
	// '/'
	SLASH,
	// '%'
	PERCENT,
	// '('
	OPEN_PAREN,
	// ')'
	CLOSE_PAREN,
	// '['
	OPEN_BRACKET,
	// ']'
	CLOSE_BRACKET,
	// '{'
	OPEN_BRACE,
	// '}'
	CLOSE_BRACE,
	// ','
	COMMA,
	// ';'
	SEMICOLON,
	// ':'
	COLON,
	// '?'
	QUESTION,
	// '.'
	DOT,
	// '^'
	CARET,

	// Two-character tokens:

	// '&&'
	AND,
	// '||'
	OR,
	// '!='
	BANG_EQUAL,
	// '=='
	EQUAL_EQUAL,
	// '<='
	LESS_EQUAL,
	// '>='
	GREATER_EQUAL,
	// '->'
	ARROW,
	// '??'
	COALESCE,

	EOF,

	/**
	 * This is a special token used to mark an error in the program.
	 */
	ERROR;

	public boolean isOpenDelim() {
		return switch (this) {
			case OPEN_PAREN, OPEN_BRACKET, OPEN_BRACE -> true;
			default -> false;
		};
	}

	public boolean isCloseDelim() {
		return switch (this) {
			case CLOSE_PAREN, CLOSE_BRACKET, CLOSE_BRACE -> true;
			default -> false;
		};
	}
}
