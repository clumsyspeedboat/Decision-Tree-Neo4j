
/**
 * This class is used to process the input csv file 
 */

package input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import definition.Attribute;
import definition.Instance;

public class ProcessInputData {
	ArrayList<Attribute> attributeSet;
	ArrayList<Instance> instanceSet;
	public static Attribute targetAttribute;

	
	/**
	 * This function read csv and process the dataset dynamically.
	 * 
	 * @param fileName: file name of input data file
	 * @throws IOException
	 */
	public ProcessInputData(String fileName) throws IOException {
		attributeSet = new ArrayList<Attribute>();
		instanceSet = new ArrayList<Instance>();
		
		Map<Integer, HashSet<String>> myMap = new HashMap<Integer, HashSet<String>>();
		HashSet<String> uSet;

		@SuppressWarnings("resource")
		Scanner in = new Scanner(new File(fileName));

		String aLine = in.nextLine();
		
		String[] attributeArr = aLine.split(",");
		
		int datasetCount = 0;
		
		while(in.hasNextLine()) {
			String line = in.nextLine();
			String[] lineArr = line.split(",");
			if(lineArr.length == attributeArr.length) {
				Instance item = new Instance();
				
				for(int a=0; a<attributeArr.length; a++){
					if(myMap.containsKey(a)) {
						uSet = myMap.get(a);
						uSet.add(lineArr[a]);
					}else {
						uSet = new HashSet<String>();
						uSet.add(lineArr[a]);
					    myMap.put(a, uSet);
					}
					
					item.addAttribute(attributeArr[a], lineArr[a]);
				}
				instanceSet.add(item);
				datasetCount++;
			}else {
				
			}
			
		}
	
	
		
		HashSet<String> targetColumn = myMap.get(myMap.size()-1);
		double threshold = 1.0 * targetColumn.size()/datasetCount + 0.01;
		
	
		for(int i=0;i<attributeArr.length;i++){
			int nUnique = myMap.get(i).size();
			
			
			boolean isCategorical = 1.0 * nUnique/datasetCount < threshold;
		
			
			if(isCategorical == false){
				Attribute attr1 = new Attribute(attributeArr[i], "real");
				attributeSet.add(attr1);
			}else{
				HashSet<String> app = myMap.get(i);
				
				String str = "{"+String.join(",", app)+"}";
				
				Attribute attr2 = new Attribute(attributeArr[i], str);
				attributeSet.add(attr2);
			}
		}
		
		targetAttribute = attributeSet.get(attributeSet.size() - 1);
		
	}
	
	
	
	/**
	 * This function is for processing input nodes from Neo4j graph database
	 * @param nodes 
	 * @throws IOException 
	 */

    public ProcessInputData(ArrayList<ArrayList<String>> nodesList, String targetAtt) throws IOException {
    	attributeSet = new ArrayList<Attribute>();
		instanceSet = new ArrayList<Instance>();
		int targetAttIndex = 0;
		int datasetCount = 0;
		
		Map<Integer, HashSet<String>> myMap = new HashMap<Integer, HashSet<String>>();
		HashSet<String> uSet;
		
		
		Set<String> attributes = new HashSet<>();
		
		boolean isListEmpty = nodesList.isEmpty();
		if(isListEmpty == true) {
			System.out.println("List is empty");
		}else {
			for(ArrayList<String> innerList : nodesList) {
				Instance item = new Instance();
				for(String attrValues : innerList) {
					String[] output = attrValues.trim().split(",");
			        for(int i=0;i<output.length;i++) {
			        	String[] eachAttribute = output[i].trim().split(":");
			        	if(eachAttribute.length == 2) {
			        		attributes.add(eachAttribute[0]);
			        		//get the target attribute index
			        		
		
			        		if(eachAttribute[0].equalsIgnoreCase(targetAtt)) {
			        			targetAttIndex = i;
			        		}
			        		
			        		if(myMap.containsKey(i)) {
								uSet = myMap.get(i);
								uSet.add(eachAttribute[1]);
							}else {
								uSet = new HashSet<String>();
								uSet.add(eachAttribute[1]);
							    myMap.put(i, uSet);
							}
			        		
			        		item.addAttribute(eachAttribute[0], eachAttribute[1]);
			        	}
			        	
			        	  
			        } 
			    }
				instanceSet.add(item);
				
			}
		
			
			//To determine feature type dynamically, whether categorical or numerical
			datasetCount = nodesList.size();
			double threshold=0.0;
			
			
			HashSet<String> targetColumn = myMap.get(targetAttIndex);
			threshold = 1.0 * targetColumn.size() / datasetCount + 0.01;
			
		
			String[] atrributesArray = attributes.toArray(new String[attributes.size()]);
			/*
			 * for(int i=0;i<atrributesArray.length;i++) {
			 * System.out.println(atrributesArray[i]); }
			 */
				

			for (int i = 0; i < attributes.size(); i++) {
				int nUnique = myMap.get(i).size();

				boolean isCategorical = 1.0 * nUnique / datasetCount < threshold;

				if (isCategorical == false) {
					Attribute attr1 = new Attribute(atrributesArray[i], "real");
					attributeSet.add(attr1);
				} else {
					HashSet<String> app = myMap.get(i);

					String str = "{" + String.join(",", app) + "}";

					Attribute attr2 = new Attribute(atrributesArray[i], str);
					attributeSet.add(attr2);
				}
			}
			targetAttribute = attributeSet.get(targetAttIndex);
		}
		
    }
    

	public ArrayList<Attribute> getAttributeSet() {
		attributeSet.remove(attributeSet.size() - 1);
		return attributeSet;
	}

	public ArrayList<Instance> getInstanceSet() {
		return instanceSet;
	}
	
	public Attribute getTargetAttribute() {
		return targetAttribute;
	}

	public static void main(String[] args) throws IOException {
		ArrayList<ArrayList<String>> nodesList = new ArrayList<>();
		ArrayList<String> al= new ArrayList<String>();
		al.add("anaemia:0, serum_creatinine:1.1, sex:0, ejection_fraction:35, creatinine_phosphokinase:618, platelets:327000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:0, time:245, serum_sodium:142, diabetes:0, age:70.0");
		ArrayList<String> bl= new ArrayList<String>();
		bl.add("anaemia:1, serum_creatinine:1.2, sex:0, ejection_fraction:30, creatinine_phosphokinase:159, platelets:302000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:0, time:29, serum_sodium:138, diabetes:0, age:50.0");
		nodesList.add(al);
		nodesList.add(bl);
		ProcessInputData p = new ProcessInputData(nodesList, "DEATH_EVENT");
		//System.out.println(p.getTargetAttribute());
	    //System.out.println(p.getInstanceSet());
	    //System.out.println(p.getAttributeSet().size());
		
	}
}