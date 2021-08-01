package gainratio;

import java.io.IOException;
import java.util.ArrayList;
import definition.Instance;
import evaluate.C45MineData;

public class GainRatioMineData extends C45MineData{
	
	public GainRatioMineData(String trainData, String testData) throws IOException {
		super(trainData,testData);
	}
	
	
	
	/**
	 * Evaluate the decision tree on the test set 
	 * 
	 * @throws IOException
	 */
	public void calculateAccuracy() throws IOException {
		//time taken to generate the tree
		long tstTime = System.nanoTime();

		ConstructTreeGR tree = new ConstructTreeGR(trainInstances, attributes, target);
		root = tree.construct();

		long teTime = System.nanoTime();
		double generationTime = calculateTime(tstTime, teTime);
		System.out.println("Time taken to generate tree: " + generationTime + " s\n");

		// time taken to run predictions
		long startTime = System.nanoTime();

		int correct = 0;
		ArrayList<Instance> res = getResult();

		for (Instance item : res) {
			String testLabel = item.getAttributeValuePairs().get("Test" + target.getName());
			String label = item.getAttributeValuePairs().get(target.getName());
			if (testLabel.equals(label)) {
				correct++;
			}
		}
		score = correct * 1.0 / res.size();

		long endTime = System.nanoTime();
		double predTime = calculateTime(startTime, endTime);
		System.out.println("Time taken to generate prediction: " + predTime + " s\n");

		System.out.println("Accuracy:" + score * 100 + "%");

	}
}
