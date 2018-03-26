package com.SpiltFile;

public class IntToDouble {
	public static double[] IntToDouble(int[] x) {
		double[] arr = new double[x.length];
		for(int i=0;i<x.length;i++) {
			arr[i] = (double) x[i];
		}
		return arr;
	}
}
