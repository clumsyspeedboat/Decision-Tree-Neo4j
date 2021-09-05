
/**
 * This class is used to process the input csv file 
 */

package input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.exec.util.StringUtils;
import org.apache.shiro.crypto.hash.Hash;

import definition.Attribute;
import definition.Instance;

public class ProcessInputData {
	private ArrayList<Attribute> attributeSet;
	private ArrayList<Instance> instanceSet;
	public static Attribute targetAttribute;
	static int targetAttributeIndex;
	
	/**
	 * This function create a custom array list from CSV
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> CustomListFromCSV(String fileName) throws IOException{
		ArrayList<String> nList = new ArrayList<String>();
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(new File(fileName));
		String aLine = in.nextLine();
		
		String[] attributeArr = aLine.split(",");
		
		
		while (in.hasNextLine()) {
			String line = in.nextLine();
			String[] lineArr = line.split(",");
			
			StringBuilder str = new StringBuilder("");
			
			if (lineArr.length == attributeArr.length) {
				
				for (int a = 0; a < attributeArr.length; a++) {
					String s1 = attributeArr[a];
					String s2 = lineArr[a];
					String s3 = s1 +':' + s2;
		            str.append(s3).append(",");  
				}	
			}
			
			String commaseparatedlist = str.toString();
		
			if (commaseparatedlist.length() > 0)
	            commaseparatedlist = commaseparatedlist.substring(0, commaseparatedlist.length() - 1);
	  
	        nList.add(commaseparatedlist);
		}
		return nList;
	} 
	

	/**
	 * This function read csv and process the dataset dynamically.
	 * 
	 * @param fileName: file name of input data file
	 * @throws IOException
	 */
	public ProcessInputData(String fileName, String targetAtt) throws IOException{
		attributeSet = new ArrayList<Attribute>();
		instanceSet = new ArrayList<Instance>();

		LinkedHashMap<String, HashSet<String>> myMap = new LinkedHashMap<String, HashSet<String>>();
		HashSet<String> uSet;

		@SuppressWarnings("resource")
		Scanner in = new Scanner(new File(fileName));

		String aLine = in.nextLine();

		String[] attributeArr = aLine.split(",");

		int datasetCount = 0;

		while (in.hasNextLine()) {
			String line = in.nextLine();
			String[] lineArr = line.split(",");
			if (lineArr.length == attributeArr.length) {
				Instance item = new Instance();

				for (int a = 0; a < attributeArr.length; a++) {
					if (myMap.containsKey(attributeArr[a])) {
						uSet = myMap.get(attributeArr[a]);
						uSet.add(lineArr[a]);
					} else {
						uSet = new HashSet<String>();
						uSet.add(lineArr[a]);
						myMap.put(attributeArr[a], uSet);
					}

					item.addAttribute(attributeArr[a], lineArr[a]);
				}
				instanceSet.add(item);
				datasetCount++;
			} else {

			}
		}

		HashSet<String> targetColumn = myMap.get(targetAtt);
		

		int index = 0;
		double threshold = 1.0 * targetColumn.size() / datasetCount + 0.01;
		
		for (Map.Entry<String, HashSet<String>> entry : myMap.entrySet()) {
			String key = entry.getKey();

			HashSet<String> value = entry.getValue();
			
			
			int nUnique = entry.getValue().size();
		
			boolean isCategorical = 1.0 * nUnique / datasetCount < threshold;
			
			if (isCategorical == false) {
				Attribute attr1 = new Attribute(key, "real");
				attributeSet.add(attr1);
			} else {
				String str = "{" + String.join(",", value) + "}";
				Attribute attr2 = new Attribute(key, str);
				attributeSet.add(attr2);
			}
			if (attributeSet.get(index).getName().equals(targetAtt)) {
				targetAttributeIndex = index;
				targetAttribute = attributeSet.get(index);
			}
			index++;
		}
 
	}
	

