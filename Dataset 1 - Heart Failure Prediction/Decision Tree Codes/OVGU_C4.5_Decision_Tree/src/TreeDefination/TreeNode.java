/**
 * This class defines TreeNode type of the decision tree,
 * including two types node, root and leaf.
 */


package TreeDefination;

import DataDefination.Attribute;

import java.util.HashMap;

public class TreeNode {
	private String type;
	private Attribute attribute;
	private HashMap<String, TreeNode> children;
	
	private String targetLabel;
	
	private int parentLevel;
	private int currentLevel;
	
	/*
	 * private enum Index{ LeftSubTree(0), RightSubTree(1);
	 * 
	 * private final int value;
	 * 
	 * Index(final int newValue) { value = newValue; } public int getValue() {
	 * return value;} };
	 */
	
	private int index;
	

	public TreeNode(Attribute attribute) {
		type = "root";
		this.attribute = attribute;
		
		children = new HashMap<String, TreeNode>();
	}
	
	public TreeNode(String targetLabel) {
		type = "leaf";
		this.targetLabel = targetLabel;
	}
	
	public Attribute getAttribute() {
		return attribute;
	}
	
	public void addChild(String valueName, TreeNode child) {
		children.put(valueName, child);
	}
	public HashMap<String, TreeNode> getChildren() {
		return children;
	}
	
	public String getTargetLabel() {
		return targetLabel;
	}
	
	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getParentLevel() {
		return parentLevel;
	}

	public void setParentLevel(int parentLevel) {
		this.parentLevel = parentLevel;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	
	
	@Override
	public String toString() {
		if (type.equals("root")) return "Root attribute: " + attribute.getName() + "; Children: " + children;
		else return "Leaf label: " + targetLabel;
	}
	
	public static void main(String[] args) {
		//int val = TreeNode.Index.RightSubTree.getValue();
		//System.out.println(val);
	}

	
}