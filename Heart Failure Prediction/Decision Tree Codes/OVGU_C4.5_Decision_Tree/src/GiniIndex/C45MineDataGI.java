/**
 * This class is used for mining data.
 */

package GiniIndex;

import java.io.IOException;
import java.util.ArrayList;
//import java.util.HashMap;

import TreeDefination.TreeNode;
import MineData.C45MineData;
import DataDefination.Instance;

public class C45MineDataGI extends C45MineData{
	
	
	public C45MineDataGI(String trainData, String testData) throws IOException {
		super(trainData,testData);
		 		
	}
	
	/**
	 * After constructing 
	 */
	
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
		
		ConstructTreeGI tree = new ConstructTreeGI(trainInstances, attributes, target);
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
		
		System.out.println("Accuracy: " + score*100 + "%");

	}
}