package gainratio;

import definition.Attribute;
import definition.Instance;
import core.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * To calculate the split info of attribute 
 * @author nasim
 *
 */

public class GainRatioDiscrete{
	
	private Attribute attribute;
	private double gainRatio;
	private HashMap<String, ArrayList<Instance>> subset;
	
	/**
	 * Constructor: initialize fields. This class is for calculating the Splitinfo for
	 * discrete attribute.
	 * @param target
	 * @param attribute
	 * @param instances
	 * @throws IOException
	 **/
	public GainRatioDiscrete(Attribute target, Attribute attribute, ArrayList<Instance> instances)
			throws IOException {

		this.attribute = attribute;
		
		ArrayList<String> valuesOfAttribute = attribute.getValues();
		
		String attributeName = attribute.getName();
		
		

		subset = new HashMap<String, ArrayList<Instance>>();
		
		for (String s : valuesOfAttribute) {
			subset.put(s, new ArrayList<Instance>());
		}
		
	    
		for (Instance instance : instances) {
			HashMap<String, String> attributeValuePairsOfInstance = instance.getAttributeValuePairs();
			
			String valueOfInstanceAtAttribute = attributeValuePairsOfInstance.get(attributeName);

			if (!subset.containsKey(valueOfInstanceAtAttribute)) 
				throw new IOException("Invalid input data");
			subset.get(valueOfInstanceAtAttribute).add(instance);
		}

		int totalN = instances.size();
		
		double splitInfo = 0;
		
		
		double infoGain = Entropy.calculate(target, instances);
		

		//calculate gain ratio
		for (String s : subset.keySet()) {
			ArrayList<Instance> currSubset = subset.get(s);
			
			int subN = currSubset.size();
					
			double subRes = ((double) subN) / ((double) totalN);
			splitInfo -= (double) subRes * ((double)(Math.log(subRes) / Math.log(2)));
			
			double subResInfo = ((double) subN) / ((double) totalN) * Entropy.calculate(target, currSubset);
			infoGain -= subResInfo;
		}
		
		gainRatio = infoGain/splitInfo;
		
	}
	

	
	public Attribute getAttribute() {
		return attribute;
	}
	
	public double getGainRatio() {
		return gainRatio;
	}
	
	public HashMap<String, ArrayList<Instance>> getSubset() {
		return subset;
	}
}