package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import evaluate.EvaluateTree;
import gainratio.EvaluateTreeGR;
import input.ProcessInputData;
import output.PrintTree;

public class ClassifyMainGR {
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
		String pathos = "data/meta_train.csv,data/meta_test.csv";
		
		String[] paths = pathos.split(",");
		
		//ArrayList<String> trainList = ProcessInputData.CustomListFromCSV("data/train.csv");
		//ArrayList<String> testList = ProcessInputData.CustomListFromCSV("data/test.csv");
				
	    //EvaluateTree mine = new EvaluateTree(trainList, testList, "DEATH_EVENT");
		
		EvaluateTreeGR mine = new EvaluateTreeGR(paths[0], paths[1], "PatientType");
		
	    mine.calculateAccuracy();

	    PrintTree tree = new PrintTree();
	    
		//System.out.println(tree.printDFS(mine.getRoot()));
		 
		tree.createNodesForGraph(mine.getRoot());

		in.close();
	}

}
