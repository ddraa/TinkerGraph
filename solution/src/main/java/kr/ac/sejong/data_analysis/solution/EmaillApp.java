package kr.ac.sejong.data_analysis.solution;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class EmaillApp {

	
	public static void main(String[] args) throws IOException {
		BufferedReader r = new BufferedReader(
				new FileReader("/Users/dragon/data.txt"));
		
		while(true) {
			String line = r.readLine();
			if (line == null){
				break;
			}
			if (line.startsWith("#")) {
				continue;
			}
			
			String[] arr = line.split("\t");
			int left = Integer.parseInt(arr[0]);
			int right = Integer.parseInt(arr[1]);

			System.out.println(left + " -> " + right);
		}
		r.close();
	}

}
