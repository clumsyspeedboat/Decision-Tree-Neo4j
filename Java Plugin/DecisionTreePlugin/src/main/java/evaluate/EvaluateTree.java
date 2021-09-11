/**
 * This class is used for evaluating decision tree.
 */

package evaluate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.glassfish.jersey.internal.guava.Stopwatch;

import core.ConstructTree;
import definition.Attribute;
import definition.Instance;
import input.ProcessInputData;
import node.TreeNode;


public class EvaluateTree {
	private ArrayList<Attribute> attributes;
	private ArrayList<Instance> testInstances;
	private ArrayList<Instance> trainInstances;
	private Attribute target;
	private TreeNode root;
	protected ArrayList<Instance> result;
	private Double score = 0.0;
	

	/**
	 * Constructor to process train and test data 
	 * @param trainData
	 * @param testData
	 * @throws IOException
	 */
	public EvaluateTree(String trainData, String testData, String targetAttr) throws IOException {
		result = new ArrayList<Instance>();
		ProcessInputData train = new ProcessInputData(trainData, targetAttr);
		ProcessInputData test = new ProcessInputData(testData, targetAttr);	

		
		this.attributes = train.getAttributeSet();
		this.target = train.getTargetAttribute();
	
		this.trainInstances = train.getInstanceSet();
		this.testInstances = test.getInstanceSet();
		
		result.addAll(testInstances);
	}
	
	
	/**
	 * Constructor to process train and test data from neo4j nodes 
	 * @param trainDataList  - The training list of nodes
	 * @param testDataList - The test list of nodes
	 * @param targetAttr- The target Attribute
	 * @throws IOException
	 */
	public EvaluateTree(ArrayList<String> trainDataList, ArrayList<String> testDataList, String targetAttr) throws IOException {
		result = new ArrayList<Instance>();
		ProcessInputData train = new ProcessInputData(trainDataList, targetAttr);
		ProcessInputData test = new ProcessInputData(testDataList, targetAttr);	
		
		this.attributes = train.getAttributeSet();
		this.target = train.getTargetAttribute();

		this.trainInstances = train.getInstanceSet();
		this.testInstances = test.getInstanceSet();	
		
		result.addAll(testInstances);
	}
	
	
	
	
	
	

	/**
	 * Loop through the entire tree 
	 */
	protected void mine(){
		for (int i = 0; i < testInstances.size(); i++) {
			TreeNode node = root;
			Instance currInstance = testInstances.get(i);
			Instance resInstance = result.get(i);
			
			if (node != null) {
				try {
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
								try {
									Double.parseDouble(value);
									Double.parseDouble(threshold);
								} catch (NumberFormatException e) {
									// not a double
									
									value = "0.0";
									threshold = "0.0";

								}

								if (Double.parseDouble(value) < Double.parseDouble(threshold)) {
									tmp = "less";
								} else {
									tmp = "more";
								}

								String curLabel = s.substring(0, 4);
								if (tmp.equals(curLabel))
									node = children.get(s);
							}
						} else {

							HashMap<String, TreeNode> children = node.getChildren();
							node = children.get(value);

						}
					}
				}catch(Exception e) {
					continue;
				}
				
				HashMap<String, String> pairs = resInstance.getAttributeValuePairs();
				pairs.put("Test" + target.getName(), node.getTargetLabel());
			}
		}
	}
	
	
	public ArrayList<Instance> getResult(){
		mine();
		return result;
	}
	
	
	
	/**
	 * This function creates the confusion matrix from actual and predicted arrays
	 * @param act
	 * @param pred
	 */
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
	         if(predLabel == null) {
	        	 continue;
	         }
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
	 * Evaluate the decision tree on the test set and calculate accuracy
	 * 
	 * @throws IOException
	 */
	public String calculateAccuracy() throws IOException {
		
		//time taken to generate the tree
		String confusionMatrix = "";
		long tstTime = System.currentTimeMillis();
		
		
		ConstructTree tree = new ConstructTree(this.trainInstances, this.attributes, this.target);
		root = tree.construct();
		
		
		
		long teTime = System.currentTimeMillis();
		
		//double generationTime = calculateTime(tstTime, teTime);
		double generationTime = (teTime-tstTime)/1000f;

		//System.out.println("Time taken to generate tree: " + generationTime + "ms\n");
		System.out.println("Time taken to generate tree:"+ generationTime +"s");
		
	
		//time taken to run predictions 
		long startTime = System.currentTimeMillis();
		
		
		int correct = 0;
		ArrayList<Instance> res = getResult();
		//System.out.println(res);
		ArrayList<String> actual = new ArrayList<>();
		ArrayList<String> predictions = new ArrayList<>();
		
		
		
		for (Instance item : res) {				
			String testLabel = item.getAttributeValuePairs().get("Test" + target.getName());
			predictions.add(testLabel);
			String label = item.getAttributeValuePairs().get(target.getName());
			actual.add(label);
			if(testLabel == null) {
				continue;
			}
			
			if(testLabel.equals(label)) {
				correct++;
			}
		}
		
		System.out.println(actual);
		System.out.println(predictions);
		
		confusionMatrix = calculateConfusionMatrix(actual, predictions);
		
		score = correct * 1.0 / res.size();
		
		long endTime = System.currentTimeMillis(); 
		//double predTime = calculateTime(startTime, endTime);
		double predTime = (endTime - startTime)/1000f;
		System.out.println("Time taken to generate prediction: " + predTime + " s\n");
		 
		
		System.out.println("Accuracy:" + score*100 + "%");
		return "Time taken to generate tree: " + generationTime + " s\n" + "Time taken to generate prediction: " + predTime + " s\n" + confusionMatrix + ", Accuracy:" + score*100 + "%";
	}
	
	/**
	 * Setter for members
	 * @param root
	 */
	public void setRoot(TreeNode root) {
		this.root = root;
	}
	
	
	public void setScore(Double score) {
		this.score = score;
	}

	
	/**
	 * Getter for members
	 * @return
	 */
	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	public ArrayList<Instance> getTestInstances() {
		return testInstances;
	}


	public ArrayList<Instance> getTrainInstances() {
		return trainInstances;
	}
	
	public Attribute getTarget() {
		return target;
	}

	
	public TreeNode getRoot(){
		return root;
	}
	
	public Double getScore() {
		return score;
	}

	
}