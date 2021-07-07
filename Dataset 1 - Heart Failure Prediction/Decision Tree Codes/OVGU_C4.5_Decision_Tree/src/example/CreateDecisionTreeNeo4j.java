package example;
import java.util.List;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

/**
 *  This aim at passing csv directory into java plugin
 * @author minhd
 *
 */
public class CreateDecisionTreeNeo4j implements AutoCloseable{
	private static Driver driver;
	
	public CreateDecisionTreeNeo4j( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }
	public CreateDecisionTreeNeo4j()
    {
        driver = null;
    }
	
	@Override
    public void close() throws Exception
    {
        driver.close();
    }
	
	/**
	 * Create nodes in Neo4j using Java
	 * @param message String The message that print to Console
	 * @param nodeDetail ArrayList<String> Detail of a node
	 */
    public void createNode( final String message, final ArrayList<String> nodeDetail)
    {
    	final String name = nodeDetail.get(2);
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                	//a is present for the node
                    Result result = tx.run( "CREATE (" + nodeDetail.get(0) + nodeDetail.get(1) +") " +
					                            "SET a.name = $name" +
					                            " SET a.l = " + nodeDetail.get(3) +
					                            " SET a.i = " + nodeDetail.get(4) +
					                            " RETURN a.message + ', from node ' + id(a)",
					   parameters( "name", name ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }
    
    /**
     * Create relationship between nodes in Java
     * @param message String the message that print to Console
     * @param nodeDetail ArrayList<String> Detail of a relationship
     */
    public void createRelationship( final String message, final ArrayList<ArrayList<String>> relationshipDetail)
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "MATCH (a:DT), (b:DT) " +
					                            "Where a.name = '" + relationshipDetail.get(0).get(0) + "' And " +
					                            "a.l = " + relationshipDetail.get(0).get(1) + " And " +
					                            "b.name = '" + relationshipDetail.get(1).get(0) + "' And " +
					                            "b.l = " + relationshipDetail.get(1).get(1) +
					                            " Create (a)-[r:DT {type: '" + relationshipDetail.get(2).get(0) +
					                            "' , value: '" + relationshipDetail.get(2).get(1) +
					                            "' , propname: '" + relationshipDetail.get(2).get(2) + "' }]->(b)" +
					                            " RETURN type(r)");
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
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
    	decisionTreeRelationshipDetail3.add("Right");
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
    	decisionTreeRelationship2Detail3.add("Left");
    	decisionTreeRelationship2Detail3.add("73");
    	decisionTreeRelationship2Detail3.add("time");
    	decisionTreeRelationship2.add(decisionTreeRelationship2Detail);
    	decisionTreeRelationship2.add(decisionTreeRelationship2Detail2);
    	decisionTreeRelationship2.add(decisionTreeRelationship2Detail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship2);
    	
    	// Create Array Relationship 3
    	ArrayList<ArrayList<String>> decisionTreeRelationship3 = new ArrayList<ArrayList<String>>();
    	//Relationship 1 - 1st element
    	ArrayList<String> decisionTreeRelationship3Detail = new ArrayList<String>();
    	decisionTreeRelationship3Detail.add("serum_creatinine");
    	decisionTreeRelationship3Detail.add("1");
    	//Relationship 1 - 2nd element
    	ArrayList<String> decisionTreeRelationship3Detail2 = new ArrayList<String>();
    	decisionTreeRelationship3Detail2.add("ejection_fraction");
    	decisionTreeRelationship3Detail2.add("2");
    	//Relationship 1 - 3rd element
    	ArrayList<String> decisionTreeRelationship3Detail3 = new ArrayList<String>();
    	decisionTreeRelationship3Detail3.add("Right");
    	decisionTreeRelationship3Detail3.add("1.18");
    	decisionTreeRelationship3Detail3.add("serum_creatinine");
    	decisionTreeRelationship3.add(decisionTreeRelationship3Detail);
    	decisionTreeRelationship3.add(decisionTreeRelationship3Detail2);
    	decisionTreeRelationship3.add(decisionTreeRelationship3Detail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship3);
    	
    	// Create Array Relationship 4
    	ArrayList<ArrayList<String>> decisionTreeRelationship4 = new ArrayList<ArrayList<String>>();
    	//Relationship 1 - 1st element
    	ArrayList<String> decisionTreeRelationship4Detail = new ArrayList<String>();
    	decisionTreeRelationship4Detail.add("serum_creatinine");
    	decisionTreeRelationship4Detail.add("1");
    	//Relationship 1 - 2nd element
    	ArrayList<String> decisionTreeRelationship4Detail2 = new ArrayList<String>();
    	decisionTreeRelationship4Detail2.add("no");
    	decisionTreeRelationship4Detail2.add("2");
    	//Relationship 1 - 3rd element
    	ArrayList<String> decisionTreeRelationship4Detail3 = new ArrayList<String>();
    	decisionTreeRelationship4Detail3.add("Left");
    	decisionTreeRelationship4Detail3.add("1.18");
    	decisionTreeRelationship4Detail3.add("serum_creatinine");
    	decisionTreeRelationship4.add(decisionTreeRelationship4Detail);
    	decisionTreeRelationship4.add(decisionTreeRelationship4Detail2);
    	decisionTreeRelationship4.add(decisionTreeRelationship4Detail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship4);
    	
    	// Create Array Relationship 5
    	ArrayList<ArrayList<String>> decisionTreeRelationship5 = new ArrayList<ArrayList<String>>();
    	//Relationship 1 - 1st element
    	ArrayList<String> decisionTreeRelationship5Detail = new ArrayList<String>();
    	decisionTreeRelationship5Detail.add("ejection_fraction");
    	decisionTreeRelationship5Detail.add("2");
    	//Relationship 1 - 2nd element
    	ArrayList<String> decisionTreeRelationship5Detail2 = new ArrayList<String>();
    	decisionTreeRelationship5Detail2.add("sex");
    	decisionTreeRelationship5Detail2.add("3");
    	//Relationship 1 - 3rd element
    	ArrayList<String> decisionTreeRelationship5Detail3 = new ArrayList<String>();
    	decisionTreeRelationship5Detail3.add("Right");
    	decisionTreeRelationship5Detail3.add("20");
    	decisionTreeRelationship5Detail3.add("ejection_fraction");
    	decisionTreeRelationship5.add(decisionTreeRelationship5Detail);
    	decisionTreeRelationship5.add(decisionTreeRelationship5Detail2);
    	decisionTreeRelationship5.add(decisionTreeRelationship5Detail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship5);
    	
    	// Create Array Relationship 6
    	ArrayList<ArrayList<String>> decisionTreeRelationship6 = new ArrayList<ArrayList<String>>();
    	//Relationship 1 - 1st element
    	ArrayList<String> decisionTreeRelationship6Detail = new ArrayList<String>();
    	decisionTreeRelationship6Detail.add("ejection_fraction");
    	decisionTreeRelationship6Detail.add("2");
    	//Relationship 1 - 2nd element
    	ArrayList<String> decisionTreeRelationship6Detail2 = new ArrayList<String>();
    	decisionTreeRelationship6Detail2.add("yes");
    	decisionTreeRelationship6Detail2.add("3");
    	//Relationship 1 - 3rd element
    	ArrayList<String> decisionTreeRelationship6Detail3 = new ArrayList<String>();
    	decisionTreeRelationship6Detail3.add("Left");
    	decisionTreeRelationship6Detail3.add("20");
    	decisionTreeRelationship6Detail3.add("ejection_fraction");
    	decisionTreeRelationship6.add(decisionTreeRelationship6Detail);
    	decisionTreeRelationship6.add(decisionTreeRelationship6Detail2);
    	decisionTreeRelationship6.add(decisionTreeRelationship6Detail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship6);
    	
    	// Create Array Relationship 7
    	ArrayList<ArrayList<String>> decisionTreeRelationship7 = new ArrayList<ArrayList<String>>();
    	//Relationship 1 - 1st element
    	ArrayList<String> decisionTreeRelationship7Detail = new ArrayList<String>();
    	decisionTreeRelationship7Detail.add("sex");
    	decisionTreeRelationship7Detail.add("3");
    	//Relationship 1 - 2nd element
    	ArrayList<String> decisionTreeRelationship7Detail2 = new ArrayList<String>();
    	decisionTreeRelationship7Detail2.add("time");
    	decisionTreeRelationship7Detail2.add("4");
    	//Relationship 1 - 3rd element
    	ArrayList<String> decisionTreeRelationship7Detail3 = new ArrayList<String>();
    	decisionTreeRelationship7Detail3.add("Right");
    	decisionTreeRelationship7Detail3.add("1");
    	decisionTreeRelationship7Detail3.add("sex");
    	decisionTreeRelationship7.add(decisionTreeRelationship7Detail);
    	decisionTreeRelationship7.add(decisionTreeRelationship7Detail2);
    	decisionTreeRelationship7.add(decisionTreeRelationship7Detail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship7);
    	
    	// Create Array Relationship 8
    	ArrayList<ArrayList<String>> decisionTreeRelationship8 = new ArrayList<ArrayList<String>>();
    	//Relationship 1 - 1st element
    	ArrayList<String> decisionTreeRelationship8Detail = new ArrayList<String>();
    	decisionTreeRelationship8Detail.add("sex");
    	decisionTreeRelationship8Detail.add("3");
    	//Relationship 1 - 2nd element
    	ArrayList<String> decisionTreeRelationship8Detail2 = new ArrayList<String>();
    	decisionTreeRelationship8Detail2.add("no");
    	decisionTreeRelationship8Detail2.add("4");
    	//Relationship 1 - 3rd element
    	ArrayList<String> decisionTreeRelationship8Detail3 = new ArrayList<String>();
    	decisionTreeRelationship8Detail3.add("Left");
    	decisionTreeRelationship8Detail3.add("1");
    	decisionTreeRelationship8Detail3.add("sex");
    	decisionTreeRelationship8.add(decisionTreeRelationship8Detail);
    	decisionTreeRelationship8.add(decisionTreeRelationship8Detail2);
    	decisionTreeRelationship8.add(decisionTreeRelationship8Detail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship8);
    	
    	// Create Array Relationship 9
    	ArrayList<ArrayList<String>> decisionTreeRelationship9 = new ArrayList<ArrayList<String>>();
    	//Relationship 1 - 1st element
    	ArrayList<String> decisionTreeRelationship9Detail = new ArrayList<String>();
    	decisionTreeRelationship9Detail.add("time");
    	decisionTreeRelationship9Detail.add("4");
    	//Relationship 1 - 2nd element
    	ArrayList<String> decisionTreeRelationship9Detail2 = new ArrayList<String>();
    	decisionTreeRelationship9Detail2.add("yes");
    	decisionTreeRelationship9Detail2.add("5");
    	//Relationship 1 - 3rd element
    	ArrayList<String> decisionTreeRelationship9Detail3 = new ArrayList<String>();
    	decisionTreeRelationship9Detail3.add("Right");
    	decisionTreeRelationship9Detail3.add("1");
    	decisionTreeRelationship9Detail3.add("time");
    	decisionTreeRelationship9.add(decisionTreeRelationship9Detail);
    	decisionTreeRelationship9.add(decisionTreeRelationship9Detail2);
    	decisionTreeRelationship9.add(decisionTreeRelationship9Detail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship9);
    	
    	// Create Array Relationship 10
    	ArrayList<ArrayList<String>> decisionTreeRelationship10 = new ArrayList<ArrayList<String>>();
    	//Relationship 1 - 1st element
    	ArrayList<String> decisionTreeRelationship10Detail = new ArrayList<String>();
    	decisionTreeRelationship10Detail.add("time");
    	decisionTreeRelationship10Detail.add("4");
    	//Relationship 1 - 2nd element
    	ArrayList<String> decisionTreeRelationship10Detail2 = new ArrayList<String>();
    	decisionTreeRelationship10Detail2.add("no");
    	decisionTreeRelationship10Detail2.add("5");
    	//Relationship 1 - 3rd element
    	ArrayList<String> decisionTreeRelationship10Detail3 = new ArrayList<String>();
    	decisionTreeRelationship10Detail3.add("Left");
    	decisionTreeRelationship10Detail3.add("1");
    	decisionTreeRelationship10Detail3.add("time");
    	decisionTreeRelationship10.add(decisionTreeRelationship10Detail);
    	decisionTreeRelationship10.add(decisionTreeRelationship10Detail2);
    	decisionTreeRelationship10.add(decisionTreeRelationship10Detail3);
    	decisionTreeRelationshipRelationships.add(decisionTreeRelationship10);
    	
    	return decisionTreeRelationshipRelationships;
    }
	
