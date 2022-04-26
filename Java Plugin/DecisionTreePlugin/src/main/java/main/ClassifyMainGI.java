package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import cv.CrossValidation;
import gini.EvaluateTreeGI;
import input.ProcessInputData;
import output.PrintTree;

/**
 * 
 * This main class is used to create decision tree based on gini from csv or from arraylist of nodes
 * The EvaluateTreeGI takes in three paths, path1-training set file path, path2 - test set file path, 
 * path3- the target level string 
 * 
 * @author nasim
 *
 */

public class ClassifyMainGI {
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
		String[] paths = Constants.LOCAL_DATASET.split(",");
		
		EvaluateTreeGI mine = new EvaluateTreeGI(paths[0],paths[1],Constants.TARGET_ATTRIBUTE, Constants.IS_PRUNED, Constants.MAX_DEPTH);

		mine.calculateAccuracy();
		
	    PrintTree tree = new PrintTree();
	    System.out.println(mine.getRoot());
	    System.out.println(mine.getFeatureTable());
	    
		tree.createNodesForGraph(mine.getRoot());
	    
		in.close();
	}
}
