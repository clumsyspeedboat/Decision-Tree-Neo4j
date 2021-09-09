package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import gini.EvaluateTreeGI;
import input.ProcessInputData;
import output.PrintTree;

public class ClassifyMainGI {
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
		//String pathos = "data/Flu_Classification_Training_Dataset.csv,data/Flu_Classification_Testing_Dataset.csv";
		String pathos = "data/flu_train.csv,data/flu_test.csv";
		
		String[] paths = pathos.split(",");
		
		EvaluateTreeGI mine = new EvaluateTreeGI(paths[0],paths[1],"Diagnosis");
		
		//ArrayList<String> trainList = ProcessInputData.CustomListFromCSV("data/meta_train.csv");
		//ArrayList<String> testList = ProcessInputData.CustomListFromCSV("data/meta_test.csv");
		
		//EvaluateTreeGI mine = new EvaluateTreeGI(trainList,testList,"PatientType");
	    
	    mine.calculateAccuracy();

	    PrintTree tree = new PrintTree();
	    
		System.out.println(tree.printDFS(mine.getRoot()));
		 
		tree.createNodesForGraph(mine.getRoot());
		

		in.close();
	}

}
