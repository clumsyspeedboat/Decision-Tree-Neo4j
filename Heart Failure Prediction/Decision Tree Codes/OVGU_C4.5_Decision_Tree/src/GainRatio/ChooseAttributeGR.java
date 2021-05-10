/**
 * This class is used to choose the attribute based on information gain
 */

package GainRatio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import C45CoreAlgorithm.*;

import DataDefination.Attribute;
import DataDefination.Instance;
import ProcessInput.ProcessInputData;

public class ChooseAttributeGR extends ChooseAttribute{
	
	
	  private Attribute chosen; 
	  private HashMap<String, ArrayList<Instance>>subset; 
	  private double infoGain, GainRatio; 
	  private double threshold, threshold1;
	  
	  
	  public ChooseAttributeGR() { 
		  chosen = new Attribute(); infoGain = -1;
		  GainRatio = -1; subset = new HashMap<String, ArrayList<Instance>>();
	  }
	 
	
	
	/**
	 * Constructor: initialize fields
	 * @param target
	 * @param attributes
	 * @param instances
	 * @throws IOException
	 */
	public ChooseAttributeGR(Attribute target, ArrayList<Attribute> attributes, 
			ArrayList<Instance> instances) throws IOException {
		super(target,attributes,instances);
		
		// Initialize variables
		chosen = null;
		infoGain = -1;
		subset = null;
		
		// Iterate to find the attribute with the largest information gain
		for (Attribute currAttribute : attributes) {
			double currInfoGain,currsplitinfo,currInfoGain1, CurrGainRatio  = 0, GainRation=0;
			HashMap<String, ArrayList<Instance>> currSubset = null;
			
			if (currAttribute.getType().equals("continuous")) {
			InfoGainContinuous contigous = new InfoGainContinuous(currAttribute, target, instances);
			currInfoGain = contigous.getInfoGain();
			currSubset = contigous.getSubset();
			threshold = contigous.getThreshold();
				
			SplitInfoContinuous contigous1 = new SplitInfoContinuous(currAttribute, target, instances);
			currsplitinfo = contigous1.getSplitInfo();
			currSubset = contigous1.getSubset();
			double threshold1 = contigous1.getThreshold();
//			calculating Gain ratio
			CurrGainRatio = currInfoGain / currsplitinfo ;
			} else {
				SplitInfoDiscrete discrete1 = new SplitInfoDiscrete(target, currAttribute, instances);
				currsplitinfo = discrete1.getSplitInfo();
				currSubset = discrete1.getSubset();
				
				InfoGainDiscrete discrete = new InfoGainDiscrete(target, currAttribute, instances);
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
