/** 
 * This class is used to construct tree
 */
package core;

import definition.Attribute;
import definition.Instance;
import input.ProcessInputData;
import node.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstructTree {
	protected ArrayList<Attribute> attributes;
	protected ArrayList<Instance> instances;
	protected Attribute target;
	int count = 0; 
	private int max_depth = 3;
	private String isPruned = "True";
	private String featureTable = "";
	
	
	public ConstructTree(String fileName, String targetAttr, String isPruned, int max_depth) throws IOException {
		ProcessInputData input = new ProcessInputData(fileName, targetAttr);
		this.attributes = input.getAttributeSet();
		this.instances = input.getInstanceSet();
		this.target = input.getTargetAttribute();
	}

	
	public ConstructTree(ArrayList<Instance> instances, ArrayList<Attribute> attributes, Attribute target, String isPruned, int max_depth) {
		this.instances = instances;
		this.attributes = attributes;
		this.target = target;
		this.setIsPruned(isPruned);
		this.setMax_depth(max_depth);
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
		
		/*
		 *  Stop when (1) entropy is zero
		 *  (2) no attribute left
		 */
		
		if (Entropy.calculate(target, instances) == 0 || attributes.size() == 0) {
			String leafLabel = "";
			if (Entropy.calculate(target, instances) == 0) {
				leafLabel = instances.get(0).getAttributeValuePairs().get(target.getName());
			} else {
				leafLabel = getMajorityLabel(target, instances);
			}
			TreeNode leaf = new TreeNode(leafLabel);

			return leaf;
		}
		
		
		// Choose the root attribute
		ChooseAttribute choose = new ChooseAttribute(target, attributes, instances);
		Attribute rootAttr = choose.getChosen();
		if(rootAttr!=null)
		{
			String feature = rootAttr.toString() + "; InfoGainValue: " + choose.getInfoGain();
			featureTable = featureTable + "|" + feature;	
		}
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
				if(getIsPruned().equals("True"))
				{
					if (count < this.getMax_depth()) {
						count += 1;
						TreeNode child = constructTree(target, attributes, subset);
						root.addChild(valueName, child);
					}
					else {
						String leafLabel = getMajorityLabel(target, instances);
						TreeNode leaf = new TreeNode(leafLabel);
						root.addChild(valueName, leaf);
					}
				}
				else
				{
					if (subset.size() == 0) 
					{
						String leafLabel = getMajorityLabel(target, instances);
						TreeNode leaf = new TreeNode(leafLabel);
						root.addChild(valueName, leaf);
					} else {
						TreeNode child = constructTree(target, attributes, subset);
						root.addChild(valueName, child);
					}
				}		
			}	
		}
		
		
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
	
	public String getFeatureTable()
	{
		return featureTable;
	}

	public int getMax_depth() {
		return max_depth;
	}


	public void setMax_depth(int max_depth) {
		this.max_depth = max_depth;
	}


	public String getIsPruned() {
		return isPruned;
	}


	public void setIsPruned(String isPruned) {
		this.isPruned = isPruned;
	}
}