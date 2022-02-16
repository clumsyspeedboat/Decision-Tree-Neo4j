/**
 * This class is used to print the Tree
 */

package output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import node.TreeNode;



public class PrintTree{ 
	public ArrayList<ArrayList<String>> nodesBucket;
	public ArrayList<ArrayList<String>> relationshipsBucket;
	private Map<String, Integer> nodeNames; 
	private Map<String, Integer> relationNames; 
	
	
	/**
	 * This method create nodes for the graph in Neo4j
	 * @param root
	 */
	public void createNodesForGraph(TreeNode root){
		if(root != null) {
			nodesBucket = new ArrayList<ArrayList<String>>();
			relationshipsBucket = new ArrayList<ArrayList<String>>();
			nodeNames = new HashMap<String, Integer>();
			relationNames = new HashMap<String, Integer>();
					
			root.setParentLevel(0);
			root.setCurrentLevel(0);
			root.setIndex(0);
			
			setLevelIndex(root);
			
			createNodeData(root, "");
			createRelationshipData(root, "");
		}else {
			System.out.println("Tree is null");
		}
		
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
	
	/**
	 * This method is to print the tree 
	 * @param root TreeNode the root node of the tree
	 * @param sb StringBuilder 
	 * @param res ArrayList<String>
	 */
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
	
	/**
	 * This method is to record the relationship data into relationshipsBucket
	 * @param root TreeNode the node of the tree
	 * @param tmp tmp String the value of the edge/relationship
	 */
	public void createRelationshipData(TreeNode root, String tmp) {
		ArrayList<String> temp = new ArrayList<String>();
		
		
		if(root.getType().equals("leaf")) {
			String tl = root.getTargetLabel();
			addToNodeNames(relationNames, tl);
			
			if(root.getParentAttribute() != null) {
				String p = root.getParentAttribute().getName();
				
				//add first node
				temp.add(p);
				temp.add(Integer.toString(root.getCurrentLevel()-1));
				temp.add(Integer.toString(relationNames.get(p)));
				
				//add second node
				temp.add(tl);
				temp.add(Integer.toString(root.getCurrentLevel()));
				temp.add(Integer.toString(relationNames.get(tl)));
				
				//edge value
				if (tmp.contains("."))
					temp.add(tmp.replace(".", "_"));
					//tmp = tmp.split(".")[0] + "_" + tmp.split(".")[1];
				temp.add(tmp);
				
				//add direction left or right
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
			addToNodeNames(relationNames, rootAtt);
			
			if(root.getParentAttribute() != null) {
				
				String parent = root.getParentAttribute().getName();
			
				
				temp.add(parent);
				temp.add(Integer.toString(root.getCurrentLevel()-1));
				temp.add(Integer.toString(relationNames.get(parent)));
				
				
				temp.add(rootAtt);
				
				temp.add(Integer.toString(root.getCurrentLevel()));
				//add duplicates
				temp.add(Integer.toString(relationNames.get(rootAtt)));
				//edge value
				if (tmp.contains("."))
					temp.add(tmp.replace(".", "_"));
					//tmp = tmp.split(".")[0] + "_" + tmp.split(".")[1];
				temp.add(tmp);
				//temp.add(tmp);
				
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
	
	
	/**
	 * This method is to record the node data into nodesBucket
	 * @param root TreeNode the node of the tree
	 * @param tmp String the value of the edge/relationship
	 */
	private void createNodeData(TreeNode root, String tmp){
		TreeNode node = root;
		
		
		if(node.getType().equals("leaf")) {
			ArrayList<String> leafNodeDetail = new ArrayList<String>();
			String leafLabel = node.getTargetLabel();
			
			addToNodeNames(nodeNames, leafLabel);
			
			leafNodeDetail.add("a:");
			//node type
			leafNodeDetail.add(":Terminal");
			leafNodeDetail.add(leafLabel);
			
			leafNodeDetail.add(Integer.toString(node.getCurrentLevel()));
			//add index
			leafNodeDetail.add(Integer.toString(node.getIndex()));
			leafNodeDetail.add(Integer.toString(nodeNames.get(leafLabel)));
			//leafNodeDetail.add(tmp);
			
			nodesBucket.add(leafNodeDetail);
		
		}else{
			
			ArrayList<String> rootNodeDetail = new ArrayList<String>();
			String rootName = node.getAttribute().getName();
	
			addToNodeNames(nodeNames, rootName);
			
		
			rootNodeDetail.add("a:");
			//node type
			rootNodeDetail.add(":Split");
			
			rootNodeDetail.add(rootName);
			//add level
			rootNodeDetail.add(Integer.toString(node.getCurrentLevel()));
			//add index
			rootNodeDetail.add(Integer.toString(node.getIndex()));
			//add duplicate
			rootNodeDetail.add(Integer.toString(nodeNames.get(rootName)));
						
			nodesBucket.add(rootNodeDetail);
			
			HashMap<String, TreeNode> children = node.getChildren();
			
			for(String valueName: children.keySet()) {
				tmp = valueName;
				TreeNode i = children.get(valueName);
				
				createNodeData(i, tmp);
				
			}
			
		}
	} 
	
	/**
	 * This method record the name of the node
	 * @param namesMap Map<String, Integer> the namesMap
	 * @param rootName String rootName
	 */
	private void addToNodeNames(Map<String, Integer> namesMap, String rootName) {
		if(namesMap.containsKey(rootName)) {
			int countNames = namesMap.get(rootName);
			namesMap.put(rootName, ++countNames);
		}else {
			namesMap.put(rootName, 0);
		}
	}
	
}