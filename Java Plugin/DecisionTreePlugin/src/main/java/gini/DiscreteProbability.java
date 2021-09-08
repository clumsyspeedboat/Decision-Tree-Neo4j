package gini;

import definition.Attribute;
import definition.Instance;
import core.InfoGainDiscrete;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DiscreteProbability{
	
	//private double giniValue;
	
	private Attribute attribute;
	private double giniValue;
	private HashMap<String, ArrayList<Instance>> subset;
	
	/**
	 * Constructor: initialize fields. This class is for calculating the gini for
	 * discrete attribute.
	 * @param target
	 * @param attribute
	 * @param instances
	 * @throws IOException
	 */
	public DiscreteProbability(Attribute target, Attribute attribute, ArrayList<Instance> instances)
			throws IOException {
		
		this.attribute = attribute;
		
		ArrayList<String> valuesOfAttribute = attribute.getValues();
		
		
		String attributeName = attribute.getName();
		
		subset = new HashMap<String, ArrayList<Instance>>();
		
		
		
		for (String s : valuesOfAttribute) {
			subset.put(s, new ArrayList<Instance>());
		}
		//append to the subset no of instances having atrribute values
		for (Instance instance : instances) {
			HashMap<String, String> attributeValuePairsOfInstance = instance.getAttributeValuePairs();
			
			String valueOfInstanceAtAttribute = attributeValuePairsOfInstance.get(attributeName);

			if (!subset.containsKey(valueOfInstanceAtAttribute)) 
				throw new IOException("Invalid input data");
			subset.get(valueOfInstanceAtAttribute).add(instance);
		}
			
		//System.out.println(subset);
		
		int totalN = instances.size();
		
		
		for (String s : subset.keySet()) {
			ArrayList<Instance> currSubset = subset.get(s);
			int subN = currSubset.size();
			double gValue = GiniIndex.calculate(target, currSubset);
			double weightedSum = ((double) subN) / ((double)totalN) * (gValue);
			giniValue += weightedSum;
		
		}
	}
	
	
	
	
	public Attribute getAttribute() {
		return attribute;
	}
	
	public double getGiniValue(){
		return giniValue;
	}
	
	public HashMap<String, ArrayList<Instance>> getSubset() {
		return subset;
	}
	
	
	public static ArrayList<Attribute> addTradingDataSet() throws IOException{
		ArrayList<Attribute> attributeSet = new ArrayList<Attribute>();
		Attribute attr1 = new Attribute("PastTrend","{Positive,Negative}");
		attributeSet.add(attr1); 
		Attribute attr2 = new Attribute("OpenInterest","{High,Low}");
		attributeSet.add(attr2);
		Attribute attr3 = new Attribute("TradingVolume","{High,Low}");
		attributeSet.add(attr3);
		Attribute attr4 = new Attribute("Return","{Up,Down}");
		attributeSet.add(attr4);
		return attributeSet;
	}	
	
	
	
	public static void main(String[] args) throws IOException {
		ArrayList<Attribute> allAttributes = addTradingDataSet();
		ArrayList<Instance> instanceSet = new ArrayList<Instance>();
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(new File("data/trading.csv"));
		
		Attribute targetAttribute = allAttributes.get(allAttributes.size() - 1);
		
		// Put all instances into instanceSet
		while (in.hasNextLine()) {
			String line = in.nextLine();
			String[] lineArr = line.split(",");
			Instance item = new Instance();
			for (int i = 0; i < lineArr.length; i++) {
				item.addAttribute(allAttributes.get(i).getName(), lineArr[i]);
			}
			instanceSet.add(item);
		}
		
		in.close();
		
		Attribute currAtt = allAttributes.get(2);
		
		DiscreteProbability dp = new DiscreteProbability(targetAttribute,currAtt, instanceSet);
		double giniValue = dp.getGiniValue();
		System.out.println(giniValue);
	
	}

	
	
}