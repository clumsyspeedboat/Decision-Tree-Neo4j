/**
 * This class is used for mining data.
 */

package GainRatio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import ProcessInput.ProcessInputData;
import TreeDefination.TreeNode;
import C45CoreAlgorithm.ConstructTree;
import DataDefination.Attribute;
import DataDefination.Instance;
import MineData.C45MineData;

public class GainRatioMineData extends C45MineData{
	
	public GainRatioMineData(String trainData, String testData) throws IOException {
		super(trainData,testData);
		/*
		 * result = new ArrayList<Instance>(); ProcessInputData train = new
		 * ProcessInputData(trainData); ProcessInputData test = new
		 * ProcessInputData(testData);
		 * 
		 * this.attributes = train.getAttributeSet(); this.target =
		 * train.getTargetAttribute();
		 * 
		 * target = train.getTargetAttribute(); trainInstances = train.getInstanceSet();
		 * testInstances = test.getInstanceSet(); result.addAll(testInstances);
		 */
	}
	
	/**
	 * After constructing 
	 */
	/*
	 * private void mine() { for (int i = 0; i < testInstances.size(); i++) {
	 * TreeNode node = root; Instance currInstance = testInstances.get(i); Instance
	 * resInstance = result.get(i); while (!node.getType().equals("leaf")) { String
	 * attributeName = node.getAttribute().getName(); String attributeType =
	 * node.getAttribute().getType(); HashMap<String, String> attributeValuePairs =
	 * currInstance.getAttributeValuePairs(); String value =
	 * attributeValuePairs.get(attributeName); if
	 * (attributeType.equals("continuous")) { HashMap<String, TreeNode> children =
	 * node.getChildren(); String tmp = ""; for (String s : children.keySet()) {
	 * String threshold = s.substring(4); if (Double.parseDouble(value) <
	 * Double.parseDouble(threshold)) { tmp = "less"; } else { tmp = "more"; }
	 * String curLabel = s.substring(0, 4); if (tmp.equals(curLabel)) node =
	 * children.get(s); } } else { HashMap<String, TreeNode> children =
	 * node.getChildren(); node = children.get(value); } } HashMap<String, String>
	 * pairs = resInstance.getAttributeValuePairs(); pairs.put("Test" +
	 * target.getName(), node.getTargetLabel()); } }
	 * 
	 * public ArrayList<Instance> getResult() { mine(); return result; }
	 * 
	 * public TreeNode getRoot() { return root; }
	 */
	
	/**
	 * Evaluate the decision tree on the test set 
	 * 
	 * @throws IOException
	 */
	
	
	public void calculateAccuracy() throws IOException {
		ConstructTreeGR tree = new ConstructTreeGR(trainInstances, attributes, target);
		root = tree.construct();
		
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
		
		System.out.println("Accuracy:" + score*100 + "%");

	}
}