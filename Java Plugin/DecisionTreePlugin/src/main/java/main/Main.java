package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import cv.CrossValidation;
import cv.CrossValidationWithPruning;
import input.ProcessInputData;
import output.PrintTree;
/**
 * This class is used to run cross validation on the entire sample 
 * Dataset1 - heart_failure_original.csv . Target - 'DEATH_EVENT'
 * Dataset2 - flu_classification_original.csv. Target - 'Diagnosis'
 * Dataset3 - metaprotein_original.csv. Target - 'PatientType'
 * 
 * AlgorithmType - 'InfoGain', 'GiniIndex', 'GainRatio'
 *
 * @author Nasim
 *
 */

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        System.out.println("===============================================================");
        
       
//        ArrayList<String> trainList = ProcessInputData.CustomListFromCSV("data/metaprotein_original.csv");
        
        CrossValidation cv = new CrossValidation("data/metaprotein_original.csv", "PatientType");
        
//        CrossValidation cv = new CrossValidation(trainList, "Patient_Type");
//        CrossValidationWithPruning cvP = new CrossValidationWithPruning("data/heart_failure.csv", "Patient_Type");

        ArrayList<Double> final_score = cv.validate(10,"InfoGain");
        //ArrayList<Double> final_score_P = cvP.validate(10);
        
        
        double r = 0;
        //double rP = 0;
        double gt = 0;
        
        for(int i = 0; i < final_score.size(); i++) {
            r += final_score.get(i);
            //rP += final_score_P.get(i);
        }
        
       
        if(cv.getCvGenerationTime() != null) {
        	ArrayList<Double> totalGenerationTime = cv.getCvGenerationTime();
        	for(int i = 0; i < totalGenerationTime.size(); i++) {
                gt += totalGenerationTime.get(i);
                
            }
        }

        PrintTree print = new PrintTree();

       
        System.out.println("Cross Validation Accuracy without Pruning: " + (r / 10) * 100 + "%");
       // System.out.println("Cross Validation Accuracy after Pruning: " + (rP / 10)* 100 + "%");
        System.out.println("Cross Validation Generation Time without pruning: " + (gt/10) + "sec");
        
        // Calculate and print total time taken.
        
        
        	
		/*System.out.println("**********************");
		System.out.println("Do you want to see the tree before pruning? (y/n)");
		String printTreeBefore = in.nextLine();
		if (printTreeBefore.equals("y")) {
			System.out.println("Tree before Pruning: \n" + print.printDFS(cv.getRoot()));
		}
		System.out.println("**********************");
		System.out.println("Do you want to see the tree after pruning? (y/n)");
		String printTreeAfter = in.nextLine();
		if (printTreeAfter.equals("y")) {
			System.out.println("Tree after Pruning: \n" + print.printDFS(cvP.getRoot()));
		}*/
		
        in.close();
    }
}