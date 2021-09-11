package gainratio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import core.Entropy;
import definition.Attribute;
import definition.Instance;

public class GainRatioContinuous{
	
	private Attribute attribute;
	private double threshold;
	private double gainRatio = -1;
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

	public GainRatioContinuous(Attribute attribute, Attribute target, 
			ArrayList<Instance> instances) throws IOException {
		
		this.attribute = attribute;
		
		// Initialize threshold and infoGain
		// (1) Get the name of the attribute to be calculated
		final String attributeName = attribute.getName();
				
		// (2) Sort instances according to the attribute
		Comparator<Instance> comparator = new Comparator<Instance>() {
			@Override
			public int compare(Instance x, Instance y) {
				HashMap<String, String> xPair = x.getAttributeValuePairs();
				String xValue = xPair.get(attributeName);
						
				HashMap<String, String> yPair = y.getAttributeValuePairs();
				
				String yValue = yPair.get(attributeName);
				
				try
				{
				  Double.parseDouble(xValue);
				  Double.parseDouble(yValue);
				}
				catch(NumberFormatException e)
				{
				  //not a double
					
					return 0;
				}

				if (Double.parseDouble(xValue) - Double.parseDouble(yValue) > 0) return 1;
				else if (Double.parseDouble(xValue) - Double.parseDouble(yValue) < 0) return -1;
			
				else return 0;
				
			}
		};
		
		Collections.sort(instances, comparator);
	  
		//instances.forEach((n)-> System.out.println(n));
		// (2) 
		int thresholdPos = 0;
		for (int i = 0; i < instances.size() - 1; i++) {
			HashMap<String, String> instancePair = instances.get(i).getAttributeValuePairs();
			String instanceValue = instancePair.get(attributeName);
			HashMap<String, String> instancePair2 = instances.get(i + 1).getAttributeValuePairs();
			String instanceValue2 = instancePair2.get(attributeName);
		
			
			if (!instanceValue.equals(instanceValue2)) {
				double currSplitInfo = calculateSplitInfo(attribute, target, instances, i);
				double gain = calculateGain(attribute, target, instances, i);
				
				double currentGainRatio = gain/currSplitInfo;
				
				if(currentGainRatio > gainRatio) {
					gainRatio = currentGainRatio;
					thresholdPos = i;
				}
				
			}
		}
		
		
		// (4) Calculate threshold
		HashMap<String, String> a = instances.get(thresholdPos).getAttributeValuePairs();
		String aValue = a.get(attributeName);
		HashMap<String, String> b = instances.get(thresholdPos).getAttributeValuePairs();
		String bValue = b.get(attributeName);

		try {
			Double.parseDouble(aValue);
			Double.parseDouble(bValue);
		} catch (NumberFormatException e) {
			// not a double

			aValue = "0.0";
			bValue = "0.0";

		}
		threshold = (Double.parseDouble(aValue) + Double.parseDouble(bValue)) / 2;

		
		// Initialize subset
		subset = new HashMap<String, ArrayList<Instance>>();
		ArrayList<Instance> left = new ArrayList<Instance>();
		ArrayList<Instance> right = new ArrayList<Instance>();
		for (int i = 0; i < thresholdPos; i++) {
			left.add(instances.get(i));
		}
		for (int i = thresholdPos + 1; i < instances.size(); i++) {
			right.add(instances.get(i));
		}
		String leftName = "less" + threshold;
		String rightName = "more" + threshold;
		subset.put(leftName, left);
		subset.put(rightName, right);
	}
	
	
	
	/**
	 * Calculate info gain 
	 * @param attribute
	 * @param target
	 * @param instances
	 * @param index
	 * @return
	 * @throws IOException
	 */
	public static double calculateGain(Attribute attribute, Attribute target, 
			ArrayList<Instance> instances, int index) throws IOException {
		
		int totalN = instances.size();
		double infoGain = Entropy.calculate(target, instances);
		
		int subL = index + 1;
		int subR = instances.size() - index - 1;
		
		double subResL = ((double) subL) / ((double) totalN) * Entropy.calculateContiEntropy(target, instances, 0, index);
		double subResR = ((double) subR) / ((double) totalN) * Entropy.calculateContiEntropy(target, instances, index+1, totalN-1);
		infoGain -= (subResL + subResR);
		 
		return infoGain;
	}
	
	
    /**
     * Calculate split info 
     * @param attribute
     * @param target
     * @param instances
     * @param index
     * @return
     * @throws IOException
     */
	
	public static double calculateSplitInfo(Attribute attribute, Attribute target, 
			ArrayList<Instance> instances, int index) throws IOException {
		
		int totalN = instances.size();
		double splitinfo = 0;
		int subL = index + 1;
		int subR = instances.size() - index - 1;
		
		double subResL = ((double) subL) / ((double) totalN);
		splitinfo -= (double) subResL * ((double)(Math.log(subResL) / Math.log(2)));
		
		double subResR = ((double) subR) / ((double) totalN);
		splitinfo -= (double) subResR * ((double)(Math.log(subResR) / Math.log(2)));
		 
		return splitinfo;
	}
	
	
	public Attribute getAttribute() {
		return attribute;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public double getGainRatio() {
		return gainRatio;
	}
	
	public HashMap<String, ArrayList<Instance>> getSubset() {
		return subset;
	}
	
	public String toString() {
		return "Attribute: " + attribute.getName() + "\n" + "Threshold: " + threshold + "\n" 
				+ "splitinfo: " + gainRatio + "\n" + "Subset: " + subset;
	}
}