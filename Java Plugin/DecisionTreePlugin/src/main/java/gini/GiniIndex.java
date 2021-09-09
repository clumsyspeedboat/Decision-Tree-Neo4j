package gini;

import definition.Attribute;
import definition.Instance;
import core.Entropy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GiniIndex extends Entropy{
	
	/**
	 * Calculate gini index of instances for the target attribute.
	 * Only for discrete attribute.
	 * @param target
	 * @param instances
	 * @return double
	 * @throws IOException
	 */
	public static double calculate(Attribute target, ArrayList<Instance> instances) throws IOException{

		ArrayList<String> valuesOfTarget = target.getValues();
		
		
		String targetName = target.getName();
		
		HashMap<String, Integer> countValueOfTarget = new HashMap<String, Integer>();

		for (String s : valuesOfTarget) {
			countValueOfTarget.put(s, 0);
		}
		
		
		for (Instance instance : instances) {
			HashMap<String, String> attributeValuePairsOfInstance = instance.getAttributeValuePairs();
			String valueOfInstanceAtTarget = attributeValuePairsOfInstance.get(targetName);
            

			if (!countValueOfTarget.containsKey(valueOfInstanceAtTarget)) {
				throw new IOException("Invalid input data");
			}
			countValueOfTarget.put(valueOfInstanceAtTarget, 
					countValueOfTarget.get(valueOfInstanceAtTarget) + 1);
		}
		
		
		int totalN = instances.size();
		double giniindex = 0;
		

		for (String s : valuesOfTarget) {
			int countSingleValue = countValueOfTarget.get(s);
			if (countSingleValue == 0) continue;
			
			double pValue = ((double) countSingleValue) / ((double)totalN);
			double itemRes = Math.pow(pValue,2);
			giniindex += itemRes;
		}
		giniindex = 1 - giniindex;
				
		return giniindex;
	}
	
	
	
	/**
	 * Calculate gini index of instances for the target attribute.
	 * Only for continuous attribute.
	 */
	public static double calculateConti(Attribute target, ArrayList<Instance> instances,
                                        int start, int end) throws IOException {
		ArrayList<String> valuesOfTarget = target.getValues();
		String targetName = target.getName();
		HashMap<String, Integer> countValueOfTarget = new HashMap<String, Integer>();
		
		
		for (String s : valuesOfTarget) {
			countValueOfTarget.put(s, 0);
		}
		for (int i = start; i <= end; i++) {
			Instance instance = instances.get(i);
			HashMap<String, String> attributeValuePairsOfInstance = instance.getAttributeValuePairs();
			String valueOfInstanceAtTarget = attributeValuePairsOfInstance.get(targetName);
			if (!countValueOfTarget.containsKey(valueOfInstanceAtTarget)) 
				throw new IOException("Invalid input data");
			countValueOfTarget.put(valueOfInstanceAtTarget, 
					countValueOfTarget.get(valueOfInstanceAtTarget) + 1);
		}
		
		int totalN = instances.size();
		double giniindex = 0;
				
		for (String s : valuesOfTarget) {
			int countSingleValue = countValueOfTarget.get(s);
			if (countSingleValue == 0) continue;
			
			double pValue = ((double) countSingleValue) / ((double)totalN);
			double itemRes = Math.pow(pValue,2);
			giniindex += itemRes;
		}
		
		giniindex = 1 - giniindex;
		
		return giniindex;
	}

}
