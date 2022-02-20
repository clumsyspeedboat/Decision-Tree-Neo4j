/**
 * Cross validation class
 */


package cv;

import core.ConstructTree;
import definition.Attribute;
import definition.Instance;
import gainratio.ConstructTreeGR;
import gini.ConstructTreeGI;
import input.ProcessInputData;
import node.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import evaluate.EvaluateTree;

public class CrossValidation {
	private ArrayList<Attribute> attributes;
	private ArrayList<Instance> trainInstances;
	private ArrayList<Instance> testInstances;
	private ArrayList<ArrayList<Instance>> testBundles;
	private Attribute target;
	private TreeNode root;
	private ArrayList<Instance> result;
	private ArrayList<Instance> totalInstances;
	private ArrayList<Double> scores;
	private double scoresAverage;
	Random rand;
	String impurity; 
	private ArrayList<Double> cvGenerationTime;
	private double cvGenerationTimeAverage;
	private ArrayList<Double> mccArray;
	private double mccAverage;
	
	String cfmDiabetes = "";
	
	/**
	 * Constructor which process the csv file 
	 * @param trainData
	 * @throws IOException
	 */
	public CrossValidation(String trainData, String targetAttr) throws IOException {

		result = new ArrayList<Instance>();
		cvGenerationTime = new ArrayList<Double>();
		
		ProcessInputData input = new ProcessInputData(trainData, targetAttr);
		this.attributes = input.getAttributeSet();

		this.target = input.getTargetAttribute();
		
		this.testBundles = new ArrayList<ArrayList<Instance>>();

		this.totalInstances = input.getInstanceSet();
		
		this.mccArray = new ArrayList<Double>();

		rand = new Random(totalInstances.size());
	}
	
	
	/*
	 * Constructor which process data from nodes in Neo4j
	 */
	
	public CrossValidation(ArrayList<String> trainDataList, String targetAttr) throws IOException {
		result = new ArrayList<Instance>();
		cvGenerationTime = new ArrayList<Double>();
		
		ProcessInputData input = new ProcessInputData(trainDataList, targetAttr);
		this.attributes = input.getAttributeSet();

		this.target = input.getTargetAttribute();
		

		this.testBundles = new ArrayList<ArrayList<Instance>>();

		this.totalInstances = input.getInstanceSet();
		
		this.mccArray = new ArrayList<Double>();

		rand = new Random(totalInstances.size());
		
	}
	
	/**
	 * Shuffle data and put them into k bundles, preparing for cross validation on k folds.
	 * @param k
	 */
	public void shuffle(int k) {
		int total_size = totalInstances.size();

		int average = total_size / k;
		
		for(int i = 0; i < k - 1; i++) {
			ArrayList<Instance> curBundle = new ArrayList<Instance>();
			for(int j = 0; j < average; j++) {
				int size = totalInstances.size();
				int curIndex = rand.nextInt(size);
				curBundle.add(totalInstances.get(curIndex));
				totalInstances.remove(curIndex);
			}
			testBundles.add(curBundle);	
		}
		

		ArrayList<Instance> lastBundle = new ArrayList<Instance>();
		
		for(int i = 0; i < totalInstances.size(); i++) {
			lastBundle.add(totalInstances.get(i));
		}
		testBundles.add(lastBundle);
	}
	
