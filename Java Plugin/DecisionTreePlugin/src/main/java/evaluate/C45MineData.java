/**
 * This class is used for mining data.
 */

package evaluate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import core.ConstructTree;
import definition.Attribute;
import definition.Instance;
import input.ProcessInputData;
import node.TreeNode;

public class C45MineData {
	protected ArrayList<Attribute> attributes;
	private ArrayList<Instance> testInstances;
	protected ArrayList<Instance> trainInstances;
	protected Attribute target;
	protected TreeNode root;
	protected ArrayList<Instance> result;
	protected Double score = 0.0;
	
	/**
	 * Constructor to process train and test data 
	 * @param trainData
	 * @param testData
	 * @throws IOException
	 */
	
	public C45MineData(String trainData, String testData) throws IOException {
		result = new ArrayList<Instance>();
		ProcessInputData train = new ProcessInputData(trainData);
		ProcessInputData test = new ProcessInputData(testData);	
		
		this.attributes = train.getAttributeSet();
		this.target = train.getTargetAttribute();
		
		target = train.getTargetAttribute();
		trainInstances = train.getInstanceSet();
		testInstances = test.getInstanceSet();	
		
		result.addAll(testInstances);
	}
	
	
	/**
	 * After constructing 
	 */
	protected void mine(){
		for(int i = 0; i < testInstances.size(); i++){
			
			TreeNode node = root;
			Instance currInstance = testInstances.get(i);
			Instance resInstance = result.get(i);
			while (!node.getType().equals("leaf")) {
				String attributeName = node.getAttribute().getName();
				String attributeType = node.getAttribute().getType();
				HashMap<String, String> attributeValuePairs = currInstance.getAttributeValuePairs();
				String value = attributeValuePairs.get(attributeName);
	
				if (attributeType.equals("continuous")){
					HashMap<String, TreeNode> children = node.getChildren();
					
					String tmp = "";
					
					for (String s : children.keySet()) {
						String threshold = s.substring(4);
						
						if (Double.parseDouble(value) < Double.parseDouble(threshold)){
							tmp = "less";
						} else {
							tmp = "more";
						}
						
						String curLabel = s.substring(0, 4);
						if (tmp.equals(curLabel)) node = children.get(s);
					}
				}else{
					HashMap<String, TreeNode> children = node.getChildren();
					node = children.get(value);
				}
			}
			
			HashMap<String, String> pairs = resInstance.getAttributeValuePairs();
			pairs.put("Test" + target.getName(), node.getTargetLabel());
			
		}
	}
	
	
	public ArrayList<Instance> getResult(){
		mine();
		return result;
	}
	
	
	public TreeNode getRoot(){
		return root;
	}
	
	
	public double calculateTime(long strTime, long eTime) {
		long elTime = eTime - strTime; 
		double mTime = (double) elTime / 1000000.0;
		double sTime = mTime / 1000;
		return sTime;
	}
	
	
	
	/**
	 * Evaluate the decision tree on the test set and calculate accuracy
	 * 
	 * @throws IOException
	 */
	public void calculateAccuracy() throws IOException {
		//time taken to generate the tree
		long tstTime = System.nanoTime();
		
		ConstructTree tree = new ConstructTree(trainInstances, attributes, target);
		root = tree.construct();
		
		long teTime = System.nanoTime();
		double generationTime = calculateTime(tstTime, teTime);
		System.out.println("Time taken to generate tree: " + generationTime + " s\n");
		
		
		
		//time taken to run predictions 
		long startTime = System.nanoTime();
		
		int correct = 0;
		ArrayList<Instance> res = getResult();
		
		for (Instance item : res) {				
			String testLabel = item.getAttributeValuePairs().get("Test" + target.getName());
			String label = item.getAttributeValuePairs().get(target.getName());
			if(testLabel.equals(label)) {
				correct++;
			}
		}
		score = correct * 1.0 / res.size();
		
		long endTime = System.nanoTime(); 
		double predTime = calculateTime(startTime, endTime);
		System.out.println("Time taken to generate prediction: " + predTime + " s\n");
		 
		
		System.out.println("Accuracy:" + score*100 + "%");
	}
	
}