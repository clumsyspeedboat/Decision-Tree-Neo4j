package gini;

import definition.Attribute;
import definition.Instance;
import core.InfoGainContinuous;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class ContinuousProbability{
	
	private Attribute attribute;
	private double giniValue;
	private HashMap<String, ArrayList<Instance>> subset;
	protected double threshold;
	
	/**
	 * Constructor: initialize fields. This class is for calculating the information gain
	 * of continuous attribute. 
	 * Use one cut to binary method. 
	 * @param attribute
	 * @param target
	 * @param instances
	 * @throws IOException
	 */
	public ContinuousProbability(Attribute attribute, Attribute target, 
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
		
		LinkedHashSet<Double> uniqueCollection =new LinkedHashSet<Double>();
		for (int i = 0; i < instances.size(); i++) {
			HashMap<String, String> instancePair = instances.get(i).getAttributeValuePairs();
			Double variable = Double.parseDouble(instancePair.get(attributeName));
			uniqueCollection.add(variable);
		}
		
		Double splitAt;
		Double[] uniqueCollectionArray = new Double[uniqueCollection.size()];
		uniqueCollectionArray =	uniqueCollection.toArray(uniqueCollectionArray);
		double smallestImpurity = 0.0;
		ArrayList<Instance> leftInstances = new ArrayList<Instance>();
		ArrayList<Instance> rightInstances = new ArrayList<Instance>();
		for (int i = 0; i < uniqueCollectionArray.length - 1; i++){
			splitAt = (uniqueCollectionArray[i+1] + uniqueCollectionArray[i])/2;
			//ArrayList Of Instances left leave
			ArrayList<Instance> leftLeaveInstances = new ArrayList<Instance>();
			ArrayList<Instance> rightLeaveInstances = new ArrayList<Instance>();
			for (int j = 0; j< instances.size(); j++)
			{
				HashMap<String, String> instancePair = instances.get(j).getAttributeValuePairs();
				Double variable = Double.parseDouble(instancePair.get(attributeName));
				if(variable < splitAt)
				{
					leftLeaveInstances.add(instances.get(j));
				}
				else
				{
					rightLeaveInstances.add(instances.get(j));
				}
			}
			int totalN = instances.size();
			//Calculate left Impurity
			double giniLeftValue = GiniIndex.calculate(target, leftLeaveInstances);
			double leftImpurity = ((double)leftLeaveInstances.size()/(double)totalN)*giniLeftValue;
			//Calculate right Impurity
			double giniRightValue = GiniIndex.calculate(target, rightLeaveInstances);
			double rightImpurity = ((double)rightLeaveInstances.size()/(double)totalN)*giniRightValue;
			double totalImpurity = leftImpurity + rightImpurity;
			if(smallestImpurity == 0.0)
			{
				smallestImpurity = totalImpurity;
				leftInstances = leftLeaveInstances;
				rightInstances = rightLeaveInstances;
				threshold = splitAt;
				giniValue = totalImpurity;
			}
			else
			{
				if(totalImpurity < smallestImpurity)
				{
					smallestImpurity = totalImpurity;
					leftInstances = leftLeaveInstances;
					rightInstances = rightLeaveInstances;
					threshold = splitAt;
					giniValue = totalImpurity;
				}
			}
		}
		String leftName = "less" + threshold;
		String rightName = "more" + threshold;
		// Initialize subset
		subset = new HashMap<String, ArrayList<Instance>>();
		subset.put(leftName, leftInstances);
		subset.put(rightName, rightInstances);
	}

	
	public Attribute getAttribute() {
		return attribute;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public double getGiniValue() {
		return giniValue;
	}
	
	public HashMap<String, ArrayList<Instance>> getSubset() {
		return subset;
	}
	
	
	public static void main(String[] args) throws IOException {
		String[] attributes = { "lotion", "expos", "burn"};

		@SuppressWarnings("serial")
		ArrayList<String[]> sunBurnDataset = new ArrayList<String[]>() {
			{
				add(new String[] { "A", "20", "N" });
				add(new String[] { "A", "30", "N" });
				add(new String[] { "A", "40", "Y" });
				add(new String[] { "B", "20", "Y"});
				add(new String[] { "C", "20", "N"});		
			}
		};
		
		ArrayList<Instance> instanceSet = new ArrayList<>();
		Iterator<String[]> iter = sunBurnDataset.iterator();
		while (iter.hasNext()) {
			Instance item = new Instance();
			String[] a = iter.next();
			for (int i = 0; i < a.length; i++) {
				item.addAttribute(attributes[i], a[i]);
			}
			instanceSet.add(item);
		}
		
		Attribute target = new Attribute("burn", "{N,Y}");
		
		Attribute currAttribute = new Attribute("expos", "real");
		
		ContinuousProbability continuous = new ContinuousProbability(currAttribute, target, instanceSet);
		double giniValue = continuous.getGiniValue();
		System.out.println(giniValue);
	}
}
