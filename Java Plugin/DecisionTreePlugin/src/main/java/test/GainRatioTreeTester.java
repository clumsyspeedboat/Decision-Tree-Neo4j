package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import core.InfoGainDiscrete;
import definition.Attribute;
import definition.Instance;
import gainratio.ChooseAttributeGR;
import gainratio.ConstructTreeGR;
import gainratio.GainRatioContinuous;
import gainratio.GainRatioDiscrete;
import gini.ChooseAttributeGI;
import gini.ConstructTreeGI;
import node.TreeNode;
import output.PrintTree;

/**
 * This class is to perform unit testing for gain ratio calculation of decision tree 
 * The calculated values are compared with this blog 
 * Link - https://sefiks.com/2018/05/13/a-step-by-step-c4-5-decision-tree-example/
 * 
 * @author minh dung, nasim
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
				add(new Attribute("Temp", "real"));
				add(new Attribute("Humidity", "real"));
				add(new Attribute("Wind", "{Weak,Strong}"));
			}
		};
		return attList;
	}

	public Attribute createTarget() throws IOException {
		Attribute target = new Attribute("Decision", "{No,Yes}");
		return target;
	}
	
	
	public void testChoosAttributeGI() throws IOException {
		ArrayList<Instance> instances = createInstances();
		Attribute target = createTarget();
		ArrayList<Attribute> attList = createAttributes();
		
		ChooseAttributeGR choose = new ChooseAttributeGR(target, attList, instances);
		
		Attribute selectedAtt = choose.getChosen();
		//System.out.println(selectedAtt);
		assertEquals("Outlook", selectedAtt.getName());
	}
	
	
	public void testCreateTree() throws IOException {
		ArrayList<Instance> instances = createInstances();
		Attribute target = createTarget();
		
		ArrayList<Attribute> attList = createAttributes();
		
		
		ConstructTreeGR tree = new ConstructTreeGR(instances, attList, target, "False", 0);
		TreeNode root = tree.construct();
	    
		PrintTree p = new PrintTree();
		//System.out.println(p.printDFS(root));
	}
	
	@Test
	public void testDiscrete() throws IOException
	{
		// Test for wind attribute
		ArrayList<Instance> instances = createInstances();
		Attribute target = createTarget();
		Attribute discreteAttribute = createAttributes().get(3);
		
		GainRatioDiscrete grDiscrete = new GainRatioDiscrete(target, discreteAttribute, instances);
		
		double gainVal = grDiscrete.getGainRatio();

		//InfoGainDiscrete discrete = new InfoGainDiscrete(target, discreteAttribute, instances);
		//double currInfoGain = discrete.getInfoGain();
		
		//double currGainRatio = (double) currInfoGain / (double) sInfoVal;
		double calVal = (double) Math.round(gainVal * 1000d) / 1000d;
		
		System.out.println(calVal);
		
		
		double originalGR = 0.049;
		
		assertTrue("Not equals", originalGR - calVal == 0);
	}
	
	
	@Test
	public void testContinuous() throws IOException{
		ArrayList<Instance> instances = createInstances();
		Attribute target = createTarget();
		Attribute conAttribute = createAttributes().get(2);
		
		GainRatioContinuous sInfo = new GainRatioContinuous(conAttribute, target, instances);
		double currGainRatio = sInfo.getGainRatio();
		//System.out.println(currGainRatio);
	}

}
