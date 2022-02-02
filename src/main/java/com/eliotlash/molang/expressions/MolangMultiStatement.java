package com.eliotlash.molang.expressions;

import java.util.*;

import com.eliotlash.molang.math.Variable;

public class MolangMultiStatement extends MolangExpression {
	public List<MolangExpression> expressions = new ArrayList<>();
	public Map<String, Variable> locals = new HashMap<>();

	@Override
	public double get() {
		double value = 0;

		for (MolangExpression expression : this.expressions) {
			value = expression.get();
		}

		return value;
	}

	@Override
	public String toString() {
		StringJoiner builder = new StringJoiner("; ");

		for (MolangExpression expression : this.expressions) {
			builder.add(expression.toString());

			if (expression instanceof ReturnStatement) {
				break;
			}
		}

		return builder.toString();
	}
}
