
package test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import definition.Attribute;
import definition.Instance;
import gini.ChooseAttributeGI;
import gini.ConstructTreeGI;
import gini.ContinuousProbability;
import gini.DiscreteProbability;
import node.TreeNode;
import output.PrintTree;



/**
 * This class is to perform unit testing for gini index calculation of decision tree 
 * The calculated values are compared with this blog 
 * Link - https://sefiks.com/2018/08/27/a-step-by-step-cart-decision-tree-example/
 * 
 * @author nasim
 *
 */

public class GiniTreeTester {
	
	
	public ArrayList<Instance> createInstances() throws IOException {
		String[] attributes = { "Outlook", "Temp", "Humidity", "Wind", "Decision" };

		@SuppressWarnings("serial")
		ArrayList<String[]> al = new ArrayList<String[]>() {
			{
				add(new String[] { "Sunny", "Hot", "High", "Weak", "No" });
				add(new String[] { "Sunny", "Hot", "High", "Strong", "No" });
				add(new String[] { "Overcast", "Hot", "High", "Weak", "Yes" });
				add(new String[] { "Rain", "Mild", "High", "Weak", "Yes" });
				add(new String[] { "Rain", "Cool", "Normal", "Weak", "Yes" });
				add(new String[] { "Rain", "Cool", "Normal", "Strong", "No" });
				add(new String[] { "Overcast", "Cool", "Normal", "Strong", "Yes" });
				add(new String[] { "Sunny", "Mild", "High", "Weak", "No" });
				add(new String[] { "Sunny", "Cool", "Normal", "Weak", "Yes" });
				add(new String[] { "Rain", "Mild", "Normal", "Weak", "Yes" });
				add(new String[] { "Sunny", "Mild", "Normal", "Strong", "Yes" });
				add(new String[] { "Overcast", "Mild", "High", "Strong", "Yes" });
				add(new String[] { "Overcast", "Hot", "Normal", "Weak", "Yes" });
				add(new String[] { "Rain", "Mild", "High", "Strong", "No" });
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
				add(new Attribute("Temp", "{Hot,Mild,Cool}"));
				add(new Attribute("Humidity", "{High,Normal}"));
				add(new Attribute("Wind", "{Weak,Strong}"));
			}
		};
		return attList;

	}

	public Attribute createTarget() throws IOException {
		Attribute target = new Attribute("Decision", "{No,Yes}");
		return target;
	}

	@Test
	public void testDiscreteProbability() throws IOException {
		ArrayList<Instance> instances = createInstances();
		Attribute target = createTarget();
		ArrayList<Attribute> attList = createAttributes();

		Double[] groundTruth = { 0.34, 0.44, 0.37, 0.43 };

		int index = 0;
		for (Attribute currAttribute : attList) { // Attribute currAttribute = new

			DiscreteProbability discrete = new DiscreteProbability(target, currAttribute, instances);
			double giniVal = discrete.getGiniValue();
			double calVal = (double) Math.round(giniVal * 100d) / 100d;

			double originalGini = groundTruth[index];
			//System.out.println("Original" + originalGini + ", " + "Calculated" + calVal);

			assertTrue("Not equals", originalGini - calVal == 0);
			index++;
		}
	}

	@Test
	public void testChoosAttributeGI() throws IOException {
		ArrayList<Instance> instances = createInstances();
		Attribute target = createTarget();
		ArrayList<Attribute> attList = createAttributes();
		
		ChooseAttributeGI choose = new ChooseAttributeGI(target, attList, instances);
		
		Attribute selectedAtt = choose.getChosen();
		//System.out.println(selectedAtt);
		assertEquals("Outlook", selectedAtt.getName());
	}
	
	@Test
	public void testCreateTree() throws IOException {
		ArrayList<Instance> instances = createInstances();
		Attribute target = createTarget();
		ArrayList<Attribute> attList = createAttributes();
		
		ConstructTreeGI tree = new ConstructTreeGI(instances, attList, target,"False",0);
		TreeNode root = tree.construct();
	    
		PrintTree p = new PrintTree();
		;
		System.out.println(p.printDFS(root));
	}
	
	
	public void testContinuousAttribte() throws IOException {
		String[] attributes = { "lotion", "expos", "burn"};

		@SuppressWarnings("serial")
		ArrayList<String[]> sunBurnDataset = new ArrayList<String[]>() {
			{
				add(new String[] { "A", "20", "N" });
				add(new String[] { "A", "30", "N" });
				add(new String[] { "A", "40", "Y" });
				add(new String[] { "B", "20", "Y"});
				add(new String[] { "C", "20", "N"});		
			}
		};
		
		ArrayList<Instance> instanceSet = new ArrayList<>();
		Iterator<String[]> iter = sunBurnDataset.iterator();
		while (iter.hasNext()) {
			Instance item = new Instance();
			String[] a = iter.next();
			for (int i = 0; i < a.length; i++) {
				item.addAttribute(attributes[i], a[i]);
			}
			instanceSet.add(item);
		}
		
		Attribute target = new Attribute("burn", "{N,Y}");
		
		Attribute currAttribute = new Attribute("expos", "real");
		
		ContinuousProbability continuous = new ContinuousProbability(currAttribute, target, instanceSet);
		double giniValue = continuous.getGiniValue();
		System.out.println(giniValue);	
	}


}
 

