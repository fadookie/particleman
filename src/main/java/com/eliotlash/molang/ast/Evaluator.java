package com.eliotlash.molang.ast;

import com.eliotlash.molang.ast.*;
import com.eliotlash.molang.utils.MathUtils;
import com.eliotlash.molang.ast.Visitor;

public class Evaluator implements Visitor<Double> {
	@Override
	public Double visitAccess(Expr.Access expr) {
		return null;
	}

	@Override
	public Double visitAssignment(Expr.Assignment expr) {
		var rhs = evaluate(expr.expression());

		// TODO: Do assignment
		return rhs;
	}

	@Override
	public Double visitBinaryOperation(Expr.BinOp expr) {

		return switch (expr.operator()) {
			case ADD -> evaluate(expr.left()) + evaluate(expr.right());
			case SUB -> evaluate(expr.left()) - evaluate(expr.right());
			case MUL -> evaluate(expr.left()) * evaluate(expr.right());
			case DIV -> evaluate(expr.left()) / (evaluate(expr.right()) == 0 ? 1 : evaluate(expr.right()));
			case MOD -> evaluate(expr.left()) % evaluate(expr.right());
			case POW -> Math.pow(evaluate(expr.left()), evaluate(expr.right()));
			case LT -> bool(evaluate(expr.left()) < evaluate(expr.right()));
			case LEQ -> bool(evaluate(expr.left()) <= evaluate(expr.right()));
			case GEQ -> bool(evaluate(expr.left()) >= evaluate(expr.right()));
			case GT -> bool(evaluate(expr.left()) > evaluate(expr.right()));
			case EQ -> bool(MathUtils.epsilonEquals(evaluate(expr.left()), evaluate(expr.right())));
			case NEQ -> bool(!MathUtils.epsilonEquals(evaluate(expr.left()), evaluate(expr.right())));
			// AND, OR should be lazily evaluated
			case AND -> bool(evaluate(expr.left()) != 0 && evaluate(expr.right()) != 0);
			case OR -> bool(evaluate(expr.left()) != 0 || evaluate(expr.right()) != 0);
		};
	}

	private static double bool(boolean b) {
		return b ? 1.0 : 0.0;
	}

	@Override
	public Double visitCall(Expr.Call expr) {

		return null;
	}

	@Override
	public Double visitConstant(Expr.Constant expr) {
		return expr.value();
	}

	@Override
	public Double visitGroup(Expr.Group expr) {
		return evaluate(expr.value());
	}

	@Override
	public Double visitNegate(Expr.Negate expr) {
		return -evaluate(expr.value());
	}

	@Override
	public Double visitNot(Expr.Not expr) {
		return evaluate(expr.value()) == 0 ? 1.0 : 0.0;
	}

	@Override
	public Double visitTernary(Expr.Ternary expr) {
		Expr branch = evaluate(expr.condition()) == 0 ? expr.ifFalse() : expr.ifTrue();
		return evaluate(branch);
	}

	@Override
	public Double visitVariable(Expr.Variable expr) {
		return null;
	}

	private Double evaluate(Expr expr) {
		return expr.accept(this);
	}
}
