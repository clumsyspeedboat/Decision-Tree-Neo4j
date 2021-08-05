package main;

/**
 * Main class to build the decision tree and test on the test dataset
 */

import java.io.IOException;
import java.util.Scanner;

import evaluate.C45MineData;
import output.PrintTree;

public class ClassifyMainIG {
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		

		String pathos = "data/Flu_Classification_Training_Dataset.csv,data/Flu_Classification_Testing_Dataset.csv";

		
		String[] paths = pathos.split(",");
		
		
		C45MineData mine = new C45MineData(paths[0], paths[1]);
		
		
	    mine.calculateAccuracy();

	    
	    PrintTree tree = new PrintTree();
	    
		//System.out.println(tree.printDFS(mine.getRoot()));
		 
		tree.createNodesForGraph(mine.getRoot());
		
		in.close();
	}
	
}