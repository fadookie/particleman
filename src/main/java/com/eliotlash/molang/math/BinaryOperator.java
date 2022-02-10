package com.eliotlash.molang.math;

/**
 * An arbitrary binary operator.
 *
 * TODO: operator registry
 */
public interface BinaryOperator {
	/**
	 * Applies this operator to the given operands.
	 * @param a The first, or left, operand.
	 * @param b The second, or right, operand.
	 * @return The result of applying this operator to the given operands.
	 */
	double apply(double a, double b);

	/**
	 * Formats this operator as a string, given string representations of the operands.
	 * @param a The string representation of the first, or left, operand.
	 * @param b The string representation of the second, or right, operand.
	 * @return The string representation of this operator applied to the given operands.
	 */
	String format(String a, String b);
}
