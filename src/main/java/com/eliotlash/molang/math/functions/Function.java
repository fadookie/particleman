package com.eliotlash.molang.math.functions;

import java.util.Arrays;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.math.IValue;

/**
 * Abstract function class
 * <p>
 * This class provides function capability (i.e. giving it arguments and
 * upon {@link IValue#evaluate(ExecutionContext)} method you receive output).
 */
public abstract class Function implements IValue {
	protected IValue[] arguments;
	protected String name;

	public Function(IValue[] arguments, String name) throws Exception {
		if (arguments.length < this.getRequiredArguments()) {
			String message = String.format("Function '%s' requires at least %s arguments. %s are given!", name, this.getRequiredArguments(), arguments.length);

			throw new Exception(message);
		}

		this.arguments = arguments;
		this.name = name;
	}

	/**
	 * Get the value of nth argument
	 */
	public double evaluateArgument(ExecutionContext ctx, int index) {
		if (index < 0 || index >= this.arguments.length) {
			return 0;
		}

		return this.arguments[index].evaluate(ctx);
	}

	@Override
	public String toString() {
		StringBuilder args = new StringBuilder();

		for (int i = 0; i < this.arguments.length; i++) {
			args.append(this.arguments[i].toString());

			if (i < this.arguments.length - 1) {
				args.append(", ");
			}
		}

		return this.getName() + "(" + args + ")";
	}

	/**
	 * Get name of this function
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get minimum count of arguments this function needs
	 */
	public int getRequiredArguments() {
		return 0;
	}

	@Override
	public boolean isConstant() {
		return Arrays.stream(arguments).allMatch(IValue::isConstant);
	}
}
