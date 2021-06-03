package JunitTesting;

import C45CoreAlgorithm.ChooseAttribute;
import DataDefination.Attribute;
import DataDefination.Instance;
import ProcessInput.ProcessInputData;
import TreeDefination.TreeNode;
import C45CoreAlgorithm.ConstructTree;

import java.io.IOException;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

public class PractiseTest {


	public static final String filePath = "C:/Users/49171/Desktop/Graph-Database-Learning-Algorithms-Neo4j-/"
			+ "Heart Failure Prediction/Decision Tree Codes/OVGU_C4.5_Decision_Tree/data/junit_test.csv";
	
	public ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	public ArrayList<Instance> instances = new ArrayList<Instance>();
	public Attribute targets = new Attribute();
	
	
	@Test
	public void testProcessInputData() throws IOException {
				 
		ProcessInputData input = new ProcessInputData(filePath);
		attributes = input.getAttributeSet();
		instances = input.getInstanceSet();
		targets = input.getTargetAttribute();
		
		String firstAttr = "id"; 
		String secondAttr = "var_1";
		String thirdAttr = "var_2";
		String fourthAttr = "var_3";

		assertEquals( firstAttr , attributes.get(0).getName());
		assertEquals( secondAttr , attributes.get(1).getName());
		assertEquals( thirdAttr , attributes.get(2).getName());
		assertEquals( fourthAttr , attributes.get(3).getName());
		
		assertEquals( 8 , instances.size());
		 		
	}
	

	@Test
	public void testChooseAttribute() throws IOException {
		
		ProcessInputData input = new ProcessInputData(filePath);
		
		Attribute igAttr;
		
		ChooseAttribute attr = new ChooseAttribute(input.getTargetAttribute(), input.getAttributeSet(), input.getInstanceSet());
		igAttr = attr.getChosen();
		
		assertEquals( "id" , igAttr.getName());
	}
	
	
	@Test
	public void testConstructTree() throws IOException {
		
		ProcessInputData input = new ProcessInputData(filePath);
		
		ConstructTree tree = new ConstructTree(input.getInstanceSet(), input.getAttributeSet(), input.getTargetAttribute());
		
		TreeNode nodes = tree.construct();
		
		assertEquals( "id" , nodes.getAttribute().getName());
        

	}

 
}
