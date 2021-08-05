package gainratio;

import definition.Attribute;
import definition.Instance;
import core.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class SplitInfoDiscrete extends InfoGainDiscrete{
	
	private Attribute attribute;
	private double splitinfo;
	private HashMap<String, ArrayList<Instance>> subset;
	
	/**
	 * Constructor: initialize fields. This class is for calculating the Splitinfo for
	 * discrete attribute.
	 * @param target
	 * @param attribute
	 * @param instances
	 * @throws IOException
	 */// public InfoGainDiscrete(Attribute target, Attribute attribute, ArrayList<Instance> instances
	public SplitInfoDiscrete(Attribute target, Attribute attribute, ArrayList<Instance> instances)
			throws IOException {
		super(target,attribute,instances);
		
		this.attribute = attribute;
		
		
		subset = new HashMap<String, ArrayList<Instance>>();
	
		
		int totalN = instances.size();
		splitinfo = 0;
		
		
		for (String s : subset.keySet()) {
			ArrayList<Instance> currSubset = subset.get(s);
			int subN = currSubset.size();
			double subRes = ((double) subN) / ((double) totalN);
			splitinfo -= (double) subRes * ((double)(Math.log(subRes) / Math.log(2)));

		}
		
	}
	
	public Attribute getAttribute() {
		return attribute;
	}
	
	public double getSplitInfo() {
		return splitinfo;
	}
	
	public HashMap<String, ArrayList<Instance>> getSubset() {
		return subset;
	}
}