package com.eliotlash.molang;

import java.util.Locale;

public enum Keyword {
	THIS("this"),
	RETURN("return"),
	LOOP("loop"),
	FOR_EACH("for_each"),
	BREAK("break"),
	CONTINUE("continue");

	private final String lexeme;

	Keyword(String s) {
		lexeme = s;
	}

	public boolean matches(String s) {
		return lexeme.equals(s.toLowerCase(Locale.ROOT));
	}
}
