package JunitTesting;


import C45CoreAlgorithm.ChooseAttribute;
import DataDefination.Attribute;
import DataDefination.Instance;
import ProcessInput.ProcessInputData;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;


public class PractiseTest {


	public ArrayList<Attribute> attributeSet;
	public ArrayList<Instance> instanceSet;
	public Attribute targetAttribute;
    public Attribute igAttr;
	
	@Test
	public void test() throws IOException {
				 
		ProcessInputData input = new ProcessInputData("C:/Users/49171/Desktop/Graph-Database-Learning-Algorithms-Neo4j-/"
				+ "Heart Failure Prediction/Decision Tree Codes/OVGU_C4.5_Decision_Tree/data/junit_test.csv");
		this.attributeSet = input.getAttributeSet();
		this.instanceSet = input.getInstanceSet();
		this.targetAttribute = input.getTargetAttribute();
		
		System.out.println(this.attributeSet);
		System.out.println(this.instanceSet);
		System.out.println(this.targetAttribute);
		
		String firstAttr = "id"; 

		assertEquals( firstAttr , attributeSet.get(0).getName());
		System.out.println(instanceSet.size());
	}
	

	@Test
	public void test1() throws IOException {
		
		ChooseAttribute attr = new ChooseAttribute(targetAttribute, attributeSet, instanceSet);
		this.igAttr = attr.getChosen();
		
		System.out.println(this.igAttr.getName());
		String s = "var_1";

		
		assertEquals( s , igAttr.getName());
	}

}
