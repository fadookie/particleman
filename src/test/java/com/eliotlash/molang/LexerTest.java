package com.eliotlash.molang;

import static com.eliotlash.molang.lexer.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.eliotlash.molang.lexer.Lexer;
import com.eliotlash.molang.lexer.Token;
import com.eliotlash.molang.lexer.TokenType;

public class LexerTest {

	@Test
	void testIdentifier() {
		assertTokens("two", IDENTIFIER);
		assertTokens("query", IDENTIFIER);
		assertTokens("q", IDENTIFIER);
		assertTokens("moo_test", IDENTIFIER);
		assertTokens("very_long_identifier_that_nobody_would_use", IDENTIFIER);
		assertTokens("two identifiers", IDENTIFIER, IDENTIFIER);
		assertTokens("_underscore", IDENTIFIER);
		assertTokens("ident1with2numbers", IDENTIFIER);

		assertTokens("23number_then_ident", NUMERAL, IDENTIFIER);
	}

	@Test
	void testNumeral() {
		assertTokens("2", NUMERAL);
		assertTokens("20000", NUMERAL);
		assertTokens("20000.0", NUMERAL);
		assertTokens("2.4", NUMERAL);
		assertTokens("22020202.2021350421425304210", NUMERAL);
		assertTokens("22020202.", NUMERAL, DOT);
	}

	@Test
	void testContext() {
		assertTokens("query.is_alive", IDENTIFIER, DOT, IDENTIFIER);
		assertTokens("q.is_alive", IDENTIFIER, DOT, IDENTIFIER);
		assertTokens("math.sin(4 * PI)", IDENTIFIER, DOT, IDENTIFIER, OPEN_PAREN, NUMERAL, STAR, IDENTIFIER, CLOSE_PAREN);
	}

	@Test
	void testMath() {
		assertTokens("!", NOT);
		assertTokens("&&", AND);
		assertTokens("||", OR);

		assertTokens("+", PLUS);
		assertTokens("-", MINUS);
		assertTokens("*", STAR);
		assertTokens("/", SLASH);
		assertTokens("%", PERCENT);

		assertTokens("<=", LESS_EQUAL);
		assertTokens(">=", GREATER_EQUAL);
		assertTokens("==", EQUAL_EQUAL);

		assertTokens("<", LESS_THAN);
		assertTokens(">", GREATER_THAN);
		assertTokens("=", EQUALS);

		assertTokens("^", CARET);

		assertTokens("?", QUESTION);
		assertTokens("??", COALESCE);

		assertTokens("-thing", MINUS, IDENTIFIER);
		assertTokens("-(things)", MINUS, OPEN_PAREN, IDENTIFIER, CLOSE_PAREN);
	}

	@Test
	void testWhitespace() {
		assertTokens("2+2", NUMERAL, PLUS, NUMERAL);
		assertTokens("2 + 2", NUMERAL, PLUS, NUMERAL);
		assertTokens("2           +                2", NUMERAL, PLUS, NUMERAL);
		assertTokens("           2           +                2    ", NUMERAL, PLUS, NUMERAL);
	}

	@Test
	void testTokenSoup() {
		assertTokens("////*220420422 abcuhedaouddtoeuh", SLASH, SLASH, SLASH, SLASH, STAR, NUMERAL, IDENTIFIER);
	}

	/**
	 * Assert that the given tokens are returned by the lexer.
	 *
	 * @param input The input string.
	 * @param expected The expected tokens, not including the EOF token.
	 * @implNote  The EOF token is implicitly added to the end of {@code expected}.
	 */
	void assertTokens(String input, TokenType... expected) {
		List<Token> tokens = Lexer.tokenize(input);

		assertEquals(expected.length + 1, tokens.size());

		for (int i = 0; i < expected.length; i++) {
			// check if the token type matches
			Token token = tokens.get(i);
			assertEquals(expected[i], token.tokenType());

			// check if the lexeme has no whitespace
			String currentLexeme = token.lexeme();
			assertEquals(currentLexeme.trim(), currentLexeme);
		}

		// check the EOF token
		assertEquals(EOF, tokens.get(expected.length).tokenType());
	}
}
