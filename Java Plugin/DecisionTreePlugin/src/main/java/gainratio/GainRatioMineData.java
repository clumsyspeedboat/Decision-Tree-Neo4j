package gainratio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import definition.Instance;
import evaluate.C45MineData;

public class GainRatioMineData extends C45MineData{
	
	public GainRatioMineData(String trainData, String testData) throws IOException {
		super(trainData,testData);
	}
	
	
	public String calculateConfusionMatrix(ArrayList<String> act, ArrayList<String> pred) {
		 int truePositive = 0;
		 int trueNegative = 0;
		 int falsePositive = 0;
		 int falseNegative = 0;
		 String confusionMatrix = "";
		
		 List<String> categories = target.getValues();
		 int matrixSize = categories.size();
		 int [][] confMatrix = new int[matrixSize][matrixSize];
		 for(int i=0; i<act.size(); i++) {
			 String predLabel = pred.get(i);
	         String actualLabel = act.get(i);
	         int outLabelIndex = categories.indexOf(predLabel);
	         int actualLabelIndex = categories.indexOf(actualLabel);
	         confMatrix[actualLabelIndex][outLabelIndex] += 1;
		 }
		 
		 if(matrixSize==2) {
			 truePositive = confMatrix[0][0]; 
			 trueNegative = confMatrix[1][1];
			 falseNegative = confMatrix[1][0];
			 falsePositive = confMatrix[0][1];
		 }else {
			 for(int row=0; row<matrixSize; row++)
			 {
			    for(int col=0; col<matrixSize; col++)
			    {
			    	if(row==0 && col==0) {
			    		truePositive = confMatrix[row][col];
			    	}else if(row==0 && col>0) {
			    		falsePositive += confMatrix[row][col];
			    	}else if(col==0 && row>0) {
			    		falseNegative += confMatrix[row][col];
			    	}
			    	if(row>0 && col>0) {
			    		trueNegative += confMatrix[row][col];
			    	}
			    }
			 }
		 }
		 
		 //System.out.println(Arrays.deepToString(confMatrix));
		 String tp=String.format("TP: %d",truePositive);
		 String tn=String.format("TN: %d",trueNegative);
		 String fp=String.format("FP: %d",falsePositive);
		 String fn=String.format("FN: %d",falseNegative);
		 System.out.println(tp);
		 System.out.println(tn);
		 System.out.println(fp);
		 System.out.println(fn);
		 confusionMatrix = tp + "\n" + tn + "\n" + fp + "\n" + fn;
		 return confusionMatrix;
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

		ConstructTreeGR tree = new ConstructTreeGR(trainInstances, attributes, target);
		root = tree.construct();

		long teTime = System.nanoTime();
		double generationTime = calculateTime(tstTime, teTime);
		System.out.println("Time taken to generate tree: " + generationTime + " s\n");

		// time taken to run predictions
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
			if (testLabel.equals(label)) {
				correct++;
			}
		}
		
		confusionMatrix = calculateConfusionMatrix(actual, predictions);
		
		score = correct * 1.0 / res.size();
		

		long endTime = System.nanoTime();
		double predTime = calculateTime(startTime, endTime);
		System.out.println("Time taken to generate prediction: " + predTime + " s\n");

		System.out.println("Accuracy:" + score * 100 + "%");
		return confusionMatrix + ", Accuracy:" + score*100 + "%";

	}
}
