
/**
 * This class is used to process the input csv file 
 */


package ProcessInput;

import DataDefination.Attribute;
import DataDefination.Instance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProcessInputData {
	ArrayList<Attribute> attributeSet;
	ArrayList<Instance> instanceSet;
	public static Attribute targetAttribute;
	
	/**
	 * Constructor to initialize fields.
	 * @param fileName: file name of input data file
	 * @throws IOException 
	 */
	
	/*
	 * public ProcessInputData(String fileName) throws IOException { attributeSet =
	 * new ArrayList<Attribute>(); instanceSet = new ArrayList<Instance>();
	 * 
	 * @SuppressWarnings("resource") Scanner in = new Scanner(new File(fileName));
	 * 
	 * // Pass the first two line of input data. if (!in.hasNextLine()) throw new
	 * IOException("Invalid input format"); in.nextLine(); if (!in.hasNextLine())
	 * throw new IOException("Invalid input format"); in.nextLine();
	 * 
	 * String line = in.nextLine(); // Put all attributes into attributeSet while
	 * (!line.equals("")) {
	 * 
	 * // lineArr should have three elements. // lineArr[1] is attribute name;
	 * lineArr[2] is attribute value String[] lineArr = line.split("\\s+");
	 * 
	 * if (lineArr.length != 3) throw new IOException("Invalid input format");
	 * Attribute attr = new Attribute(lineArr[1], lineArr[2]);
	 * attributeSet.add(attr); line = in.nextLine(); } targetAttribute =
	 * attributeSet.get(attributeSet.size() - 1);
	 * 
	 * // Pass the next two line if (!in.hasNextLine()) throw new
	 * IOException("Invalid input format"); line = in.nextLine();
	 * 
	 * // Put all instances into instanceSet while (in.hasNextLine()) { line =
	 * in.nextLine(); String[] lineArr = line.split(","); Instance item = new
	 * Instance(); for (int i = 0; i < lineArr.length; i++) {
	 * item.addAttribute(attributeSet.get(i).getName(), lineArr[i]); }
	 * instanceSet.add(item); } }
	 */


	
	public ProcessInputData(String fileName) throws IOException {

		attributeSet = new ArrayList<Attribute>();
		instanceSet = new ArrayList<Instance>();

		@SuppressWarnings("resource")
		Scanner in = new Scanner(new File(fileName));

		addToAttributeSet();

		targetAttribute = attributeSet.get(attributeSet.size() - 1);

		// Put all instances into instanceSet
		while (in.hasNextLine()) {
			String line = in.nextLine();
			String[] lineArr = line.split(",");
			Instance item = new Instance();
			for (int i = 0; i < lineArr.length; i++) {
				item.addAttribute(attributeSet.get(i).getName(), lineArr[i]);
			}
			instanceSet.add(item);
		}
	}


	public void addToAttributeSet() throws IOException {
		Attribute attr1 = new Attribute("age","real");
		attributeSet.add(attr1);
		Attribute attr2 = new Attribute("anaemia","{0,1}");
		attributeSet.add(attr2);
		Attribute attr3 = new Attribute("creatinine_phosphokinase","real");
		attributeSet.add(attr3);
		Attribute attr4 = new Attribute("diabetes","{0,1}");
		attributeSet.add(attr4);
		Attribute attr5 = new Attribute("ejection_fraction","real");
		attributeSet.add(attr5);
		Attribute attr6 = new Attribute("high_blood_pressure","{0,1}");
		attributeSet.add(attr6);
		Attribute attr7 = new Attribute("platelets","real");
		attributeSet.add(attr7);
		Attribute attr8 = new Attribute("serum_creatinine","real");
		attributeSet.add(attr8);
		Attribute attr9 = new Attribute("serum_sodium","real");
		attributeSet.add(attr9);
		Attribute attr10 = new Attribute("sex","{0,1}");
		attributeSet.add(attr10);
		Attribute attr11 = new Attribute("smoking","{0,1}");
		attributeSet.add(attr11);
		Attribute attr12 = new Attribute("time","real");
		attributeSet.add(attr12);
		Attribute attr13 = new Attribute("label","{0,1}");
		attributeSet.add(attr13);
	}
	
	public ArrayList<Attribute> getAttributeSet() {
		attributeSet.remove(attributeSet.size() - 1);
		return attributeSet;
	}
	
	public ArrayList<Instance> getInstanceSet(){
		return instanceSet;
	}
	
	public Attribute getTargetAttribute() {
		return targetAttribute;
	}

}