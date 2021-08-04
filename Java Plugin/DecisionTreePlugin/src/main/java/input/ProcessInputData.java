
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
				
				for(int a=0; a<attributeArr.length; a++) {
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

}