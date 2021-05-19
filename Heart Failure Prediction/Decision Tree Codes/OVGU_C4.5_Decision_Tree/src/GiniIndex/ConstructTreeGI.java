/** 
 * This class is used to construct tree
 */
package GiniIndex;

import DataDefination.Attribute;
import C45CoreAlgorithm.ConstructTree;
import DataDefination.Instance;
import TreeDefination.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstructTreeGI extends ConstructTree{


	
	public ConstructTreeGI(ArrayList<Instance> instances, ArrayList<Attribute> attributes,
                         Attribute target) {
		
		super(instances,attributes,target);
		
		this.instances = instances;
		this.attributes = attributes;
		this.target = target;
	}
	
	/**
	 * Construct tree
	 * @return TreeNode
	 * @throws IOException
	 */
	public TreeNode construct() throws IOException {
		return constructTree(target, attributes, instances);
	}
	
	/**
	 * Construct tree recursively. First make the root node, then construct its subtrees 
	 * recursively, and finally connect root with subtrees.
	 * @param target
	 * @param attributes
	 * @param instances
	 * @return TreeNode
	 * @throws IOException
	 */
	private TreeNode constructTree(Attribute target, ArrayList<Attribute> attributes, 
			ArrayList<Instance> instances) throws IOException {
		
		
		
		if (Gini_Index.calculate(target, instances) == 0 || attributes.size() == 0) {
			String leafLabel = "";
			if (Gini_Index.calculate(target, instances) == 0) {
				leafLabel = instances.get(0).getAttributeValuePairs().get(target.getName());
			} else {
				leafLabel = getMajorityLabel(target, instances);
			}
			TreeNode leaf = new TreeNode(leafLabel);
			return leaf;
		}
		
		// Choose the root attribute
		ChooseAttributeGI choose = new ChooseAttributeGI(target, attributes, instances);
		Attribute rootAttr = choose.getChosen();
		
		// Remove the chosen attribute from attribute set
		attributes.remove(rootAttr);
		
		// Make a new root
		TreeNode root = new TreeNode(rootAttr);
		
		// Get value subsets of the root attribute to construct branches
		HashMap<String, ArrayList<Instance>> valueSubsets = choose.getSubset();
		
		if (valueSubsets == null || valueSubsets.size() == 0) {
			String leafLabel = getMajorityLabel(target, instances);
			TreeNode leaf = new TreeNode(leafLabel);
			return leaf;
			
		}else {
			for (String valueName : valueSubsets.keySet()) {
				ArrayList<Instance> subset = valueSubsets.get(valueName);
				if (subset.size() == 0) {
					String leafLabel = getMajorityLabel(target, instances);
					TreeNode leaf = new TreeNode(leafLabel);
					root.addChild(valueName, leaf);
				} else {
					TreeNode child = constructTree(target, attributes, subset);
					root.addChild(valueName, child);
				}			
			}	
		}
		
			
		
		// Remember to add it again!
		attributes.add(rootAttr);
		
		return root;
	}
	
	/**
	 * Get the majority target class label from instances
	 * @param target
	 * @param instances
	 * @return String
	 * @throws IOException
	 */
	public String getMajorityLabel(Attribute target, ArrayList<Instance> instances) throws IOException {
		ArrayList<String> valuesOfTarget = target.getValues();
		String targetName = target.getName();
		HashMap<String, Integer> countValueOfTarget = new HashMap<String, Integer>();
		for (String s : valuesOfTarget) {
			countValueOfTarget.put(s, 0);
		}
		for (Instance instance : instances) {
			HashMap<String, String> attributeValuePairsOfInstance = instance.getAttributeValuePairs();
			String valueOfInstanceAtTarget = attributeValuePairsOfInstance.get(targetName);
			if (!countValueOfTarget.containsKey(valueOfInstanceAtTarget)) 
				throw new IOException("Invalid input data");
			countValueOfTarget.put(valueOfInstanceAtTarget, 
					countValueOfTarget.get(valueOfInstanceAtTarget) + 1);
		}
		String maxLabel = "";
		int maxCount = 0;
		for (String s : countValueOfTarget.keySet()) {
			int currCount = countValueOfTarget.get(s);
			if (currCount > maxCount) {
				maxCount = currCount;
				maxLabel = s;
			}
		}
		return maxLabel;
	}
}