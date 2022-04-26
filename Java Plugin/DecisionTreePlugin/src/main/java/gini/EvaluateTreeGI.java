package gini;

import java.io.IOException;
import java.util.ArrayList;

import evaluate.EvaluateTree;
import definition.*;

public class EvaluateTreeGI extends EvaluateTree{
	String isPruned = "True";
	int max_depth = 3;
	private String featureTable;
	
	/**
	 * Constructor to process the csv path 
	 *  
	 * @param trainData
	 * @param testData
	 * @param targetAttr
	 * @throws IOException
	 */

	public EvaluateTreeGI(String trainData, String testData, String targetAttr, String isPruned, int max_depth) throws IOException {
		super(trainData,testData, targetAttr, isPruned, max_depth);
		this.isPruned=isPruned;
		this.max_depth=max_depth;
	}
	
	/**
	 * Overloaded constructor to process the nodes from Neo4j
	 * 
	 * @param trainDataList
	 * @param testDataList
	 * @param targetAttr
	 * @throws IOException
	 */
	public EvaluateTreeGI(ArrayList<String> trainDataList, ArrayList<String> testDataList, String targetAttr, String isPruned, int max_depth) throws IOException {
		super(trainDataList, testDataList, targetAttr, isPruned, max_depth);
		this.isPruned=isPruned;
		this.max_depth=max_depth;
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
		ConstructTreeGI tree = new ConstructTreeGI(getTrainInstances(), getAttributes(), getTarget(), isPruned, max_depth);
		super.setRoot(tree.construct());
		featureTable = tree.getFeatureTable();
		
		
		
		long teTime = System.currentTimeMillis();
		double generationTime = (teTime-tstTime)/1000f;
		System.out.println("Time taken to generate tree:"+ generationTime +"s");
		
		//time taken to run predictions 
		long startTime = System.currentTimeMillis();
		
		
		int correct = 0;
		
		
		ArrayList<Instance> res = getResult();
		
		
		createClassificationResults(res);
		
		
		ArrayList<String> actual = new ArrayList<>();
		ArrayList<String> predictions = new ArrayList<>();
		
		for (Instance item : res) {	
			String testLabel = item.getAttributeValuePairs().get("Test" + getTarget().getName());
			predictions.add(testLabel);
			
			String label = item.getAttributeValuePairs().get(getTarget().getName());
			actual.add(label);
			if(testLabel == null) {
				continue;
			}
			
			if (testLabel.equals(label)) {
				correct++;
			}
			
		}
	
		confusionMatrix = calculateConfusionMatrix(actual, predictions);
		//confusionMatrix = " ";
		
		super.setScore(correct * 1.0 / res.size());
		
		long endTime = System.currentTimeMillis(); 
		double predTime = (endTime - startTime)/1000f;
		System.out.println("Time taken to generate prediction: " + predTime + " s\n");
		 
		
		//System.out.println("Accuracy:" + getScore()*100 + "%");
		return "Time taken to generate tree: " + generationTime + " s\n" + "Time taken to generate prediction: " + predTime + " s\n" + confusionMatrix + "%";
	}
	
	public String getFeatureTable()
	{
		return featureTable;
	}
}