package com.eliotlash.molang.math;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.eliotlash.molang.Token;

/**
 * Basic binary operators common in programming languages.
 *
 * <p>
 *     Includes both math and boolean operators. Boolean operators still operate on doubles.
 * </p>
 */
public enum Operator implements BinaryOperator {
	ADD("+") {
		@Override
		public double apply(double a, double b) {
			return a + b;
		}
	},
	SUB("-") {
		@Override
		public double apply(double a, double b) {
			return a - b;
		}
	},
	MUL("*") {
		@Override
		public double apply(double a, double b) {
			return a * b;
		}
	},
	DIV("/") {
		@Override
		public double apply(double a, double b) {
			/* To avoid any exceptions */
			return a / (b == 0 ? 1 : b);
		}
	},
	MOD("%") {
		@Override
		public double apply(double a, double b) {
			return a % b;
		}
	},
	POW("^") {
		@Override
		public double apply(double a, double b) {
			return Math.pow(a, b);
		}
	},
	AND("&&") {
		@Override
		public double apply(double a, double b) {
			return a != 0 && b != 0 ? 1 : 0;
		}
	},
	OR("||") {
		@Override
		public double apply(double a, double b) {
			return a != 0 || b != 0 ? 1 : 0;
		}
	},
	LT("<") {
		@Override
		public double apply(double a, double b) {
			return a < b ? 1 : 0;
		}
	},
	LEQ("<=") {
		@Override
		public double apply(double a, double b) {
			return a <= b ? 1 : 0;
		}
	},
	GEQ(">=") {
		@Override
		public double apply(double a, double b) {
			return a >= b ? 1 : 0;
		}
	},
	GT(">") {
		@Override
		public double apply(double a, double b) {
			return a > b ? 1 : 0;
		}
	},
	EQ("==") {
		@Override
		public double apply(double a, double b) {
			return equals(a, b) ? 1 : 0;
		}
	},
	NEQ("!=") {
		@Override
		public double apply(double a, double b) {
			return !equals(a, b) ? 1 : 0;
		}
	};

	private static final Map<String, Operator> LOOKUP = new HashMap<>();
	private static final Pattern OPERATORS;

	public static boolean equals(double a, double b) {
		return Math.abs(a - b) < 0.00001;
	}

	public static boolean isOperator(String s) {
		return OPERATORS.matcher(s).matches();
	}

	static {
		StringBuilder regex = new StringBuilder();
		for (Operator op : values()) {
			LOOKUP.put(op.sign, op);
			regex.append(Pattern.quote(op.sign)).append("|");
		}
		regex = new StringBuilder(regex.substring(0, regex.length() - 1));
		OPERATORS = Pattern.compile(regex.toString());
	}

	/**
	 * String-ified name of this operation
	 */
	public final String sign;

	Operator(String sign) {
		this.sign = sign;
	}

	public static Operator fromString(String op) {
		return LOOKUP.get(op);
	}

	public static BinaryOperator from(Token operator) {
		return switch (operator.tokenType()) {
			case PLUS -> ADD;
			case MINUS -> SUB;
			case STAR -> MUL;
			case SLASH -> DIV;
			case PERCENT -> MOD;
			case CARET -> POW;
			case AND -> AND;
			case OR -> OR;
			case LESS_THAN -> LT;
			case LESS_EQUAL -> LEQ;
			case GREATER_THAN -> GEQ;
			case GREATER_EQUAL -> GT;
			case EQUAL_EQUAL -> EQ;
			case BANG_EQUAL -> NEQ;
			default -> null;
		};
	}

	@Override
	public String format(String a, String b) {
		return a + " " + sign + " " + b;
	}
}
