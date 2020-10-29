package kr.ac.sejong.data_analysis.world;

import java.util.Scanner;

public class init {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int val = 10;
		int[] array = new int[val];
		for(int i=0; i<array.length; i++) {
			array[i] = i;
		}
		for(int i=0; i<array.length; i++)
			System.out.println(array[i]);
		
	}

}
