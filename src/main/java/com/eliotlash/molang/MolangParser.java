package com.eliotlash.molang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.eliotlash.molang.math.*;
import com.eliotlash.molang.expressions.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * MoLang parser
 * <p>
 * This bad boy parses Molang expressions
 *
 * @link https://bedrock.dev/1.14.0.0/1.14.2.50/MoLang
 */
public class MolangParser {
	public static final MolangExpression ZERO = new MolangValue(new Constant(0));
	public static final MolangExpression ONE = new MolangValue(new Constant(1));
	public static final String RETURN = "return ";
	private static final Pattern DECIMAL = Pattern.compile("^-?\\d+(\\.\\d+)?$");
	private static final Pattern SEQUENTIAL_ALLOWED_CHARACTERS = Pattern.compile("^[\\w\\d\\s_+-/*%^&|<>=!?:.,()]+$");
	public final BuiltinFunctions functions;
	public final CompileConstants constants;
	/**
	 * Named variables that can be used in math expression by this
	 * builder
	 */
	public Map<String, Variable> variables = new HashMap<>();

	private MolangMultiStatement currentStatement;

	public MolangParser() {

		this.functions = new BuiltinFunctions();
		this.constants = new CompileConstants();
	}

	public void setValue(String name, double value) {
		this.getOrCreateVariable(name).set(value);
	}

	protected Variable getOrCreateVariable(String name) {
		Variable variable = getVariable(name);

		return variable == null ? createVariable(name) : variable;
	}

	public Variable createVariable(String name) {
		Variable variable = new Variable(name, 0);

		this.variables.put(variable.getName(), variable);
		return variable;
	}

	private Variable getVariable(String name) {
		Variable variable = this.currentStatement == null ? null : this.currentStatement.locals.get(name);

		if (variable == null) {
			variable = this.variables.get(name);
		}
		return variable;
	}

	/**
	 * Get either a variable or a compile-time constant.
	 * @param name Name of the variable or constant.
	 * @return Value of the variable or constant.
	 */
	protected IValue getVariableOrConstantOrCreateVariable(String name) {
		IValue out = getVariable(name);

		if (out != null)
			return out;

		out = this.constants.get(name);

		if (out != null)
			return out;

		return createVariable(name);
	}

	public MolangExpression parseJson(JsonElement element) throws MolangException {
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();

			if (primitive.isString()) {
				try {
					return new MolangValue(new Constant(Float.parseFloat(primitive.getAsString())));
				} catch (Exception ignored) {
				}

				return this.parseExpression(primitive.getAsString());
			} else {
				return new MolangValue(new Constant(primitive.getAsDouble()));
			}
		}

