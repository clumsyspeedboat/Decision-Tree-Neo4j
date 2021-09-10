package gini;

import java.io.IOException;
import java.util.ArrayList;

import evaluate.EvaluateTree;
import definition.*;

public class EvaluateTreeGI extends EvaluateTree{
	
	public EvaluateTreeGI(String trainData, String testData, String targetAttr) throws IOException {
		super(trainData,testData, targetAttr);
	}
	
	public EvaluateTreeGI(ArrayList<String> trainDataList, ArrayList<String> testDataList, String targetAttr) throws IOException {
		super(trainDataList, testDataList, targetAttr);
	}
	
	
	/**
	 * Evaluate the decision tree on the test set 
	 * 
	 * @throws IOException
	 */
	@Override
	public String calculateAccuracy() throws IOException {
		//time taken to generate the tree
		String confusionMatrix = "";
		
		long tstTime = System.currentTimeMillis();
		ConstructTreeGI tree = new ConstructTreeGI(getTrainInstances(), getAttributes(), getTarget());
		super.setRoot(tree.construct());
		int count = 0;
	
		long teTime = System.currentTimeMillis();
		double generationTime = (teTime-tstTime)/1000f;
		System.out.println("Time taken to generate tree:"+ generationTime +"s");
		
		//time taken to run predictions 
		long startTime = System.currentTimeMillis();
		
		
		int correct = 0;
		ArrayList<Instance> res = getResult();
		//System.out.println(res);
		ArrayList<String> actual = new ArrayList<>();
		ArrayList<String> predictions = new ArrayList<>();
		
		for (Instance item : res) {	
			String testLabel = item.getAttributeValuePairs().get("Test" + getTarget().getName());
			predictions.add(testLabel);
			String label = item.getAttributeValuePairs().get(getTarget().getName());
			actual.add(label);
			if(testLabel != null && label != null)
			{
				if(testLabel.equals(label)) {
					correct++;
				}
			}
		}
		
		System.out.println(actual.size());
		System.out.println(predictions.size());
		confusionMatrix = calculateConfusionMatrix(actual, predictions);
		
		super.setScore(correct * 1.0 / res.size());
		
		long endTime = System.currentTimeMillis(); 
		//double predTime = calculateTime(startTime, endTime);
		double predTime = (endTime - startTime)/1000f;
		System.out.println("Time taken to generate prediction: " + predTime + " s\n");
		 
		
		System.out.println("Accuracy:" + getScore()*100 + "%");
		return "Time taken to generate tree: " + generationTime + " s\n" + "Time taken to generate prediction: " + predTime + " s\n" + confusionMatrix + ", Accuracy:" + getScore()*100 + "%";
	}
}