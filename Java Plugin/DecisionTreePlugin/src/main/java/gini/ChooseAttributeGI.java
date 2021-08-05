package gini;

import core.ChooseAttribute;
import definition.Attribute;
import definition.Instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChooseAttributeGI extends ChooseAttribute{
	
	private Attribute chosen;
	private HashMap<String, ArrayList<Instance>> subset;
	private double probabilities;
	private double threshold;
	
	
	public ChooseAttributeGI() {
		chosen = new Attribute();
		probabilities = Double.POSITIVE_INFINITY;
		subset = new HashMap<String, ArrayList<Instance>>();
	}
	
	
	/**
	 * Constructor: initialize fields
	 * @param target
	 * @param attributes
	 * @param instances
	 * @throws IOException
	 */
	public ChooseAttributeGI(Attribute target, ArrayList<Attribute> attributes, 
			ArrayList<Instance> instances) throws IOException {
		super(target,attributes,instances);
		
		// Initialize variables
		chosen = null;
		probabilities = Double.POSITIVE_INFINITY;
		subset = null;
		
		
		// Iterate to find the attribute with the largest information gain
		for (Attribute currAttribute : attributes) {
			double currProbability = 0;
			HashMap<String, ArrayList<Instance>> currSubset = null;
			
			if (currAttribute.getType().equals("continuous")) {
				ContinuousProbability continuous = new ContinuousProbability(currAttribute, target, instances);
				currProbability = continuous.getGiniValue();
				currSubset = continuous.getSubset();
				threshold = continuous.getThreshold();
			} else {
				DiscreteProbability discrete = new DiscreteProbability(target, currAttribute, instances);
				currProbability = discrete.getGiniValue();
				currSubset = discrete.getSubset();
			}
			if (currProbability < probabilities) {
				probabilities = currProbability;
				chosen = currAttribute;
				subset = currSubset;
			}
		}
	}
	
	public Attribute getChosen() {
		return chosen;
	}
	
	public double getInfoGain() {
		return probabilities;
	}
	
	public HashMap<String, ArrayList<Instance>> getSubset() {
		return subset;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	

}
