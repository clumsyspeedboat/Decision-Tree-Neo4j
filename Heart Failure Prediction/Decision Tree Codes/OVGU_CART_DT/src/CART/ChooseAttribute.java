/**
 * This class is used to choose the attribute based on information gain
 */

package CART;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import DataDefination.Attribute;
import DataDefination.Instance;
import ProcessInput.ProcessInputData;

public class ChooseAttribute {
	
	private Attribute chosen;
	private HashMap<String, ArrayList<Instance>> subset;
	private double infoGain, GainRatio;
	private double threshold, threshold1;
	
	
	public ChooseAttribute() {
		chosen = new Attribute();
		infoGain = -1;
		GainRatio = -1;
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
			double currInfoGain,currsplitinfo,currInfoGain1, CurrGainRatio  = 0;
			HashMap<String, ArrayList<Instance>> currSubset = null;
			
			if (currAttribute.getType().equals("continuous")) {
			Continuous_Prob contigous = new Continuous_Prob(currAttribute, target, instances);
			currInfoGain = contigous.getInfoGain();
			currSubset = contigous.getSubset();
			threshold = contigous.getThreshold();
				
			splitinfo_continuous contigous1 = new splitinfo_continuous(currAttribute, target, instances);
			currsplitinfo = contigous1.getSplitInfo();
			currSubset = contigous1.getSubset();
			threshold1 = contigous1.getThreshold();
//				calculating Gain ratio
			CurrGainRatio = currInfoGain / currsplitinfo ;
			} else {
				splitinfo_discrete discrete1 = new splitinfo_discrete(target, currAttribute, instances);
				currsplitinfo = discrete1.getSplitInfo();
				currSubset = discrete1.getSubset();
				
				Discrete_Prob discrete = new Discrete_Prob(target, currAttribute, instances);
				currInfoGain = discrete.getInfoGain();
				currSubset = discrete.getSubset();
				
				CurrGainRatio = currInfoGain / currsplitinfo;
				
			}
			if (CurrGainRatio > GainRatio) {
				GainRatio = CurrGainRatio;
				chosen = currAttribute;
				subset = currSubset;
			}
		}
	}
	
	public Attribute getChosen() {
		return chosen;
	}
	
	public double getInfoGain() {
		return GainRatio;
	}
	
	public HashMap<String, ArrayList<Instance>> getSubset() {
		return subset;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public String toString() {
		return "Chosen attribute: " + chosen + "\n" + "GainRatio: " + GainRatio + "\n"
				+ "Subset: " + subset;
	}

}
