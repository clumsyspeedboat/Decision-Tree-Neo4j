package gainratio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import core.*;

import definition.Attribute;
import definition.Instance;


public class ChooseAttributeGR extends ChooseAttribute{

	  private double gainRatio; 
	
	  
	  public ChooseAttributeGR() { 
			super();
			gainRatio = -1;
	  }
	 
	
	
	/**
	 * Constructor: initialize fields
	 * @param target
	 * @param attributes
	 * @param instances
	 * @throws IOException
	 */
	  public ChooseAttributeGR(Attribute target, ArrayList<Attribute> attributes, 
				ArrayList<Instance> instances) throws IOException {
			
			// Initialize variables
			chosen = null;
			infoGain = -1;
			subset = null;

			// Iterate to find the attribute with the largest information gain
			for (Attribute currAttribute : attributes) {
				double currGainRatio = 0;
				HashMap<String, ArrayList<Instance>> currSubset = null;

				if (currAttribute.getType().equals("continuous")) {

					GainRatioContinuous gainRatio = new GainRatioContinuous(currAttribute, target, instances);
					currGainRatio = gainRatio.getGainRatio();
					currSubset = gainRatio.getSubset();
					threshold = gainRatio.getThreshold();
					
				} else {
					GainRatioDiscrete discrete1 = new GainRatioDiscrete(target, currAttribute, instances);
					currGainRatio = discrete1.getGainRatio();
					currSubset = discrete1.getSubset();

				}
				if (currGainRatio > gainRatio) {
					gainRatio = currGainRatio;
					chosen = currAttribute;
					subset = currSubset;
				}
			}
		}
		
		public Attribute getChosen() {
			return chosen;
		}
		
		public double getInfoGain() {
			return gainRatio;
		}
		
		public HashMap<String, ArrayList<Instance>> getSubset() {
			return subset;
		}
		
		public double getThreshold() {
			return threshold;
		}
	

}