	@UserFunction
	public String something(@Name("path") String path)
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			return "path to csv file: " + path;
		}
	}
	
	@UserFunction
	public String createTree(@Name("path") String path) throws Exception
	{
		try ( CreateDecisionTreeNeo4j connecter = new CreateDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
        	ArrayList<ArrayList<String>> decisionTreeNodes = new ArrayList<ArrayList<String>>();
        	decisionTreeNodes = dummyNodeData();
        	System.out.println(decisionTreeNodes.size());
        	for (ArrayList<String> nodeDetail : decisionTreeNodes)
        	{
        		System.out.println( "Node" + nodeDetail.get(2));
        		connecter.createNode( "create nodes in neo4j", nodeDetail);
        	}
        	System.out.println( "----------------Create Relationship--------------------");
        	ArrayList<ArrayList<ArrayList<String>>> decisionTreeRelationshipRelationships = new ArrayList<ArrayList<ArrayList<String>>>();
        	decisionTreeRelationshipRelationships = dummyRelationShipData();
        	System.out.println( "Relationship " + decisionTreeRelationshipRelationships);
        	for (ArrayList<ArrayList<String>> relationshipDetail : decisionTreeRelationshipRelationships)
        	{
        		System.out.println( "Relationship " + relationshipDetail);
        		connecter.createRelationship( "create relationship in neo4j", relationshipDetail);
        	}
        }
		return "Create the tree successful, path: " +path;
	}
	
}
