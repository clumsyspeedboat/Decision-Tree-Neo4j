package main;
import java.util.Scanner;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

import Gini.C45MineDataGI;
import evaluate.C45MineData;
import gainratio.GainRatioMineData;
import output.PrintTree;

import static org.neo4j.driver.Values.parameters;

import java.io.IOException;
import java.util.ArrayList;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

public class OutputDecisionTreeNeo4j implements AutoCloseable{
	
	private static Driver driver;
	
	
	/**
	 * Creation of driver object using bolt protocol
	 * @param uri
	 * @param user
	 * @param password
	 */
	public OutputDecisionTreeNeo4j( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }
	/**
	 * Empty constructor
	 */
	
	public OutputDecisionTreeNeo4j()
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
    public void createNode( final String dtType, final String message, final ArrayList<String> nodeDetail)
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
            		Result result = tx.run( "CREATE (" + nodeDetail.get(0) + dtType + nodeDetail.get(1) +") " +
                            "SET a.name = $name" +
                            " SET a.l = " + nodeDetail.get(3) +
                            " SET a.i = " + nodeDetail.get(4) +
                            " SET a.dupValue = " + nodeDetail.get(5) +
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
    public void createRelationship( final String dtType, final String message, final ArrayList<String> relationshipDetail)
    {
    	try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "MATCH (a:" + dtType + "), (b:" + dtType + ") " +
					                            "Where a.name = '" + relationshipDetail.get(0) + "' And " +
					                            "a.l = " + relationshipDetail.get(1) + " And " +
					                            "a.dupValue = " + relationshipDetail.get(2) + " And " +
					                            "b.name = '" + relationshipDetail.get(3) + "' And " +
					                            "b.dupValue = " + relationshipDetail.get(5) + " And " +
					                            "b.l = " + relationshipDetail.get(4) +
					                            " Create (a)-[r:DT {type: '" + relationshipDetail.get(7) +
					                            "' , value: " + relationshipDetail.get(6) +
					                            " , propname: '" + relationshipDetail.get(0) + "' }]->(b)" +
					                            " RETURN type(r)");
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }
    
    
    /**
     * User defined function to create the decision tree with nodes and relationships in neo4j
     * @param path
     * @return
     * @throws Exception
     */

    @UserFunction
    public String createTreeGini(@Name("path") String path) throws Exception
	{
    	
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
			
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			C45MineDataGI mine = new C45MineDataGI(paths[0], paths[1]);

			mine.calculateAccuracy();

			PrintTree tree = new PrintTree();

			tree.createNodesForGraph(mine.getRoot());
			
			
			in.close();
			
			for (ArrayList<String> nodeDetail : tree.nodesBucket) {
				connector.createNode("DTGini","create nodes in neo4j", nodeDetail);
			}
			
			for (ArrayList<String> relationshipDetail : tree.relationshipsBucket) {
				System.out.println("Relationship " + relationshipDetail);
				connector.createRelationship("DTGini","create relationship in neo4j", relationshipDetail);
			}
        }
    	
    	return "Create the Gini Index Decision Tree successful, path: " + path;
    	
	}
    
    @UserFunction
    public String createTreeGain(@Name("path") String path) throws Exception
	{
    	
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
			
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			GainRatioMineData mine = new GainRatioMineData(paths[0], paths[1]);

			mine.calculateAccuracy();

			PrintTree tree = new PrintTree();

			tree.createNodesForGraph(mine.getRoot());
			
			
			in.close();
			
			for (ArrayList<String> nodeDetail : tree.nodesBucket) {
				connector.createNode("DTGain","create nodes in neo4j", nodeDetail);
			}
			
			for (ArrayList<String> relationshipDetail : tree.relationshipsBucket) {
				System.out.println("Relationship " + relationshipDetail);
				connector.createRelationship("DTGain","create relationship in neo4j", relationshipDetail);
			}
        }
    	
    	return "Create the Gain Ratio Decision Tree successful, path: " + path;
    	
	}
    
    @UserFunction
    public String createTreeInformation(@Name("path") String path) throws Exception
	{
    	
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
			
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			C45MineData mine = new C45MineData(paths[0], paths[1]);

			mine.calculateAccuracy();

			PrintTree tree = new PrintTree();

			tree.createNodesForGraph(mine.getRoot());
			
			
			in.close();
			
			for (ArrayList<String> nodeDetail : tree.nodesBucket) {
				connector.createNode("DTInformation","create nodes in neo4j", nodeDetail);
			}
			
			for (ArrayList<String> relationshipDetail : tree.relationshipsBucket) {
				System.out.println("Relationship " + relationshipDetail);
				connector.createRelationship("DTInformation","create relationship in neo4j", relationshipDetail);
			}
        }
    	
    	return "Create the Gain Ratio Decision Tree successful, path: " + path;
    	
	}
    
    @UserFunction
    @Description("retrieve the confusion matrix Information Gain Decision Tree")
	public String confusionMatrixIG(@Name("path") String path) throws Exception
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			String confusionMatrix = "";
			Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			C45MineData mine = new C45MineData(paths[0], paths[1]);

			confusionMatrix = mine.calculateAccuracy();
			return "The confusion Matrix for Information Gain DT : " + confusionMatrix;
		}
	}
    
    @UserFunction
    @Description("retrieve the confusion matrix Gain Ratio Decision Tree")
	public String confusionMatrixGR(@Name("path") String path) throws Exception
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			String confusionMatrix = "";
			Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			GainRatioMineData mine = new GainRatioMineData(paths[0], paths[1]);

			confusionMatrix = mine.calculateAccuracy();
			return "The confusion Matrix for Gain Ratio DT: " + confusionMatrix;
		}
	}
    
    @UserFunction
    @Description("retrieve the confusion matrix Gini Index Decision Tree")
	public String confusionMatrixGI(@Name("path") String path) throws Exception
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			String confusionMatrix = "";
			Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			C45MineDataGI mine = new C45MineDataGI(paths[0], paths[1]);

			confusionMatrix = mine.calculateAccuracy();
			return "The confusion Matrix for Gini Index DT: " + confusionMatrix;
		}
	}
	

}
