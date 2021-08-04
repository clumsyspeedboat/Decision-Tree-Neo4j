package gainratio;

import definition.Attribute;
import definition.Instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import core.*;

public class SplitInfoContinuous extends InfoGainContinuous {
	
	private Attribute attribute;
	private double threshold;
	private double splitinfo = -1;
	private HashMap<String, ArrayList<Instance>> subset;
	
	/**
	 * Constructor: initialize fields. This class is for calculating the splitinformation
	 * of continuous attribute. 
	 * Use one cut to binary method. 
	 * @param attribute
	 * @param target
	 * @param instances
	 * @throws IOException
	 */
	public SplitInfoContinuous(Attribute attribute, Attribute target, 
			ArrayList<Instance> instances) throws IOException {
			super(attribute,target,instances);
		
	}
	
	public static double calculateConti(Attribute attribute, Attribute target, 
			ArrayList<Instance> instances, int index) throws IOException {
		
		int totalN = instances.size();
		double splitinfo = 0;
		int subL = index + 1;
		int subR = instances.size() - index - 1;
		double subResL = ((double) subL) / ((double) totalN) ;
		splitinfo -= (double)subResL *(Math.log(subResL));
		double subResR = ((double) subR) / ((double) totalN);
		splitinfo -= (double)subResR *(Math.log(subResR));
		return splitinfo;
	}
	
	
	public Attribute getAttribute() {
		return attribute;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public double getSplitInfo() {
		return splitinfo;
	}
	
	public HashMap<String, ArrayList<Instance>> getSubset() {
		return subset;
	}
	
	public String toString() {
		return "Attribute: " + attribute.getName() + "\n" + "Threshold: " + threshold + "\n" 
				+ "splitinfo: " + splitinfo + "\n" + "Subset: " + subset;
	}
}