	/**
	 * This function is for processing input nodes from Neo4j graph database
	 * 
	 * @param nodes
	 * @throws IOException
	 */
	public ProcessInputData(ArrayList<String> nodesList, String targetAtt) throws IOException {
		attributeSet = new ArrayList<Attribute>();
		instanceSet = new ArrayList<Instance>();
		
		int datasetCount = nodesList.size();
		
		HashMap<String, HashSet<String>> myMap = new HashMap<String, HashSet<String>>();
		HashSet<String> uSet;

		boolean isListEmpty = nodesList.isEmpty();
		if(isListEmpty == true) {
			System.out.println("List is empty");
		}else {
			for(String line : nodesList) {
				Instance item = new Instance();
				String[] lineArr = line.trim().split(",");
				
				for(String l: lineArr) {
					String[] attArray = l.trim().split(":");
					
					item.addAttribute(attArray[0], attArray[1]);
				
					if(myMap.containsKey(attArray[0])) {
						uSet = myMap.get(attArray[0]);
						uSet.add(attArray[1]);
					}else {
						uSet = new HashSet<String>();
						uSet.add(attArray[1]);
					    myMap.put(attArray[0], uSet);
					}
				}
				instanceSet.add(item);
			}
			
			HashSet<String> target = myMap.get(targetAtt);
			
			double threshold = 1.0 * target.size() / datasetCount + 0.01;
		    
			int index=0;
			
			for (Map.Entry<String, HashSet<String>> entry : myMap.entrySet()) {
				String key = entry.getKey();

				HashSet<String> value = entry.getValue();
				int nUnique = entry.getValue().size();

				boolean isCategorical = 1.0 * nUnique / datasetCount < threshold;
				if (isCategorical == false) {
					Attribute attr1 = new Attribute(key, "real");
					attributeSet.add(attr1);
				}else{
					String str = "{" + String.join(",", value) + "}";
					Attribute attr2 = new Attribute(key, str);
					attributeSet.add(attr2);
				}
				if(attributeSet.get(index).getName().equals(targetAtt)) {
					targetAttributeIndex = index;
					targetAttribute = attributeSet.get(index);
				}
				index++;
			}
		}
	}

	public ArrayList<Attribute> getAttributeSet() {
		//attributeSet.remove(attributeSet.size() - 1);
		//attributeSet.remove(targetAttribute);
		attributeSet.remove(targetAttributeIndex);
		
		return attributeSet;
	}

	public ArrayList<Instance> getInstanceSet() {
		return instanceSet;
	}

	public Attribute getTargetAttribute(){
		return targetAttribute;
	}

	public static void main(String[] args) throws IOException {
		ArrayList<String> nodesList = new ArrayList<>();

		nodesList.add(
				"anaemia:1, serum_creatinine:1.2, sex:1, ejection_fraction:40, creatinine_phosphokinase:170, platelets:336000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:0, time:250, serum_sodium:135, diabetes:1, age:'55.0'");
		nodesList.add(
				"anaemia:0, serum_creatinine:1.4, sex:1, ejection_fraction:50, creatinine_phosphokinase:224, platelets:481000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:1, time:192, serum_sodium:138, diabetes:0, age:'78.0'");
		nodesList.add("anaemia:1, serum_creatinine:0.8, sex:0, ejection_fraction:40, creatinine_phosphokinase:101, platelets:226000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:0, time:187, serum_sodium:141, diabetes:0, age:'40.0'");
		nodesList.add("anaemia:0, serum_creatinine:1.18, sex:0, ejection_fraction:60, creatinine_phosphokinase:582, platelets:263358.03, DEATH_EVENT:0, high_blood_pressure:0, smoking:0, time:82, serum_sodium:137, diabetes:0, age:'42.0'");
		nodesList.add("anaemia:0, serum_creatinine:0.9, sex:1, ejection_fraction:25, creatinine_phosphokinase:231, platelets:253000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:1, time:10, serum_sodium:140, diabetes:0, age:'62.0'");
		
		//ArrayList<String> nList = ProcessInputData.CustomListFromCSV("data/train.csv");
		//ProcessInputData p = new ProcessInputData(nList, "DEATH_EVENT");
		//System.out.println(p.getTargetAttribute());
		
		
		//ProcessInputData p = new ProcessInputData("data/train.csv", "DEATH_EVENT");
		//System.out.println(p.getTargetAttribute());
		

	}
}