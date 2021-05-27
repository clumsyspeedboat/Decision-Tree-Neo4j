package JunitTesting;

import C45CoreAlgorithm.ChooseAttribute;
import DataDefination.Attribute;
import DataDefination.Instance;
import ProcessInput.ProcessInputData;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Test;

public class PractiseTest {


	public ArrayList<Attribute> attributeSet;
	public ArrayList<Instance> instanceSet;
	public Attribute targetAttribute;
	
	@Test
	public void testProcessInputData() throws IOException {
				 
		ProcessInputData input = new ProcessInputData("C:/Users/49171/Desktop/Graph-Database-Learning-Algorithms-Neo4j-/"
				+ "Heart Failure Prediction/Decision Tree Codes/OVGU_C4.5_Decision_Tree/data/junit_test.csv");
		this.attributeSet = input.getAttributeSet();
		this.instanceSet = input.getInstanceSet();
		this.targetAttribute = input.getTargetAttribute();
		
		System.out.println(this.attributeSet);
		System.out.println(this.instanceSet);
		System.out.println(this.targetAttribute);
		
		String firstAttr = "id"; 
		String secondAttr = "var_1";
		String thirdAttr = "var_3";
		String fourthAttr = "var_4";

		assertEquals( firstAttr , attributeSet.get(0).getName());
		assertEquals( secondAttr , attributeSet.get(1).getName());
		assertEquals( thirdAttr , attributeSet.get(2).getName());
		assertEquals( fourthAttr , attributeSet.get(3).getName());
		
		assertEquals( 8 , instanceSet.size());
		 
		
	}
	

	@Test
	public void testChooseAttribute() throws IOException {
		
		Attribute igAttr;
		
		ChooseAttribute attr = new ChooseAttribute(targetAttribute, attributeSet, instanceSet);
		igAttr = attr.getChosen();
		
		System.out.println(igAttr.getName());
		String s = "var_3";

		
		assertEquals( s , igAttr);
	}

}
