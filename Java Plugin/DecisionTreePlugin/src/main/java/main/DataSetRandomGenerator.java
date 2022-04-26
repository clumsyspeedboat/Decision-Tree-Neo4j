package main;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class DataSetRandomGenerator {
	public static ArrayList<String> randomDataSet =  new ArrayList<String>(); 
	
	public static ArrayList<String> RandomGenerateDataList(int Rows, int Columns)
	{
		ArrayList<String> generatedRandomDataList = new ArrayList<String>();
		for(int i=0; i < Rows; i++)
		{
			String generatedRandomInstance = "Columns1:" + Math.random();
			for(int j=1; j < Columns; j++)
			{
				generatedRandomInstance = generatedRandomInstance + "," + "Columns" + (j+1) + ":" + Math.random();
			}
			Random r = new Random();
			int max = 2;
			int min = 1;
			generatedRandomInstance = generatedRandomInstance + ",ClassLabel:" + (r.nextInt((max - min) + 1) + min);
			generatedRandomDataList.add(generatedRandomInstance);
		}
		for(String part:generatedRandomDataList)
		{
			System.out.println(part);
		}
		return generatedRandomDataList;
	}
	
	
	public static void main(String[] args) {
		RandomGenerateDataList(20,5);
	}
}
