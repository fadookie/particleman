package com.eliotlash.molang;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.eliotlash.molang.expressions.MolangAssignment;
import com.eliotlash.molang.expressions.MolangExpression;
import com.eliotlash.molang.expressions.MolangMultiStatement;
import com.eliotlash.molang.expressions.MolangValue;
import com.eliotlash.molang.functions.CosDegrees;
import com.eliotlash.molang.functions.SinDegrees;
import com.eliotlash.mclib.math.Constant;
import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.MathBuilder;
import com.eliotlash.mclib.math.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * MoLang parser
 *
 * This bad boy parses Molang expressions
 *
 * @link https://bedrock.dev/1.14.0.0/1.14.2.50/MoLang
 */
public class MolangParser extends MathBuilder
{
	public static final MolangExpression ZERO = new MolangValue(null, new Constant(0));
	public static final MolangExpression ONE = new MolangValue(null, new Constant(1));
	public static final String RETURN = "return ";

	private MolangMultiStatement currentStatement;

	public MolangParser()
	{
		super();

		/* Replace radian based sin and cos with degreebased */
		this.functions.put("cos", CosDegrees.class);
		this.functions.put("sin", SinDegrees.class);

		/* Remap functions to be in tact with Molang specification */
		this.remap("abs", "math.abs");
		this.remap("ceil", "math.ceil");
		this.remap("clamp", "math.clamp");
		this.remap("cos", "math.cos");
		this.remap("exp", "math.exp");
		this.remap("floor", "math.floor");
		this.remap("lerp", "math.lerp");
		this.remap("lerprotate", "math.lerprotate");
		this.remap("ln", "math.ln");
		this.remap("max", "math.max");
		this.remap("min", "math.min");
		this.remap("mod", "math.mod");
		this.remap("pow", "math.pow");
		this.remap("random", "math.random");
		this.remap("round", "math.round");
		this.remap("sin", "math.sin");
		this.remap("sqrt", "math.sqrt");
		this.remap("trunc", "math.trunc");
	}

	/**
	 * Remap function names
	 */
	public void remap(String old, String newName)
	{
		this.functions.put(newName, this.functions.remove(old));
	}

	public void setValue(String name, double value)
	{
		Variable variable = this.getVariable(name);

		if (variable != null)
		{
			variable.set(value);
		}
	}

	/**
	 * Interactively return a new variable
	 */
	@Override
	protected Variable getVariable(String name)
	{
		Variable variable = this.currentStatement == null ? null : this.currentStatement.locals.get(name);

		if (variable == null)
		{
			variable = super.getVariable(name);
		}

		if (variable == null)
		{
			variable = new Variable(name, 0);

			this.register(variable);
		}

		return variable;
	}

	public MolangExpression parseJson(JsonElement element) throws MolangException
	{
		if (element.isJsonPrimitive())
		{
			JsonPrimitive primitive = element.getAsJsonPrimitive();

			if (primitive.isString())
			{
				try
				{
					return new MolangValue(this, new Constant(Float.parseFloat(primitive.getAsString())));
				}
				catch (Exception e)
				{}

				return this.parseExpression(primitive.getAsString());
			}
			else
			{
				return new MolangValue(this, new Constant(primitive.getAsDouble()));
			}
		}

		return ZERO;
	}

	/**
	 * Parse a molang expression
	 */
	public MolangExpression parseExpression(String expression) throws MolangException
	{
		List<String> lines = new ArrayList<String>();

		for (String split : expression.toLowerCase().trim().split(";"))
		{
			if (!split.trim().isEmpty())
			{
				lines.add(split);
			}
		}

		if (lines.size() == 0)
		{
			throw new MolangException("Molang expression cannot be blank!");
		}

		MolangMultiStatement result = new MolangMultiStatement(this);

		this.currentStatement = result;

		try
		{
			for (String line : lines)
			{
				result.expressions.add(this.parseOneLine(line));
			}
		}
		catch (Exception e)
		{
			this.currentStatement = null;

			throw e;
		}

		this.currentStatement = null;

		return result;
	}

	/**
	 * Parse a single Molang statement
	 */
	protected MolangExpression parseOneLine(String expression) throws MolangException
	{
		expression = expression.trim();

		if (expression.startsWith(RETURN))
		{
			try
			{
				return new MolangValue(this, this.parse(expression.substring(RETURN.length()))).addReturn();
			}
			catch (Exception e)
			{
				throw new MolangException("Couldn't parse return '" + expression + "' expression!");
			}
		}

		try
		{
			List<Object> symbols = this.breakdownChars(this.breakdown(expression));

			/* Assignment it is */
			if (symbols.size() >= 3 && symbols.get(0) instanceof String && this.isVariable(symbols.get(0)) && symbols.get(1).equals("="))
			{
				String name = (String) symbols.get(0);
				symbols = symbols.subList(2, symbols.size());

				Variable variable = null;

				if (!this.variables.containsKey(name) && !this.currentStatement.locals.containsKey(name))
				{
					variable = new Variable(name, 0);
					this.currentStatement.locals.put(name, variable);
				}
				else
				{
					variable = this.getVariable(name);
				}

				return new MolangAssignment(this, variable, this.parseSymbolsMolang(symbols));
			}

			return new MolangValue(this, this.parseSymbolsMolang(symbols));
		}
		catch (Exception e)
		{
			throw new MolangException("Couldn't parse '" + expression + "' expression!");
		}
	}

	/**
	 * Wrapper around {@link #parseSymbols(List)} to throw {@link MolangException}
	 */
	private IValue parseSymbolsMolang(List<Object> symbols) throws MolangException
	{
		try
		{
			return this.parseSymbols(symbols);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new MolangException("Couldn't parse an expression!");
		}
	}

	/**
	 * Extend this method to allow {@link #breakdownChars(String[])} to capture
	 * "=" as an operator so it was easier to parse assignment statements
	 */
	@Override
	protected boolean isOperator(String s)
	{
		return super.isOperator(s) || s.equals("=");
	}
}
