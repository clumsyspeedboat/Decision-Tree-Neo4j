package main;

/**
 * Main class to build the decision tree and test on the test dataset
 */

import java.io.IOException;
import java.util.Scanner;
import evaluate.EvaluateTree;
import output.PrintTree;

public class ClassifyMainIG {
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		String pathos = "data/train.csv,data/test.csv";

		String[] paths = pathos.split(",");
		
		EvaluateTree mine = new EvaluateTree(paths[0], paths[1], "DEATH_EVENT");
		
		//ArrayList<String> trainList = ProcessInputData.CustomListFromCSV("data/train.csv");
		//ArrayList<String> testList = ProcessInputData.CustomListFromCSV("data/test.csv");
		
		//EvaluateTree mine = new EvaluateTree(trainList, testList, "DEATH_EVENT");
	
		mine.calculateAccuracy();

	    PrintTree tree = new PrintTree();
	    
		//System.out.println(tree.printDFS(mine.getRoot()));
		 
		tree.createNodesForGraph(mine.getRoot());
		System.out.println(tree.nodesBucket);
		System.out.println(tree.relationshipsBucket);
		
		
		in.close();
	}
	
}