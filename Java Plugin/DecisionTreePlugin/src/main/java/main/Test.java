package main;

import java.io.IOException;
import java.util.ArrayList;

import input.ProcessInputData;

public class Test {
	public static void main(String[] args) throws IOException {
		ProcessInputData trainDataset = new ProcessInputData("data/train.csv", "DEATH_EVENT");
		System.out.println(trainDataset.getAttributeSet());
		
		
		//ArrayList<String> trainList = ProcessInputData.CustomListFromCSV("data/computer_buy.csv");
		//ProcessInputData trainDataset = new ProcessInputData(trainList, "buys_computer");
		//System.out.println(trainDataset.getAttributeSet());
		
	}

}
