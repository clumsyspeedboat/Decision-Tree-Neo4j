/**
 * 
 * Instance class
 */

package definition;

import java.util.HashMap;

public class Instance {
	private static int count = 0;
	private int index;
	private HashMap<String, String> attributeValuePairs;
	
	public Instance() {
		index = count;
		attributeValuePairs = new HashMap<String, String>();
		count++;
	}
	
	public void addAttribute(String name, String value) {
		attributeValuePairs.put(name, value);
	}
	public int getInstanceIndex() {
		return index;
	}
	public HashMap<String, String> getAttributeValuePairs() {
		return attributeValuePairs;
	}
	public String toString() {
		return "@Instance Index: " + index + "; " 
				+ "@Instance Attribute Value Pairs: " + attributeValuePairs;
	}
	
	
}