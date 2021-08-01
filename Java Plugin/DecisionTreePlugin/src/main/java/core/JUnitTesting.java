package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import definition.Attribute;
import definition.Instance;


public class JUnitTesting {
	ArrayList<Attribute> attributeSet;
	ArrayList<Instance> instanceSet;
	public Attribute targetAttribute;

	
	public int square(int x) {
		return x*x;
	}
	
	public int divide(int y, int z) {
		return y/z;
	}
	
	public void addGolfAttributeSet() throws IOException {
		Attribute attr1 = new Attribute("Outlook","{Sunny,Overcast,Rain}");
		attributeSet.add(attr1); 
		Attribute attr2 = new Attribute("Temperature","real");
		attributeSet.add(attr2);
		Attribute attr3 = new Attribute("Humidity","real");
		attributeSet.add(attr3);
		Attribute attr4 = new Attribute("Wind","{Weak,Strong}");
		attributeSet.add(attr4);
		Attribute attr5 = new Attribute("Decision","{Yes,No}");
		attributeSet.add(attr5);
	}
	
	
	public void ProcessInputData(String fileName) throws IOException {
		attributeSet = new ArrayList<Attribute>();
		instanceSet = new ArrayList<Instance>();

		@SuppressWarnings("resource")
		Scanner in = new Scanner(new File(fileName));

		addGolfAttributeSet();

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
		
		in.close();
	}
	
	
	
}
