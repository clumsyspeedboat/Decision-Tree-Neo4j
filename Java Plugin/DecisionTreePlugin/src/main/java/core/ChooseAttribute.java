/**
 * This class is used to choose the attribute based on information gain
 */

package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import definition.Attribute;
import definition.Instance;

public class ChooseAttribute {
	
	public Attribute chosen;
	public HashMap<String, ArrayList<Instance>> subset;
	protected double infoGain;
	protected double threshold;
	
	
	
	public ChooseAttribute() {
		chosen = new Attribute();
		infoGain = -1;
		subset = new HashMap<String, ArrayList<Instance>>();
	}
	
	
	/**
	 * Constructor: initialize fields
	 * @param target
	 * @param attributes
	 * @param instances
	 * @throws IOException
	 */
	public ChooseAttribute(Attribute target, ArrayList<Attribute> attributes, 
			ArrayList<Instance> instances) throws IOException {
		
		// Initialize variables
		chosen = null;
		infoGain = -1;
		subset = null;
		
		// Iterate to find the attribute with the largest information gain
		for (Attribute currAttribute : attributes) {
			double currInfoGain = 0;
			HashMap<String, ArrayList<Instance>> currSubset = null;
			
			if (currAttribute.getType().equals("continuous")) {
				InfoGainContinuous continuous = new InfoGainContinuous(currAttribute, target, instances);
				currInfoGain = continuous.getInfoGain();
				currSubset = continuous.getSubset();
				threshold = continuous.getThreshold();
			} else {
				InfoGainDiscrete discrete = new InfoGainDiscrete(target, currAttribute, instances);
				currInfoGain = discrete.getInfoGain();
				currSubset = discrete.getSubset();
			}
			if (currInfoGain > infoGain) {
				infoGain = currInfoGain;
				chosen = currAttribute;
				subset = currSubset;
			}
		}
	}
	

	
	
	public Attribute getChosen() {
		return chosen;
	}
	
	public double getInfoGain() {
		return infoGain;
	}
	
	public HashMap<String, ArrayList<Instance>> getSubset() {
		return subset;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public String toString() {
		return "Chosen attribute: " + chosen + "\n" + "InfoGain: " + infoGain + "\n"
				+ "Subset: " + subset;
	}

}
