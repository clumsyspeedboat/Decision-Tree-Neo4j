//package test;
//
//import static org.junit.jupiter.api.Assertions.*;
//import core.ChooseAttribute;
//import definition.Attribute;
//import definition.Instance;
//import input.ProcessInputData;
//import node.TreeNode;
//import core.ConstructTree;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//
//import org.junit.jupiter.api.Test;
//
//class DecisionTreeTester {
//	public static final String filePath = "data/junit.csv";
//	public static final String filePath1 = "data/junit_train.csv";
//	public static final String filePath2 = "data/junit_test.csv";
//	
//	public ArrayList<Attribute> attributes = new ArrayList<Attribute>();
//	public ArrayList<Instance> instances = new ArrayList<Instance>();
//	public Attribute targets = new Attribute();
//	
//	@Test
//	public void testProcessInputData() throws IOException {
//		 
//		ProcessInputData input = new ProcessInputData(filePath);
//		attributes = input.getAttributeSet();
//		instances = input.getInstanceSet();
//		targets = input.getTargetAttribute();
//
//		assertEquals( "id" , attributes.get(0).getName());
//		assertEquals( "var_1" , attributes.get(1).getName());
//		assertEquals( "var_2" , attributes.get(2).getName());
//		assertEquals( "var_3" , attributes.get(3).getName());
//		
//		assertEquals(8,instances.size()); 		
//	}
//	
//	@Test
//	public void testChooseAttribute() throws IOException {
//
//		ProcessInputData input = new ProcessInputData(filePath);
//
//		Attribute igAttr;
//
//		ChooseAttribute attr = new ChooseAttribute(input.getTargetAttribute(), input.getAttributeSet(), input.getInstanceSet());
//		igAttr = attr.getChosen();
//
//		assertEquals("id", igAttr.getName());
//	}
//	
//	
//	
//	@Test
//	public void testConstructTree() throws IOException {
//
//		ProcessInputData input = new ProcessInputData(filePath);
//
//		ConstructTree tree = new ConstructTree(input.getInstanceSet(), input.getAttributeSet(), input.getTargetAttribute());
//
//		TreeNode nodes = tree.construct(); 
//		System.out.println(nodes);	
//
//		assertEquals("id", nodes.getAttribute().getName());
//	}
//	 
//	
//	
//	
//
//}
