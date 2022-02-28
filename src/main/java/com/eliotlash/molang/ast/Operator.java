package com.eliotlash.molang.ast;

import java.util.function.DoubleSupplier;

import com.eliotlash.molang.Token;
import com.eliotlash.molang.utils.MathUtils;

/**
 * Basic binary operators common in programming languages.
 *
 * <p>
 *     Includes both math and boolean operators. Boolean operators still operate on doubles.
 * </p>
 */
public enum Operator {
	ADD("+"),
	SUB("-"),
	MUL("*"),
	DIV("/"),
	MOD("%"),
	POW("^"),
	AND("&&"),
	OR("||"),
	LT("<"),
	LEQ("<="),
	GEQ(">="),
	GT(">"),
	EQ("=="),
	NEQ("!="),
	;

	/**
	 * The symbol representing this operation.
	 */
	public final String sign;

	Operator(String sign) {
		this.sign = sign;
	}

	public static Operator from(Token operator) {
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
			case GREATER_THAN -> GT;
			case GREATER_EQUAL -> GEQ;
			case EQUAL_EQUAL -> EQ;
			case BANG_EQUAL -> NEQ;
			default -> null;
		};
	}

	public double apply(DoubleSupplier lhs, DoubleSupplier rhs) {
		return switch (this) {
			case ADD -> lhs.getAsDouble() + rhs.getAsDouble();
			case SUB -> lhs.getAsDouble() - rhs.getAsDouble();
			case MUL -> lhs.getAsDouble() * rhs.getAsDouble();
			case DIV -> lhs.getAsDouble() / (rhs.getAsDouble() == 0 ? 1 : rhs.getAsDouble());
			case MOD -> lhs.getAsDouble() % rhs.getAsDouble();
			case POW -> Math.pow(lhs.getAsDouble(), rhs.getAsDouble());
			case LT -> bool(lhs.getAsDouble() < rhs.getAsDouble());
			case LEQ -> bool(lhs.getAsDouble() <= rhs.getAsDouble());
			case GEQ -> bool(lhs.getAsDouble() >= rhs.getAsDouble());
			case GT -> bool(lhs.getAsDouble() > rhs.getAsDouble());
			case EQ -> bool(MathUtils.epsilonEquals(lhs.getAsDouble(), rhs.getAsDouble()));
			case NEQ -> bool(!MathUtils.epsilonEquals(lhs.getAsDouble(), rhs.getAsDouble()));
			// AND, OR should be lazily evaluated
			case AND -> bool(lhs.getAsDouble() != 0 && rhs.getAsDouble() != 0);
			case OR -> bool(lhs.getAsDouble() != 0 || rhs.getAsDouble() != 0);
		};
	}

	private static double bool(boolean b) {
		return b ? 1.0 : 0.0;
	}

	/**
	 * Formats this operator as a string, given string representations of the operands.
	 * @param a The string representation of the first, or left, operand.
	 * @param b The string representation of the second, or right, operand.
	 * @return The string representation of this operator applied to the given operands.
	 */
	public String format(String a, String b) {
		return a + " " + sign + " " + b;
	}

	@Override
	public String toString() {
		return sign;
	}
}
