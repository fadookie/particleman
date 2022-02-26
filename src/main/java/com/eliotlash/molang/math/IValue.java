package com.eliotlash.molang.math;

import com.eliotlash.molang.variables.ExecutionContext;

/**
 * Math value interface
 * <p>
 * This interface provides only one method which is used by all
 * mathematical related classes. The point of this interface is to
 * provide generalized abstract method for computing/fetching some value
 * from different mathematical classes.
 */
public interface IValue {
	/**
	 * Get computed or stored value
	 * @param ctx
	 */
	double evaluate(ExecutionContext ctx);

	boolean isConstant();
}
