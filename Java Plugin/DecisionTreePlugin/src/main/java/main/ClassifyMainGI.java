package main;

import java.io.IOException;
import java.util.Scanner;
import gini.EvaluateTreeGI;
import output.PrintTree;

/**
 * This main class is used to create decision tree based on gini from csv or from arraylist of nodes
 * The EvaluateTreeGI takes in three paths, path1-training set file path, path2 - test set file path, 
 * path3- the target level string 
 * 
 * @author nasim
 *
 */

public class ClassifyMainGI {
	
	static final String LOCAL_DATASET = "data/flu_train.csv,data/flu_test.csv";
	static final String TARGET_ATTRIBUTE = "Diagnosis";
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
		String[] paths = LOCAL_DATASET.split(",");
		
		EvaluateTreeGI mine = new EvaluateTreeGI(paths[0],paths[1],TARGET_ATTRIBUTE);
	
	    mine.calculateAccuracy();

	    PrintTree tree = new PrintTree();
	    
		System.out.println(tree.printDFS(mine.getRoot()));
		
		tree.createNodesForGraph(mine.getRoot());
		
		in.close();
	}

}
