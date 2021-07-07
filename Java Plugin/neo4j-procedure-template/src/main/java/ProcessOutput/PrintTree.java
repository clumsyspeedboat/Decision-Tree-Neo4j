/**
 * This class is used to print the Tree
 */

package ProcessOutput;

import TreeDefination.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import MineData.C45MineData;




public class PrintTree{
	private String pic; 
	public ArrayList<ArrayList<String>> nodesBucket;
	public ArrayList<ArrayList<String>> relationshipsBucket;
	
	
	
	
	/**
	 * This method create nodes for the graph in Neo4j
	 * @param root
	 */
	
	public void createNodesForGraph(TreeNode root){
		nodesBucket = new ArrayList<ArrayList<String>>();
		relationshipsBucket = new ArrayList<ArrayList<String>>();
				
		root.setParentLevel(0);
		root.setCurrentLevel(0);
		//root.setParentAttribute(null);
		root.setIndex(0);
		
		setLevelIndex(root);
		
		createNodeData(root, "");
		createRelationshipData(root, "");
	}


	/**
	 * This method print out each path of the tree from root to leaf.
	 * @param root
	 * @return ArrayList<String>
	 */
	public ArrayList<String> printDFS(TreeNode root) {
		
		ArrayList<String> res = new ArrayList<String>();
		printDFS(root, new StringBuilder(), res);
		return res;
	}
	
	
	/**
	 * This method set the level and index of the individual nodes of decision tree
	 * @param root
	 */
	public static void setLevelIndex(TreeNode root) {
		HashMap<String, TreeNode> children = root.getChildren();
		if(children != null) {
			int index = 0; 
			for(String key: children.keySet()) {
				TreeNode childAttr = children.get(key);
				childAttr.setCurrentLevel(root.getParentLevel()+1);
				childAttr.setParentLevel(childAttr.getCurrentLevel());
				childAttr.setIndex(index);
				
				childAttr.setParentAttribute(root.getAttribute());
				
				setLevelIndex(childAttr);
				index++;
			}
		}
	}
	

	private void printDFS(TreeNode root, StringBuilder sb, ArrayList<String> res) {
		
		if (root.getType().equals("leaf")) {
			StringBuilder curr = new StringBuilder(sb);
			curr.append(root.getTargetLabel());
			curr.append("\n");
			res.add(curr.toString());
			
		} else {
			String rootAtt = root.getAttribute().getName();
			sb.append(rootAtt);
			
			
			HashMap<String, TreeNode> children = root.getChildren();
			for (String valueName : children.keySet()) {
				StringBuilder curr = new StringBuilder(sb);
				curr.append("(");
				curr.append(valueName);
				curr.append(")");
				printDFS(children.get(valueName), curr, res);
			}
		}	
	}
	
	
	
	public void createRelationshipData(TreeNode root, String tmp) {
		ArrayList<String> temp = new ArrayList<String>();
		
		
		if(root.getType().equals("leaf")) {
			
		
			
			
			if(root.getParentAttribute() != null) {
				temp.add(root.getParentAttribute().getName());
				temp.add(Integer.toString(root.getCurrentLevel()-1));
				
				temp.add(root.getTargetLabel());
				temp.add(Integer.toString(root.getCurrentLevel()));
				temp.add(tmp);
				int ind = root.getIndex();
				if(ind == 0) {
					temp.add("Right");
				}else {
					temp.add("Left");
				}
				
				
				relationshipsBucket.add(temp);
			}

			
		} else {
			String rootAtt = root.getAttribute().getName();

			
			if(root.getParentAttribute() != null) {
				temp.add(root.getParentAttribute().getName());
				temp.add(Integer.toString(root.getCurrentLevel()-1));
				
				temp.add(rootAtt);
				temp.add(Integer.toString(root.getCurrentLevel()));
				temp.add(tmp);
				int ind = root.getIndex();
				if(ind == 0) {
					temp.add("Right");
				}else {
					temp.add("Left");
				}
				relationshipsBucket.add(temp);
			}
			
			
			HashMap<String, TreeNode> children = root.getChildren();
			
			for (String valueName : children.keySet()) {
			   tmp = valueName;
			   createRelationshipData(children.get(valueName), tmp);
				
				
			}
			
		}
	}
	
	

	
	private void createNodeData(TreeNode root, String tmp){
		TreeNode node = root;
		
		if(node.getType().equals("leaf")) {
			ArrayList<String> leafNodeDetail = new ArrayList<String>();
			String leafLabel = node.getTargetLabel();
			
			leafNodeDetail.add("a");
			leafNodeDetail.add(":DT:Terminal");
			leafNodeDetail.add(leafLabel);
			
			leafNodeDetail.add(Integer.toString(node.getCurrentLevel()));
			//add index
			leafNodeDetail.add(Integer.toString(node.getIndex()));
			leafNodeDetail.add(tmp);
			
			nodesBucket.add(leafNodeDetail);
		
		}else{
			ArrayList<String> rootNodeDetail = new ArrayList<String>();
			String rootName = node.getAttribute().getName();
			
			rootNodeDetail.add("a");
			rootNodeDetail.add(":DT:Split");
			
			rootNodeDetail.add(rootName);
			//add level
			rootNodeDetail.add(Integer.toString(node.getCurrentLevel()));
			//add index
			rootNodeDetail.add(Integer.toString(node.getIndex()));
			rootNodeDetail.add(tmp);
			
			nodesBucket.add(rootNodeDetail);
			
			
			HashMap<String, TreeNode> children = node.getChildren();
			
			for(String valueName: children.keySet()) {
				tmp = valueName;
				TreeNode i = children.get(valueName);
				
				createNodeData(i, tmp);
				
			}
			
		}
	} 
	
	
	@Override
	public String toString() {
		return pic;
	}
	
	public static void main(String[] args) {
		
	}
}