	/**
	 * Mine input data (e.g. put target attribute label on input data).
	 */
	private void mine() {
		for (int i = 0; i < testInstances.size(); i++) {
			TreeNode node = root;
			Instance currInstance = testInstances.get(i);
			Instance resInstance = result.get(i);

			while (!node.getType().equals("leaf")) {
				String attributeName = node.getAttribute().getName();
				String attributeType = node.getAttribute().getType();

				HashMap<String, String> attributeValuePairs = currInstance.getAttributeValuePairs();
				String value = attributeValuePairs.get(attributeName);
				if (attributeType.equals("continuous")) {
					HashMap<String, TreeNode> children = node.getChildren();
					String tmp = "";
					for (String s : children.keySet()) {
						String threshold = s.substring(4);
						if (Double.parseDouble(value) < Double.parseDouble(threshold)) {
							tmp = "less";
						} else {
							tmp = "more";
						}
						String curLabel = s.substring(0, 4);
						if (tmp.equals(curLabel)) node = children.get(s);
					}
				} else {
					HashMap<String, TreeNode> children = node.getChildren();
					node = children.get(value);
				}
			}
			HashMap<String, String> pairs = resInstance.getAttributeValuePairs();
			pairs.put("Test" + target.getName(), node.getTargetLabel());
		}
	}
	
	/**
	 * This function creates the confusion matrix from actual and predicted arrays
	 * @param act
	 * @param pred
	 */
	public ArrayList<Integer> calculateConfusionMatrix(ArrayList<String> act, ArrayList<String> pred) {
		 ArrayList<Integer> conMatrixArray = new ArrayList<Integer>();
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
	         if(predLabel == null) {
	        	 continue;
	         }
	         int outLabelIndex = categories.indexOf(predLabel);
	         int actualLabelIndex = categories.indexOf(actualLabel);
	         confMatrix[actualLabelIndex][outLabelIndex] += 1;
	     
		 }
		 cfmDiabetes = cfmDiabetes + "|" + Arrays.deepToString(confMatrix);
		 System.out.println(cfmDiabetes);
		 
//		 for (int[] x : confMatrix)
//		 {
//		    for (int y : x)
//		    {
//		         System.out.print(y + " ");
//		    }
//		    System.out.println();
//		 }
		 
		 if(matrixSize==2) {
			 truePositive = confMatrix[0][0]; 
			 trueNegative = confMatrix[1][1];
			 falseNegative = confMatrix[1][0];
			 falsePositive = confMatrix[0][1];
			 
			 String tp=String.format("TP: %d",truePositive);
			 String tn=String.format("TN: %d",trueNegative);
			 String fp=String.format("FP: %d",falsePositive);
			 String fn=String.format("FN: %d",falseNegative);
			
			 conMatrixArray.add(truePositive);
			 conMatrixArray.add(trueNegative);
			 conMatrixArray.add(falsePositive);
			 conMatrixArray.add(falseNegative);
		 }else 
		 {
			 double[] predictedArray = new double[matrixSize];
			 double[] actualArray = new double[matrixSize];
			 double s = 0.0;
			 double c = 0.0;
			 for(int row=0; row<matrixSize; row++)
			 {
			    for(int col=0; col<matrixSize; col++)
			    {
			    	predictedArray[row] = predictedArray[row] + confMatrix[row][col];
			    	actualArray[col] = actualArray[col] + confMatrix[row][col];
			    	if(row == col)
			    	{
			    		c = c + confMatrix[row][col];
			    	}
			    }
			    s = s + predictedArray[row];
			 }
			 double numerator = 0.0;
			 double denominator = 0.0;
			 double denominator1 = s*s;
			 double denominator2 = s*s;
			 //calculate numerator and denominator1 and 2
			 numerator = s*c;
			 for (int i = 0; i < actualArray.length; i ++)
			 {
				 numerator = numerator - actualArray[i]*predictedArray[i];
				 denominator1 = denominator1 - actualArray[i]*actualArray[i];
				 denominator2 = denominator2 - predictedArray[i]*predictedArray[i];
			 }
			 denominator = Math.sqrt(denominator1)*Math.sqrt(denominator2);
			 double mcc = 0.0;
			 if(denominator!=0)
			 {
				 mcc = numerator/denominator;
			 }
			 mccArray.add(mcc);
		 }
		 		 
