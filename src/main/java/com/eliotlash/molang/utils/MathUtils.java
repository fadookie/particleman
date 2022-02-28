package com.eliotlash.molang.utils;

public class MathUtils {
	public static int clamp(int x, int min, int max) {
		return Math.min(Math.max(x, min), max);
	}

	public static float clamp(float x, float min, float max) {
		return Math.min(Math.max(x, min), max);
	}

	public static double clamp(double x, double min, double max) {
		return Math.min(Math.max(x, min), max);
	}

	public static int cycler(int x, int min, int max) {
		return x < min ? max : (x > max ? min : x);
	}

	public static float cycler(float x, float min, float max) {
		return x < min ? max : (x > max ? min : x);
	}

	public static double cycler(double x, double min, double max) {
		return x < min ? max : (x > max ? min : x);
	}

	/**
	 * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
	 */
	public static float wrapDegrees(float value) {
		value = value % 360.0F;

		if (value >= 180.0F) {
			value -= 360.0F;
		}

		if (value < -180.0F) {
			value += 360.0F;
		}

		return value;
	}

	/**
	 * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
	 */
	public static double wrapDegrees(double value) {
		value = value % 360.0D;

		if (value >= 180.0D) {
			value -= 360.0D;
		}

		if (value < -180.0D) {
			value += 360.0D;
		}

		return value;
	}

	/**
	 * Adjust the angle so that his value is in range [-180;180[
	 */
	public static int wrapDegrees(int angle) {
		angle = angle % 360;

		if (angle >= 180) {
			angle -= 360;
		}

		if (angle < -180) {
			angle += 360;
		}

		return angle;
	}

	public static boolean epsilonEquals(double a, double b) {
		return epsilonEquals(a, b, 0.00001);
	}

	public static boolean epsilonEquals(double a , double b, double epsilon) {
		return Math.abs(a - b) < epsilon;
	}
}
