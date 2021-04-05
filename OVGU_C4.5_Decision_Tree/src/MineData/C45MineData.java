/**
 * This class is used for mining data.
 */

package MineData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import ProcessInput.ProcessInputData;
import TreeDefination.TreeNode;
import C45CoreAlgorithm.ConstructTree;
import DataDefination.Attribute;
import DataDefination.Instance;

public class C45MineData {
	private ArrayList<Attribute> attributes;
	private ArrayList<Instance> testInstances;
	private ArrayList<Instance> trainInstances;
	private Attribute target;
	private TreeNode root;
	private ArrayList<Instance> result;
	private Double score = 0.0;
	
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
	
	public ArrayList<Instance> getResult() {
		mine();
		return result;
	}
	
	public TreeNode getRoot() {
		return root;
	}
	
	/**
	 * Evaluate the decision tree on the test set 
	 * 
	 * @throws IOException
	 */
	
	public void calculateAccuracy() throws IOException {
		ConstructTree tree = new ConstructTree(trainInstances, attributes, target);
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
		
		System.out.println(score);

	}
}