import java.util.ArrayList;
import java.util.Hashtable;

public class test {
	
	public static void main(String []args) {
		ArrayList<ArrayList<ArrayList<String>>> decisionTreeRelationshipRelationships = new ArrayList<ArrayList<ArrayList<String>>>();
    	decisionTreeRelationshipRelationships = dummyRelationShipData();
    	System.out.println( "Relationship " + decisionTreeRelationshipRelationships);
    	
	}
	
	public static ArrayList<ArrayList<String>> dummyNodeData()
    {
    	ArrayList<ArrayList<String>> decisionTreeNodes = new ArrayList<ArrayList<String>>();
    	//Node 0
    	ArrayList<String> nodeDetail = new ArrayList<String>();
    	//a is present the node
    	nodeDetail.add("a");
    	//DT type the node, Split is the kind node (there are 2 kind: Split and Terminal) Split is for parent nodes, Terminal for leave nodes
    	nodeDetail.add(":DT:Split");
    	//Name of the node
    	nodeDetail.add("time");
    	//Level of the node
    	nodeDetail.add("0");
    	//Index of the node
    	nodeDetail.add("0");
    	
    	
    	//Node 1
    	ArrayList<String> nodeDetail2 = new ArrayList<String>();
    	nodeDetail2.add("a");
    	nodeDetail2.add(":DT:Split");
    	nodeDetail2.add("serum_creatinine");
    	nodeDetail2.add("1");
    	nodeDetail2.add("1");
    	
    	//Node 2
    	ArrayList<String> nodeDetail3 = new ArrayList<String>();
    	nodeDetail3.add("a");
    	nodeDetail3.add(":DT:Split");
    	nodeDetail3.add("ejection_fraction");
    	nodeDetail3.add("2");
    	nodeDetail3.add("1");
    	
    	
    	//Node 3 
    	ArrayList<String> nodeDetail4 = new ArrayList<String>();
    	nodeDetail4.add("a");
    	nodeDetail4.add(":DT:Split");
    	nodeDetail4.add("sex");
    	nodeDetail4.add("3");
    	nodeDetail4.add("1");
    	
    	//Node 4
    	ArrayList<String> nodeDetail5 = new ArrayList<String>();
    	nodeDetail5.add("a");
    	nodeDetail5.add(":DT:Split");
    	nodeDetail5.add("time");
    	nodeDetail5.add("4");
    	nodeDetail5.add("1");
    	
    	
    	//Node 5
    	ArrayList<String> nodeDetail6 = new ArrayList<String>();
    	nodeDetail6.add("a");
    	nodeDetail6.add(":DT:Terminal");
    	nodeDetail6.add("yes");
    	nodeDetail6.add("1");
    	nodeDetail6.add("0");
    	
    	
    	//Node 6
    	ArrayList<String> nodeDetail7 = new ArrayList<String>();
    	nodeDetail7.add("a");
    	nodeDetail7.add(":DT:Terminal");
    	nodeDetail7.add("no");
    	nodeDetail7.add("2");
    	nodeDetail7.add("0");
    	
    	
    	//Node 7
    	ArrayList<String> nodeDetail8 = new ArrayList<String>();
    	nodeDetail8.add("a");
    	nodeDetail8.add(":DT:Terminal");
    	nodeDetail8.add("yes");
    	nodeDetail8.add("3");
    	nodeDetail8.add("0");
    	
    	
    	
    	//Node 8
    	ArrayList<String> nodeDetail9 = new ArrayList<String>();
    	nodeDetail9.add("a");
    	nodeDetail9.add(":DT:Terminal");
    	nodeDetail9.add("no");
    	nodeDetail9.add("4");
    	nodeDetail9.add("0");
    	//Node 9
    	ArrayList<String> nodeDetail10 = new ArrayList<String>();
    	nodeDetail10.add("a");
    	nodeDetail10.add(":DT:Terminal");
    	nodeDetail10.add("yes");
    	nodeDetail10.add("5");
    	nodeDetail10.add("0");
    	//Node 10
    	ArrayList<String> nodeDetail11 = new ArrayList<String>();
    	nodeDetail11.add("a");
    	nodeDetail11.add(":DT:Terminal");
    	nodeDetail11.add("no");
    	nodeDetail11.add("5");
    	nodeDetail11.add("1");
    	
    	decisionTreeNodes.add(nodeDetail);
    	decisionTreeNodes.add(nodeDetail2);
    	decisionTreeNodes.add(nodeDetail3);
    	decisionTreeNodes.add(nodeDetail4);
    	decisionTreeNodes.add(nodeDetail5);
    	decisionTreeNodes.add(nodeDetail6);
    	decisionTreeNodes.add(nodeDetail7);
    	decisionTreeNodes.add(nodeDetail8);
    	decisionTreeNodes.add(nodeDetail9);
    	decisionTreeNodes.add(nodeDetail10);
    	decisionTreeNodes.add(nodeDetail11);
    	return decisionTreeNodes;
    }
	
