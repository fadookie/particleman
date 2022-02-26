package com.eliotlash.molang.variables;

/**
 * Represents the flavor of a variable.
 *
 * <p>
 *     The flavor of a variable is tied the access pattern it is used in.
 * </p>
 */
public enum VariableFlavor {
	/**
	 * Read-only storage provided by the game in certain scenarios.
	 */
	CONTEXT("context", "c"),
	/**
	 * Access to an entities properties.
	 */
	QUERY("query", "q"),
	/**
	 * Read/write temporary storage.
	 */
	TEMP("temp", "t"),
	/**
	 * Read/write storage on an actor.
	 */
	VARIABLE("variable", "v");

	private final String name;
	private final String alias;

	VariableFlavor(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}

	public static VariableFlavor parse(String name) {
		for (VariableFlavor flavor : values()) {
			if (flavor.name.equals(name) || flavor.alias.equals(name)) {
				return flavor;
			}
		}
		return null;
	}
}
