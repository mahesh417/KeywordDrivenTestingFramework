package com.qualitestgroup.util.iter.arrays;

public class ArrayIterableFactory {
	
	public static ArrayIterable<Integer> create(int[] arr) {
		Integer[] temp = new Integer[arr.length];
		for(int i = 0; i < arr.length; i++) {
			temp[i] = Integer.valueOf(arr[i]);
		}
		return new ArrayIterable<Integer>(temp);
	}
	
	public static ArrayIterable<Float> create(float[] arr) {
		Float[] temp = new Float[arr.length];
		for(int i = 0; i < arr.length; i++) {
			temp[i] = Float.valueOf(arr[i]);
		}
		return new ArrayIterable<Float>(temp);
	}
	
	public static ArrayIterable<Long> create(long[] arr) {
		Long[] temp = new Long[arr.length];
		for(int i = 0; i < arr.length; i++) {
			temp[i] = Long.valueOf(arr[i]);
		}
		return new ArrayIterable<Long>(temp);
	}
	
	public static ArrayIterable<Boolean> create(boolean[] arr) {
		Boolean[] temp = new Boolean[arr.length];
		for(int i = 0; i < arr.length; i++) {
			temp[i] = Boolean.valueOf(arr[i]);
		}
		return new ArrayIterable<Boolean>(temp);
	}
	
	public static ArrayIterable<Double> create(double[] arr) {
		Double[] temp = new Double[arr.length];
		for(int i = 0; i < arr.length; i++) {
			temp[i] = Double.valueOf(arr[i]);
		}
		return new ArrayIterable<Double>(temp);
	}
	
	public static ArrayIterable<Byte> create(byte[] arr) {
		Byte[] temp = new Byte[arr.length];
		for(int i = 0; i < arr.length; i++) {
			temp[i] = Byte.valueOf(arr[i]);
		}
		return new ArrayIterable<Byte>(temp);
	}
	
	public static ArrayIterable<Short> create(short[] arr) {
		Short[] temp = new Short[arr.length];
		for(int i = 0; i < arr.length; i++) {
			temp[i] = Short.valueOf(arr[i]);
		}
		return new ArrayIterable<Short>(temp);
	}
	
	public static ArrayIterable<Character> create(char[] arr) {
		Character[] temp = new Character[arr.length];
		for(int i = 0; i < arr.length; i++) {
			temp[i] = Character.valueOf(arr[i]);
		}
		return new ArrayIterable<Character>(temp);
	}
	
	public static <T> ArrayIterable<T> create(T[] arr) {
		return new ArrayIterable<T>(arr);
	}
}