	public static ArrayList<ArrayList<ArrayList<String>>> dummyRelationShipData()
    {
    	//To hold all relationships
    	ArrayList<ArrayList<ArrayList<String>>> decisionTreeRelationshipRelationships = new ArrayList<ArrayList<ArrayList<String>>>();


    	// Create Array Relationship 1
    	// To create 1 relationship
    	ArrayList<ArrayList<String>> decisionTreeRelationship = new ArrayList<ArrayList<String>>();

    	//Relationship 1 - 1st element
    	// Hold the information of the 1st node
    	ArrayList<String> decisionTreeRelationshipDetail = new ArrayList<String>();
    	// 1st node name
    	decisionTreeRelationshipDetail.add("time");
    	// 1st node level
    	decisionTreeRelationshipDetail.add("0");


    	//Relationship 1 - 2nd element
    	// Hold the information of the 2nd node
    	ArrayList<String> decisionTreeRelationshipDetail2 = new ArrayList<String>();
    	// 2nd node name
    	decisionTreeRelationshipDetail2.add("serum_creatinine");
    	// 2nd node level
    	decisionTreeRelationshipDetail2.add("1");


    	//Relationship 1 - 3rd element
    	// Hold the information of the relationship
    	ArrayList<String> decisionTreeRelationshipDetail3 = new ArrayList<String>();
    	// The relationship type
    	decisionTreeRelationshipDetail3.add("left");
    	// The relationship value
    	decisionTreeRelationshipDetail3.add("73");
    	// The relationship first node name.
    	decisionTreeRelationshipDetail3.add("time");


    	decisionTreeRelationship.add(decisionTreeRelationshipDetail);
    	decisionTreeRelationship.add(decisionTreeRelationshipDetail2);
    	decisionTreeRelationship.add(decisionTreeRelationshipDetail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship);

    	
    	// Create Array Relationship 2
    	ArrayList<ArrayList<String>> decisionTreeRelationship2 = new ArrayList<ArrayList<String>>();
    	//Relationship 1 - 1st element
    	ArrayList<String> decisionTreeRelationship2Detail = new ArrayList<String>();
    	decisionTreeRelationship2Detail.add("time");
    	decisionTreeRelationship2Detail.add("0");

    	//Relationship 1 - 2nd element
    	ArrayList<String> decisionTreeRelationship2Detail2 = new ArrayList<String>();
    	decisionTreeRelationship2Detail2.add("yes");
    	decisionTreeRelationship2Detail2.add("1");

    	//Relationship 1 - 3rd element
    	ArrayList<String> decisionTreeRelationship2Detail3 = new ArrayList<String>();
    	decisionTreeRelationship2Detail3.add("Right");
    	decisionTreeRelationship2Detail3.add("73");
    	decisionTreeRelationship2Detail3.add("time");




    	decisionTreeRelationship2.add(decisionTreeRelationship2Detail);
    	decisionTreeRelationship2.add(decisionTreeRelationship2Detail2);
    	decisionTreeRelationship2.add(decisionTreeRelationship2Detail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship2);
    	return decisionTreeRelationshipRelationships;
    }
	
	

}
