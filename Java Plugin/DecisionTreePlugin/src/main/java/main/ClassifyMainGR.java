package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import cv.CrossValidation;
import gainratio.EvaluateTreeGR;
import gini.EvaluateTreeGI;
import input.ProcessInputData;
import output.PrintTree;


/**
 * 
 * This main class is used to create decision tree based on gain ratio from csv or from arraylist of nodes
 * The EvaluateTreeGI takes in three paths, path1-training set file path, path2 - test set file path, 
 * path3- the target level string 
 * 
 * @author nasim
 *
 */

public class ClassifyMainGR {
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
		String[] paths = Constants.LOCAL_DATASET.split(",");
		
		EvaluateTreeGR mine = new EvaluateTreeGR("data/diabetes_train.csv", "data/diabetes_test.csv", "Diabetes_012");
		//ArrayList<String> trainFile = ProcessInputData.CustomListFromCSV("data/flu_train.csv");
		//ArrayList<String> testFile = ProcessInputData.CustomListFromCSV("data/flu_test.csv");
		//EvaluateTreeGR mine = new EvaluateTreeGR(trainFile,testFile,Constants.TARGET_ATTRIBUTE);
		
//		ArrayList<String> customList = ProcessInputData.CustomListFromCSV("data/diabetes_health_indicators.csv");
//		CrossValidation cv = new CrossValidation(customList,"Diabetes_012");
//		
//		ArrayList<Double> final_score = cv.validate(Integer.parseInt("5"), "GainRatio");
//		
//		double mcc = cv.getMccAverage();
//		System.out.println("calculated mcc: " + mcc);
//		double totalGenerationTime = cv.getCvGenerationTimeAverage();
//		System.out.println("calculated generation time: " + totalGenerationTime);

//		double score = cv.getScoreAverage();
		
//		System.out.println("calculated accuracy: " + score);
		
	    mine.calculateAccuracy();
		
	    PrintTree tree = new PrintTree();
	    System.out.println(mine.getRoot());
	    
	    //System.out.println(tree.printDFS(mine.getRoot()));
		tree.createNodesForGraph(mine.getRoot());
		System.out.println(tree.nodesBucket);
	    
		in.close();
	}

}
