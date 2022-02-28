package com.eliotlash.molang.functions;

import com.eliotlash.molang.variables.ExecutionContext;
import com.eliotlash.molang.ast.Expr;

public abstract class Function {
	protected Expr[] arguments;
	protected String name;

	public Function(Expr[] arguments, String name) throws Exception {
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

		return 0;//this.arguments[index].evaluate(ctx);
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

	public abstract double evaluate(ExecutionContext ctx);
}