		 return conMatrixArray;
	}
	
	/**
	 * Get result of mined data.
	 * @return the result of mined data.
	 */
	public ArrayList<Instance> getResult() {
		mine();
		return result;
	}
	
	/**
	 * Do cross validation on input data.
	 * @param crossValidationN
	 * @return the result of cross validation
	 * @throws IOException
	 */
	public ArrayList<Double> validate(int crossValidationN, String algorithmType) throws IOException {
		shuffle(crossValidationN);
		scores = new ArrayList<Double>();
		for(int i = 0; i < testBundles.size(); i++) {
			trainInstances = new ArrayList<Instance>();
		
			testInstances = new ArrayList<Instance>();
			
			result = new ArrayList<Instance>();
			long startTime = System.currentTimeMillis();
			
			for(int j = 0; j < testBundles.size(); j++) {
				if(i == j) {
					result.addAll(testBundles.get(j));
					testInstances.addAll(testBundles.get(j));
				} else {
					trainInstances.addAll(testBundles.get(j));
				}
			}
			
			
			ConstructTree tree;
			if(algorithmType == "InfoGain")
			{
				tree = new ConstructTree(trainInstances, attributes, target, "False", 0);
			}
			else if(algorithmType == "GainRatio")
			{
				tree = new ConstructTreeGR(trainInstances, attributes, target, "False", 0);
			}
			else
			{
				tree = new ConstructTreeGI(trainInstances, attributes, target, "False", 0);
			}
			
			root = tree.construct();
			
			long endTime = System.currentTimeMillis();
			double crossValidationGenTime = (endTime -startTime)/1000f;
			cvGenerationTime.add(crossValidationGenTime);


			int correct = 0;
			ArrayList<Instance> res = getResult();
			ArrayList<String> actual = new ArrayList<String>();
			ArrayList<String> prec = new ArrayList<String>();
			
			for (Instance item : res) {				
				String testLabel = item.getAttributeValuePairs().get("Test" + target.getName());
				String label = item.getAttributeValuePairs().get(target.getName());
				prec.add(testLabel);
				actual.add(label);
				if(testLabel.equals(label)) {
					correct++;
				}
			}
			ArrayList<Integer> conMatrixArray = calculateConfusionMatrix(actual, prec);
//			if(conMatrixArray.size()!=0)
//			{
//				System.out.println(conMatrixArray.toString());
//				double mcc = mccCalculation(conMatrixArray);
//				mccArray.add(mcc);
//			}
			scores.add(correct * 1.0 / res.size());
		}
		return scores;
	}
	
	public Double getMccAverage()
	{
		double total = 0.0;
		for(double i : mccArray)
		{
			total = total + i;
		}
		mccAverage = total/mccArray.size();
		return mccAverage;
	}
	
	public Double mccCalculation(ArrayList<Integer> conMatrixArray)
	{
		double mcc = 0.0;
		int TP = conMatrixArray.get(0);
		int TN = conMatrixArray.get(1);
		int FP = conMatrixArray.get(2);
		int FN = conMatrixArray.get(3);
		double numerator = ((TP * TN) - (FP * FN));
		double denominator = Math.sqrt((TP + FP)*(TP + FN)*(TN + FP)*(TN + FN));
		if(denominator != 0)
		{
			mcc = numerator/denominator;
		}
		else
		{
			mcc = 0.0;
		}
		return mcc;
	}
	
	public ArrayList<Double> getCvGenerationTime() {
		return cvGenerationTime;
	}
	
	public double getCvGenerationTimeAverage() {
		double time = 0.0;
		for(double i : cvGenerationTime)
		{
			time = time + i;
		}
		cvGenerationTimeAverage = time/cvGenerationTime.size();
		return cvGenerationTimeAverage;
	}
	
	public double getScoreAverage() {
		double score = 0.0;
		for(double i : scores)
		{
			score = score + i;
		}
		scoresAverage = score/scores.size();
		return scoresAverage;
	}
	
	public String getCfmDiabetes()
	{
		return cfmDiabetes;
	}
	

	/**
	 * 
	 * @return tree root
	 */
	public TreeNode getRoot() {
		return root;
	}
}