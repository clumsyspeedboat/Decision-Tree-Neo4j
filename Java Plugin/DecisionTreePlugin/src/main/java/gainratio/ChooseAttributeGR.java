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
				double currInfoGain, currSplitinfo, currGainRatio = 0;
				HashMap<String, ArrayList<Instance>> currSubset = null;

				if (currAttribute.getType().equals("continuous")) {
					InfoGainContinuous continuous = new InfoGainContinuous(currAttribute, target, instances);
					currInfoGain = continuous.getInfoGain();
					currSubset = continuous.getSubset();
					threshold = continuous.getThreshold();

					SplitInfoContinuous contigous1 = new SplitInfoContinuous(currAttribute, target, instances);
					currSplitinfo = contigous1.getSplitInfo();
					currSubset = contigous1.getSubset();
					
					currGainRatio = currInfoGain / currSplitinfo;
				} else {
					SplitInfoDiscrete discrete1 = new SplitInfoDiscrete(target, currAttribute, instances);
					currSplitinfo = discrete1.getSplitInfo();
					currSubset = discrete1.getSubset();

					InfoGainDiscrete discrete = new InfoGainDiscrete(target, currAttribute, instances);
					currInfoGain = discrete.getInfoGain();
					currSubset = discrete.getSubset();

					currGainRatio = (double) currInfoGain / (double) currSplitinfo;
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