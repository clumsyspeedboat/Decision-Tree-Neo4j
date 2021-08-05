package Gini;

import java.io.IOException;
import java.util.ArrayList;


import evaluate.C45MineData;
import output.PrintTree;
import definition.*;

public class C45MineDataGI extends C45MineData{
	
	
	public C45MineDataGI(String trainData, String testData) throws IOException {
		super(trainData,testData);
	}
	

	/**
	 * Evaluate the decision tree on the test set 
	 * 
	 * @throws IOException
	 */
	
	public String calculateAccuracy() throws IOException {
		
		//time taken to generate the tree
		long tstTime = System.nanoTime();
		String confusionMatrix = "";
		
		ConstructTreeGI tree = new ConstructTreeGI(trainInstances, attributes, target);
		root = tree.construct();
		
		long teTime = System.nanoTime();
		double generationTime = calculateTime(tstTime, teTime);
		System.out.println("Time taken to generate tree: " + generationTime + " s\n");
		

		//time taken to run predictions 
		long startTime = System.nanoTime();
		
		int correct = 0;
		ArrayList<Instance> res = getResult();
		//
		ArrayList<String> actual = new ArrayList<>();
		ArrayList<String> predictions = new ArrayList<>();
		
		for (Instance item : res) {				
			String testLabel = item.getAttributeValuePairs().get("Test" + target.getName());
			predictions.add(testLabel);
			String label = item.getAttributeValuePairs().get(target.getName());
			actual.add(label);

			if(testLabel != null) {
				if(testLabel.equals(label)) {
					correct++;
				}
			}
			
			
		}
		
		calculateConfusionMatrix(actual, predictions);
		
		score = correct * 1.0 / res.size();
		
		confusionMatrix = calculateConfusionMatrix(actual, predictions);
		
		long endTime = System.nanoTime(); 
		double predTime = calculateTime(startTime, endTime);
		System.out.println("Time taken to generate prediction: " + predTime + " s\n");
		 
		
		System.out.println("Accuracy:" + score*100 + "%");
		return confusionMatrix + ", Accuracy:" + score*100 + "%";

	}
}