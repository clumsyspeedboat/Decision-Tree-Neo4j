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
		
		EvaluateTreeGI mine = new EvaluateTreeGI(paths[0],paths[1],Constants.TARGET_ATTRIBUTE);
		//ArrayList<String> trainFile = ProcessInputData.CustomListFromCSV("data/flu_train.csv");
		//ArrayList<String> testFile = ProcessInputData.CustomListFromCSV("data/flu_test.csv");
		//EvaluateTreeGI mine = new EvaluateTreeGI(trainFile,testFile,Constants.TARGET_ATTRIBUTE);
		
		ArrayList<String> customList = ProcessInputData.CustomListFromCSV("data/flu_classification.csv");
		CrossValidation cv = new CrossValidation(customList, "Diagnosis");
		
		ArrayList<Double> final_score = cv.validate(Integer.parseInt("50"), "InfoGain");
		double mcc = cv.getMccAverage();
		System.out.println("calculated mcc: " + mcc);
		ArrayList<Double> totalGenerationTime = cv.getCvGenerationTime();

	
	    mine.calculateAccuracy();

	    PrintTree tree = new PrintTree();
	    
		
		tree.createNodesForGraph(mine.getRoot());
		
		in.close();
	}
}
