package gainratio;

import java.io.IOException;
import java.util.ArrayList;

import definition.Instance;
import evaluate.EvaluateTree;


public class EvaluateTreeGR extends EvaluateTree{
	
	
	public EvaluateTreeGR(String trainData, String testData, String targetAtt) throws IOException {
		super(trainData,testData, targetAtt);
	}
	
	public EvaluateTreeGR(ArrayList<String> trainDataList, ArrayList<String> testDataList, String targetAttr) throws IOException {
		super(trainDataList, testDataList, targetAttr);
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

		ConstructTreeGR tree = new ConstructTreeGR(getTrainInstances(), getAttributes(), getTarget());
		//root = tree.construct();
		super.setRoot(tree.construct());

		long teTime = System.nanoTime();
		double generationTime = (teTime-tstTime)/1000f;
		System.out.println("Time taken to generate tree: " + generationTime + " s\n");

		// time taken to run predictions
		long startTime = System.nanoTime();
		
		int correct = 0;
		ArrayList<Instance> res = getResult();
		
		ArrayList<String> actual = new ArrayList<>();
		ArrayList<String> predictions = new ArrayList<>();

		for (Instance item : res) {
			String testLabel = item.getAttributeValuePairs().get("Test" + getTarget().getName());
			predictions.add(testLabel);
			String label = item.getAttributeValuePairs().get(getTarget().getName());
			actual.add(label);
			if (testLabel.equals(label)) {
				correct++;
			}
		}
		
		confusionMatrix = calculateConfusionMatrix(actual, predictions);
		
		//score = correct * 1.0 / res.size();
		super.setScore(correct * 1.0 / res.size());
		

		long endTime = System.nanoTime();
		double predTime = (endTime - startTime)/1000f;
		System.out.println("Time taken to generate prediction: " + predTime + " s\n");

		System.out.println("Accuracy:" + getScore() * 100 + "%");
		return "Time taken to generate tree: " + generationTime + " s\n" + "Time taken to generate prediction: " + predTime + " s\n" + confusionMatrix + ", Accuracy:" + getScore()*100 + "%";

	}
}
