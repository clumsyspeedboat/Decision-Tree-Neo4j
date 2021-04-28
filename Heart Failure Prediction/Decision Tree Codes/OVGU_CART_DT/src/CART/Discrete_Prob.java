/**
 * Calculates the information gain of discrete attribute
 */


package CART;

import DataDefination.Attribute;
import DataDefination.Instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Discrete_Prob {
	
	private Attribute attribute;
	private double gini_impurity;
	private HashMap<String, ArrayList<Instance>> subset;
	
	/**
	 * Constructor: initialize fields. This class is for calculating the information gain for
	 * discrete attribute.
	 * @param target
	 * @param attribute
	 * @param instances
	 * @throws IOException
	 */
	public Discrete_Prob(Attribute target, Attribute attribute, ArrayList<Instance> instances)
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
		
//		for (String key : subset.keySet()) {
//		    System.out.println(key + subset.get(key).size());
//		}
		

		int totalN = instances.size();
		gini_impurity = Gini_Impurity.calculate(target, instances);
		
		
		for (String s : subset.keySet()) {
			ArrayList<Instance> currSubset = subset.get(s);
			int subN = currSubset.size();
			double subRes = ((double) subN) / ((double) totalN) * 
					(1 - Gini_Impurity.calculate(target, currSubset));
			//double subRes = (1 - Gini_Impurity.calculate(target, currSubset));
			gini_impurity += subRes;
		}
//double pValue = ((double) countSingleValue) / ((double)totalN);
//		double itemRes = -pValue * (Math.log(pValue));
//		double subRes = ((double) subN) / ((double) totalN) ;
//		double xres = -subRes *(Math.log(subRes));
		//		Entropy.calculate(target, currSubset);
//infoGain -= subRes;
//		
	}
	
	public Attribute getAttribute() {
		return attribute;
	}
	
	public double getInfoGain() {
		return gini_impurity;
	}
	
	public HashMap<String, ArrayList<Instance>> getSubset() {
		return subset;
	}
	
	public String toString() {
		return "Attribute: " + attribute + "\n"  
				+ "InfoGain: " + gini_impurity + "\n" + "Subset: " + subset;
	}
	
	
}