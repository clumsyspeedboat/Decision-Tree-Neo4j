package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import cv.CrossValidation;
import evaluate.EvaluateTree;
import gini.EvaluateTreeGI;
import input.ProcessInputData;
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
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
		String[] paths = Constants.LOCAL_DATASET.split(",");
		
	    EvaluateTree mine = new EvaluateTree(paths[0], paths[1], Constants.TARGET_ATTRIBUTE, Constants.IS_PRUNED, Constants.MAX_DEPTH);

	    mine.calculateAccuracy();
		
	    PrintTree tree = new PrintTree();
	    System.out.println(mine.getRoot());
	    
		tree.createNodesForGraph(mine.getRoot());
		
		in.close();
	}
	
}