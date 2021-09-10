package main;

import java.io.IOException;
import java.util.Scanner;
import evaluate.EvaluateTree;
import output.PrintTree;

/**
 * 
 * This main class is used to create decision tree based on information gain from csv or from arraylist of nodes
 * The EvaluateTreeGI takes in three paths, path1-training set file path, path2 - test set file path, 
 * path3- the target level string 
 * 
 * @author nasim
 *
 */

public class ClassifyMainIG {
	
	static final String LOCAL_DATASET = "data/train.csv,data/test.csv";
	static final String TARGET_ATTRIBUTE = "DEATH_EVENT";
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
		String[] paths = LOCAL_DATASET.split(",");
		
		EvaluateTree mine = new EvaluateTree(paths[0], paths[1], TARGET_ATTRIBUTE);
	
		mine.calculateAccuracy();

	    PrintTree tree = new PrintTree();
	    
		System.out.println(tree.printDFS(mine.getRoot()));
		 
		tree.createNodesForGraph(mine.getRoot());
		
		in.close();
	}
	
}