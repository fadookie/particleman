package com.eliotlash.molang;

import com.eliotlash.molang.lexer.Token;

public class ParseException extends RuntimeException {

	private final Token faulty;

	public ParseException(Token faulty, String message) {
		super(message);
		this.faulty = faulty;
	}

	@Override
	public String getMessage() {
		return faulty.lexeme() + ' ' + super.getMessage();
	}
}
