package gainratio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import definition.Attribute;
import definition.Instance;
import evaluate.EvaluateTree;


public class EvaluateTreeGR extends EvaluateTree{
	String isPruned = "True";
	int max_depth = 3;
	private String featureTable;
	
	public EvaluateTreeGR(String trainData, String testData, String targetAtt, String isPruned, int max_depth) throws IOException {
		super(trainData,testData, targetAtt, isPruned, max_depth);
		this.isPruned=isPruned;
		this.max_depth=max_depth;
	}
	
	public EvaluateTreeGR(ArrayList<String> trainDataList, ArrayList<String> testDataList, String targetAttr, String isPruned, int max_depth) throws IOException {
		super(trainDataList, testDataList, targetAttr, isPruned, max_depth);
		this.isPruned=isPruned;
		this.max_depth=max_depth;
	}
	
	

	/**
	 * Evaluate the decision tree on the test set 
	 * 
	 * @throws IOException
	 */
	public String calculateAccuracy() throws IOException {
		//time taken to generate the tree
		long tstTime = System.currentTimeMillis();
		String confusionMatrix = "";

		ConstructTreeGR tree = new ConstructTreeGR(getTrainInstances(), getAttributes(), getTarget(), isPruned, max_depth);
		//root = tree.construct();
		super.setRoot(tree.construct());
		featureTable = tree.getFeatureTable();

		long teTime = System.currentTimeMillis();
		double generationTime = (teTime-tstTime)/1000f;
		System.out.println("Time taken to generate tree:"+ generationTime +"s");
		

		// time taken to run predictions
		long startTime = System.currentTimeMillis();
		
		int correct = 0;
		ArrayList<Instance> res = getResult();
		
		createClassificationResults(res);
				
	
		
//		for (HashMap.Entry<String, ArrayList<String>> entry : predictedResults.entrySet()) {
//		    String key = entry.getKey();
//		    ArrayList<String> value = entry.getValue();
//		    System.out.println(value.size());
//		}
		
		
		
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
		
		//score = correct * 1.0 / res.size();
		super.setScore(correct * 1.0 / res.size());
		

		long endTime = System.currentTimeMillis(); 
		//double predTime = calculateTime(startTime, endTime);
		double predTime = (endTime - startTime)/1000f;
		System.out.println("Time taken to generate prediction: " + predTime + " s\n");

		//System.out.println("Accuracy:" + getScore() * 100 + "%");
		return "Time taken to generate tree: " + generationTime + " s\n" + "Time taken to generate prediction: " + predTime + " s\n" + confusionMatrix + "%";
	}
	
	public String getFeatureTable()
	{
		return featureTable;
	}
}
