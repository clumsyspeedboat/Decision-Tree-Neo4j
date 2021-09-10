package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import definition.Attribute;
import definition.Instance;
import gainratio.ChooseAttributeGR;
import gainratio.ConstructTreeGR;
import gainratio.SplitInfoDiscrete;
import gini.ChooseAttributeGI;
import gini.ConstructTreeGI;
import node.TreeNode;
import output.PrintTree;

/**
 * This class is to perform unit testing for gain ratio calculation of decision tree 
 * The calculated values are compared with this blog 
 * Link - https://sefiks.com/2018/05/13/a-step-by-step-c4-5-decision-tree-example/
 * 
 * @author minh dung
 *
 */

public class GainRatioTreeTester {
	
	public ArrayList<Instance> createInstances() throws IOException {
		String[] attributes = { "Outlook", "Temp", "Humidity", "Wind", "Decision" };

		@SuppressWarnings("serial")
		ArrayList<String[]> al = new ArrayList<String[]>() {
			{
				add(new String[] { "Sunny", "85", "85", "Weak", "No" });
				add(new String[] { "Sunny", "80", "90", "Strong", "No" });
				add(new String[] { "Overcast", "83", "78", "Weak", "Yes" });
				add(new String[] { "Rain", "70", "96", "Weak", "Yes" });
				add(new String[] { "Rain", "68", "80", "Weak", "Yes" });
				add(new String[] { "Rain", "65", "70", "Strong", "No" });
				add(new String[] { "Overcast", "64", "65", "Strong", "Yes" });
				add(new String[] { "Sunny", "72", "95", "Weak", "No" });
				add(new String[] { "Sunny", "69", "70", "Weak", "Yes" });
				add(new String[] { "Rain", "75", "80", "Weak", "Yes" });
				add(new String[] { "Sunny", "75", "70", "Strong", "Yes" });
				add(new String[] { "Overcast", "72", "90", "Strong", "Yes" });
				add(new String[] { "Overcast", "81", "75", "Weak", "Yes" });
				add(new String[] { "Rain", "71", "80", "Strong", "No" });
//				add(new String[] { "Sunny", "Hot", "High", "Weak", "No" });
//				add(new String[] { "Sunny", "Hot", "High", "Strong", "No" });
//				add(new String[] { "Overcast", "Hot", "High", "Weak", "Yes" });
//				add(new String[] { "Rain", "Mild", "High", "Weak", "Yes" });
//				add(new String[] { "Rain", "Cool", "Normal", "Weak", "Yes" });
//				add(new String[] { "Rain", "Cool", "Normal", "Strong", "No" });
//				add(new String[] { "Overcast", "Cool", "Normal", "Strong", "Yes" });
//				add(new String[] { "Sunny", "Mild", "High", "Weak", "No" });
//				add(new String[] { "Sunny", "Cool", "Normal", "Weak", "Yes" });
//				add(new String[] { "Rain", "Mild", "Normal", "Weak", "Yes" });
//				add(new String[] { "Sunny", "Mild", "Normal", "Strong", "Yes" });
//				add(new String[] { "Overcast", "Mild", "High", "Strong", "Yes" });
//				add(new String[] { "Overcast", "Hot", "Normal", "Weak", "Yes" });
//				add(new String[] { "Rain", "Mild", "High", "Strong", "No" });
			}
		};

		ArrayList<Instance> instanceSet = new ArrayList<>();
		Iterator<String[]> iter = al.iterator();
		while (iter.hasNext()) {
			Instance item = new Instance();
			String[] a = iter.next();
			for (int i = 0; i < a.length; i++) {
				item.addAttribute(attributes[i], a[i]);
			}
			instanceSet.add(item);
		}

		return instanceSet;
	}
	
	public ArrayList<Attribute> createAttributes() throws IOException {
		@SuppressWarnings("serial")
		ArrayList<Attribute> attList = new ArrayList<Attribute>() {
			{
				add(new Attribute("Outlook", "{Sunny,Overcast,Rain}"));
				add(new Attribute("Temp", "{64,65,68,70,71,72,75,80,81,83,85}"));
				add(new Attribute("Humidity", "{65,70,75,80,85,90,95,96}"));
				add(new Attribute("Wind", "{Weak,Strong}"));
//				add(new Attribute("Outlook", "{Sunny,Overcast,Rain}"));
//				add(new Attribute("Temp", "{Hot,Mild,Cool}"));
//				add(new Attribute("Humidity", "{High,Normal}"));
//				add(new Attribute("Wind", "{Weak,Strong}"));
			}
		};
		return attList;

	}

	public Attribute createTarget() throws IOException {
		Attribute target = new Attribute("Decision", "{No,Yes}");
		return target;
	}
	
	@Test
	public void testChoosAttributeGI() throws IOException {
		ArrayList<Instance> instances = createInstances();
		Attribute target = createTarget();
		ArrayList<Attribute> attList = createAttributes();
		
		ChooseAttributeGR choose = new ChooseAttributeGR(target, attList, instances);
		
		Attribute selectedAtt = choose.getChosen();
		//System.out.println(selectedAtt);
		System.out.println(selectedAtt.getName());
		assertEquals("Outlook", selectedAtt.getName());
	}
	
	@Test
	public void testCreateTree() throws IOException {
		ArrayList<Instance> instances = createInstances();
		Attribute target = createTarget();
		ArrayList<Attribute> attList = createAttributes();
		
		ConstructTreeGR tree = new ConstructTreeGR(instances, attList, target);
		TreeNode root = tree.construct();
	    
		PrintTree p = new PrintTree();
		;
		System.out.println(p.printDFS(root));
	}
	
	@Test
	public void testDiscrete() throws IOException
	{
		ArrayList<Instance> instances = createInstances();
		Attribute target = createTarget();
		Attribute discreteAttribute = new Attribute("Outlook", "{Sunny,Overcast,Rain}");
		SplitInfoDiscrete splitInfo = new SplitInfoDiscrete(target, discreteAttribute, instances);
		double discreteInfo = splitInfo.getInfoGain();
		System.out.println(discreteInfo);
	}
}
