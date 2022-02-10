package com.eliotlash.molang.math;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Basic binary operators common in programming languages.
 *
 * <p>
 *     Includes both math and boolean operators. Boolean operators still operate on doubles.
 * </p>
 */
public enum Operator implements BinaryOperator {
	ADD("+", 1) {
		@Override
		public double apply(double a, double b) {
			return a + b;
		}
	},
	SUB("-", 1) {
		@Override
		public double apply(double a, double b) {
			return a - b;
		}
	},
	MUL("*", 2) {
		@Override
		public double apply(double a, double b) {
			return a * b;
		}
	},
	DIV("/", 2) {
		@Override
		public double apply(double a, double b) {
			/* To avoid any exceptions */
			return a / (b == 0 ? 1 : b);
		}
	},
	MOD("%", 2) {
		@Override
		public double apply(double a, double b) {
			return a % b;
		}
	},
	POW("^", 3) {
		@Override
		public double apply(double a, double b) {
			return Math.pow(a, b);
		}
	},
	AND("&&", 5) {
		@Override
		public double apply(double a, double b) {
			return a != 0 && b != 0 ? 1 : 0;
		}
	},
	OR("||", 5) {
		@Override
		public double apply(double a, double b) {
			return a != 0 || b != 0 ? 1 : 0;
		}
	},
	LESS("<", 5) {
		@Override
		public double apply(double a, double b) {
			return a < b ? 1 : 0;
		}
	},
	LESS_THAN("<=", 5) {
		@Override
		public double apply(double a, double b) {
			return a <= b ? 1 : 0;
		}
	},
	GREATER_THAN(">=", 5) {
		@Override
		public double apply(double a, double b) {
			return a >= b ? 1 : 0;
		}
	},
	GREATER(">", 5) {
		@Override
		public double apply(double a, double b) {
			return a > b ? 1 : 0;
		}
	},
	EQUALS("==", 5) {
		@Override
		public double apply(double a, double b) {
			return equals(a, b) ? 1 : 0;
		}
	},
	NOT_EQUALS("!=", 5) {
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

	/**
	 * Value of this operation in relation to other operations (i.e
	 * precedence importance)
	 */
	public final int precedence;

	Operator(String sign, int precedence) {
		this.sign = sign;
		this.precedence = precedence;
	}

	public static Operator fromString(String op) {
		return LOOKUP.get(op);
	}

	@Override
	public String format(String a, String b) {
		return a + " " + sign + " " + b;
	}
}