		return ZERO;
	}

	/**
	 * Parse a molang expression
	 */
	public MolangExpression parseExpression(String expression) throws MolangException {
		List<String> lines = new ArrayList<>();

		for (String split : expression.toLowerCase().trim().split(";")) {
			if (!split.trim().isEmpty()) {
				lines.add(split);
			}
		}

		if (lines.size() == 0) {
			throw new MolangException("Molang expression cannot be blank!");
		}

		MolangMultiStatement result = new MolangMultiStatement();

		this.currentStatement = result;

		try {
			for (String line : lines) {
				result.expressions.add(this.parseOneLine(line));
			}
		} catch (Exception e) {
			this.currentStatement = null;

			throw e;
		}

		this.currentStatement = null;

		return result;
	}

	/**
	 * Parse a single Molang statement
	 */
	protected MolangExpression parseOneLine(String expression) throws MolangException {
		expression = expression.trim();

		if (expression.startsWith(RETURN)) {
			try {
				return new ReturnStatement(this.parse(expression.substring(RETURN.length())));
			} catch (Exception e) {
				throw new MolangException("Couldn't parse return '" + expression + "' expression!");
			}
		}

		try {
			List<Object> symbols = this.recursiveTokenize(this.breakdown(expression));

			/* Assignment it is */
			if (symbols.size() >= 3 && symbols.get(0) instanceof String name && this.isVariable(name) && symbols.get(1)
					.equals("=")) {
				symbols = symbols.subList(2, symbols.size());

				Variable variable = null;

				if (!this.variables.containsKey(name) && !this.currentStatement.locals.containsKey(name)) {
					variable = new Variable(name, 0);
					this.currentStatement.locals.put(name, variable);
				} else {
					variable = this.getOrCreateVariable(name);
				}

				return new MolangAssignment(variable, this.parseSymbolsMolang(symbols));
			}

			return new MolangValue(this.parseSymbolsMolang(symbols));
		} catch (Exception e) {
			throw new MolangException("Couldn't parse '" + expression + "' expression!");
		}
	}

	/**
	 * Parse symbols
	 * <p>
	 * This function is the most important part of this class. It's
	 * responsible for turning list of symbols into {@link IValue}. This
	 * is done by constructing a binary tree-like {@link IValue} based on
	 * {@link BinaryOperation} class.
	 * <p>
	 * However, beside parsing operations, it's also can return one or
	 * two item sized symbol lists.
	 */
	private IValue parseSymbolsMolang(List<Object> symbols) throws MolangException {
		try {
			return this.parseSymbols(symbols);
		} catch (Exception e) {
			e.printStackTrace();

			throw new MolangException("Couldn't parse an expression!");
		}
	}

	/**
		 * Whether string is an operator
		 */
	protected boolean isOperator(String s) {
		return Operator.isOperator(s) || s.equals("?") || s.equals(":") || s.equals("=");
	}

	/**
	 * Parse given math expression into a {@link IValue} which can be
	 * used to execute math.
	 */
	public IValue parse(String expression) throws Exception {
		return this.parseSymbols(this.recursiveTokenize(this.breakdown(expression)));
	}

	/**
	 * Breakdown an expression
	 */
	public String[] breakdown(String expression) throws Exception {
		/* If given string have illegal characters, then it can't be parsed */
		if (!containsOnlyAllowedCharacters(expression)) {
			throw new Exception("Given expression '" + expression + "' contains illegal characters!");
		}

		/* Remove all spaces, and leading and trailing parenthesis */
		expression = expression.replaceAll("\\s+", "");

		String[] chars = expression.split("(?!^)");

		int left = 0;
		int right = 0;

		for (String s : chars) {
			if (s.equals("(")) {
				left++;
			} else if (s.equals(")")) {
				right++;
			}
		}

		/* Amount of left and right brackets should be the same */
		if (left != right) {
			throw new Exception("Given expression '" + expression + "' has more uneven amount of parenthesis, there are " + left + " open and " + right + " closed!");
		}

		return chars;
	}

	/**
	 * Breakdown characters into a list of math expression symbols.
	 */
	public List<Object> recursiveTokenize(String[] chars) {
		List<Object> symbols = new ArrayList<>();
		StringBuilder buffer = new StringBuilder();
		int len = chars.length;

		for (int i = 0; i < len; i++) {
			String s = chars[i];
			boolean longOperator = i > 0 && this.isOperator(chars[i - 1] + s);

			if (this.isOperator(s) || longOperator || s.equals(",")) {
				/* Taking care of a special case of using minus sign to
				 * invert the positive value */
				if (s.equals("-")) {
					int size = symbols.size();

					boolean isFirst = size == 0 && (buffer.length() == 0);
					boolean isOperatorBehind = size > 0 && (this.isOperator(symbols.get(size - 1)) || symbols.get(size - 1)
							.equals(",")) && (buffer.length() == 0);

					if (isFirst || isOperatorBehind) {
						buffer.append(s);

						continue;
					}
				}

				if (longOperator) {
					s = chars[i - 1] + s;
					buffer = new StringBuilder(buffer.substring(0, buffer.length() - 1));
				}

				/* Push buffer and operator */
				if (buffer.length() > 0) {
					symbols.add(buffer.toString());
					buffer = new StringBuilder();
				}

				symbols.add(s);
			} else if (s.equals("(")) {
				/* Push a list of symbols */
				if (buffer.length() > 0) {
					symbols.add(buffer.toString());
					buffer = new StringBuilder();
				}

				int counter = 1;

				for (int j = i + 1; j < len; j++) {
					String c = chars[j];

					if (c.equals("(")) {
						counter++;
					} else if (c.equals(")")) {
						counter--;
					}

					if (counter == 0) {
						symbols.add(this.recursiveTokenize(buffer.toString().split("(?!^)")));

						i = j;
						buffer = new StringBuilder();

						break;
					} else {
						buffer.append(c);
					}
				}
			} else {
				/* Accumulate the buffer */
				buffer.append(s);
			}
		}

		if (buffer.length() > 0) {
			symbols.add(buffer.toString());
		}

		return symbols;
	}

	/**
	 * Parse symbols
	 * <p>
	 * This function is the most important part of this class. It's
	 * responsible for turning list of symbols into {@link IValue}. This
	 * is done by constructing a binary tree-like {@link IValue} based on
	 * {@link BinaryOperation} class.
	 * <p>
	 * However, beside parsing operations, it's also can return one or
	 * two item sized symbol lists.
	 */
	@SuppressWarnings("unchecked")
	public IValue parseSymbols(List<Object> symbols) throws Exception {
		IValue ternary = this.tryTernary(symbols);

		if (ternary != null) {
			return ternary;
		}

		int size = symbols.size();

		/* Constant, variable or group (parenthesis) */
		if (size == 1) {
			return this.valueFromObject(symbols.get(0));
		}

		/* Function */
		if (size == 2) {
			Object first = symbols.get(0);
			Object second = symbols.get(1);

			if ((this.isVariable(first) || first.equals("-")) && second instanceof List) {
				return this.createFunction((String) first, (List<Object>) second);
			}
		}

		/* Any other math expression */
		int lastOp = this.seekLastOperator(symbols);
		int op = lastOp;

		while (op != -1) {
			int leftOp = this.seekLastOperator(symbols, op - 1);

			if (leftOp != -1) {
				Operator left = this.operationForOperator((String) symbols.get(leftOp));
				Operator right = this.operationForOperator((String) symbols.get(op));

				if (right.precedence > left.precedence) {
					IValue leftValue = this.parseSymbols(symbols.subList(0, leftOp));
					IValue rightValue = this.parseSymbols(symbols.subList(leftOp + 1, size));

					return new BinaryOperation(left, leftValue, rightValue);
				} else if (left.precedence > right.precedence) {
					Operator initial = this.operationForOperator((String) symbols.get(lastOp));

					if (initial.precedence < left.precedence) {
						IValue leftValue = this.parseSymbols(symbols.subList(0, lastOp));
						IValue rightValue = this.parseSymbols(symbols.subList(lastOp + 1, size));

						return new BinaryOperation(initial, leftValue, rightValue);
					}

					IValue leftValue = this.parseSymbols(symbols.subList(0, op));
					IValue rightValue = this.parseSymbols(symbols.subList(op + 1, size));

					return new BinaryOperation(right, leftValue, rightValue);
				}
			}

			op = leftOp;
		}

		Operator operator = this.operationForOperator((String) symbols.get(lastOp));

		return new BinaryOperation(operator, this.parseSymbols(symbols.subList(0, lastOp)), this.parseSymbols(symbols.subList(lastOp + 1, size)));
	}

	protected int seekLastOperator(List<Object> symbols) {
		return this.seekLastOperator(symbols, symbols.size() - 1);
	}

	/**
	 * Find the index of the first operator
	 */
	protected int seekLastOperator(List<Object> symbols, int offset) {
		for (int i = offset; i >= 0; i--) {
			Object o = symbols.get(i);

			if (this.isOperator(o)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Try parsing a ternary expression
	 * <p>
	 * From what we know, with ternary expressions, we should have only one ? and :,
	 * and some elements from beginning till ?, in between ? and :, and also some
	 * remaining elements after :.
	 */
	protected IValue tryTernary(List<Object> symbols) throws Exception {
		int question = -1;
		int questions = 0;
		int colon = -1;
		int colons = 0;
		int size = symbols.size();

		for (int i = 0; i < size; i++) {
			Object object = symbols.get(i);

			if (object instanceof String) {
				if (object.equals("?")) {
					if (question == -1) {
						question = i;
					}

					questions++;
				} else if (object.equals(":")) {
					if (colons + 1 == questions && colon == -1) {
						colon = i;
					}

					colons++;
				}
			}
		}

		if (questions == colons && question > 0 && question + 1 < colon && colon < size - 1) {
			return new Ternary(this.parseSymbols(symbols.subList(0, question)), this.parseSymbols(symbols.subList(question + 1, colon)), this.parseSymbols(symbols.subList(colon + 1, size)));
		}

		return null;
	}

	/**
	 * Create a function value
	 * <p>
	 * This method in comparison to {@link #valueFromObject(Object)}
	 * needs the name of the function and list of args (which can't be
	 * stored in one object).
	 * <p>
	 * This method will constructs {@link IValue}s from list of args
	 * mixed with operators, groups, values and commas. And then plug it
	 * in to a class constructor with given name.
	 */
	protected IValue createFunction(String first, List<Object> args) throws Exception {
		/* Handle special cases with negation */
		if (first.equals("!")) {
			return new Negate(this.parseSymbols(args));
		}

		if (first.startsWith("!") && first.length() > 1) {
			return new Negate(this.createFunction(first.substring(1), args));
		}

		/* Handle inversion of the value */
		if (first.equals("-")) {
			return new Negative(this.parseSymbols(args));
		}

		if (first.startsWith("-") && first.length() > 1) {
			return new Negative(this.createFunction(first.substring(1), args));
		}

		if (!this.functions.isFunction(first)) {
			throw new Exception("Function '" + first + "' couldn't be found!");
		}

		List<IValue> values = new ArrayList<>();
		List<Object> buffer = new ArrayList<>();

		for (Object o : args) {
			if (o.equals(",")) {
				values.add(this.parseSymbols(buffer));
				buffer.clear();
			} else {
				buffer.add(o);
			}
		}

		if (!buffer.isEmpty()) {
			values.add(this.parseSymbols(buffer));
		}

		return this.functions.get(first).create(values.toArray(new IValue[0]), first);
	}

	/**
	 * Get value from an object.
	 * <p>
	 * This method is responsible for creating different sort of values
	 * based on the input object. It can create constants, variables and
	 * groups.
	 */
	@SuppressWarnings("unchecked")
	public IValue valueFromObject(Object object) throws Exception {
		if (object instanceof String symbol) {

			/* Variable and constant negation */
			if (symbol.startsWith("!")) {
				return new Negate(this.valueFromObject(symbol.substring(1)));
			}

			if (this.isDecimal(symbol)) {
				return new Constant(Double.parseDouble(symbol));
			} else if (this.isVariable(symbol)) {
				/* Need to account for a negative value variable */
				if (symbol.startsWith("-")) {
					symbol = symbol.substring(1);
					var value = this.getVariableOrConstantOrCreateVariable(symbol);

					if (value != null) {
						return new Negative(value);
					}
				} else {
					var value = this.getVariableOrConstantOrCreateVariable(symbol);

					/* Avoid NPE */
					if (value != null) {
						return value;
					}
				}
			}
		} else if (object instanceof List) {
			return new Group(this.parseSymbols((List<Object>) object));
		}

		throw new Exception("Given object couldn't be converted to value! " + object);
	}

	/**
	 * Get operation for given operator strings
	 */
	protected Operator operationForOperator(String op) throws Exception {
		Operator out = Operator.fromString(op);

		if (out == null)
			throw new Exception("There is no such operator '" + op + "'!");

		return out;
	}

	/**
	 * Whether given object is a variable
	 */
	protected boolean isVariable(Object o) {
		return o instanceof String s && !this.isDecimal(s) && !this.isOperator(s);
	}

	protected boolean isOperator(Object o) {
		return o instanceof String s && this.isOperator(s);
	}

	/**
	 * Whether string is numeric (including whether it's a floating
	 * number)
	 */
	protected boolean isDecimal(String s) {
		return DECIMAL.matcher(s).matches();
	}

	protected boolean containsOnlyAllowedCharacters(String expression) {
		return SEQUENTIAL_ALLOWED_CHARACTERS.matcher(expression).matches();
	}
}
