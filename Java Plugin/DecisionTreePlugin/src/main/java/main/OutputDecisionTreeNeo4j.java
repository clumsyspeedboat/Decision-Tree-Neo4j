package main;
import java.util.Scanner;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

import evaluate.EvaluateTree;
import gainratio.EvaluateTreeGR;
import gini.EvaluateTreeGI;
import output.PrintTree;

import static org.neo4j.driver.Values.parameters;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.util.Pair;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class OutputDecisionTreeNeo4j implements AutoCloseable{
	
	private static Driver driver;	
	public static List<Record> dataKey = new ArrayList<>();
	public static ArrayList<ArrayList<String>> testDataList =  new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> trainDataList =  new ArrayList<ArrayList<String>>();
	
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
					                            "' , value: '" + relationshipDetail.get(6) +
					                            "' , propname: '" + relationshipDetail.get(0) + "' }]->(b)" +
					                            " RETURN type(r)");
                    return result.single().get( 0 ).asString();
                }
            } );
        }
    }
    
    /**
     * Query Data
     * @param 
     * @param 
     */
    public void queryData( final String nodeType)
    {
    	try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "MATCH (n:" + nodeType + ") RETURN n");
                    dataKey = result.list();
                    return "Query Successful";
                }
            } );
        }
    }
    
    @UserFunction
    public String queryTestData(@Name("nodeType") String nodeType) throws Exception
	{
    	String listOfData = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		queryData(nodeType);
    		for (Record key : dataKey) {
    			List<Pair<String,Value>> values = key.fields();
    			for (Pair<String,Value> nodeValues: values) {
    				String valueOfNode = "";
    				ArrayList<String> nodeData =  new ArrayList<String>();
    				if ("n".equals(nodeValues.key())) { 
    			        Value value = nodeValues.value();
    			        for (String nodeKey : value.keys())
    			        {
    			        	if(value.get(nodeKey).getClass().equals(String.class))
    			        	{
    			        		if(valueOfNode != "")
    			        		{
    			        			valueOfNode = valueOfNode + ", " + nodeKey + ":" + value.get(nodeKey);
        			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
    			        		}
    			        		else
    			        		{
    			        			valueOfNode = nodeKey + ":" + value.get(nodeKey);
        			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
    			        		}
    			   
    			        	}
    			        	else
    			        	{
    			        		if(valueOfNode != "")
    			        		{
    			        			String converValueToString = String.valueOf(value.get(nodeKey));
            			        	valueOfNode = valueOfNode + ", " + nodeKey + ":" + converValueToString;
            			        	//nodeData.add(nodeKey+":"+converValueToString);
    			        		}
    			        		else
    			        		{
    			        			String converValueToString = String.valueOf(value.get(nodeKey));
            			        	valueOfNode =  nodeKey + ":" + converValueToString;
            			        	//nodeData.add(nodeKey+":"+converValueToString);
    			        		}
    			        		
    			        	}
    			        }
    			        nodeData.add(valueOfNode);
    			    }
    				listOfData = listOfData + valueOfNode + " | ";
    				testDataList.add(nodeData);
    			}
    		}
        }
    	
    	return "The Data: " + listOfData;
    	
	}
    
    @UserFunction
    public String queryTrainData(@Name("nodeType") String nodeType) throws Exception
	{
    	String listOfData = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		queryData(nodeType);
    		for (Record key : dataKey) {
    			List<Pair<String,Value>> values = key.fields();
    			for (Pair<String,Value> nodeValues: values) {
    				String valueOfNode = "";
    				ArrayList<String> nodeData =  new ArrayList<String>();
    				if ("n".equals(nodeValues.key())) { 
    			        Value value = nodeValues.value();
    			        for (String nodeKey : value.keys())
    			        {
    			        	if(value.get(nodeKey).getClass().equals(String.class))
    			        	{
    			        		if(valueOfNode != "")
    			        		{
    			        			valueOfNode = valueOfNode + ", " + nodeKey + ":" + value.get(nodeKey);
        			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
    			        		}
    			        		else
    			        		{
    			        			valueOfNode = nodeKey + ":" + value.get(nodeKey);
        			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
    			        		}
    			   
    			        	}
    			        	else
    			        	{
    			        		if(valueOfNode != "")
    			        		{
    			        			String converValueToString = String.valueOf(value.get(nodeKey));
            			        	valueOfNode = valueOfNode + ", " + nodeKey + ":" + converValueToString;
            			        	//nodeData.add(nodeKey+":"+converValueToString);
    			        		}
    			        		else
    			        		{
    			        			String converValueToString = String.valueOf(value.get(nodeKey));
            			        	valueOfNode =  nodeKey + ":" + converValueToString;
            			        	//nodeData.add(nodeKey+":"+converValueToString);
    			        		}
    			        		
    			        	}
    			        }
    			        nodeData.add(valueOfNode);
    			    }
    				listOfData = listOfData + valueOfNode + " | ";
    				trainDataList.add(nodeData);
    			}
    		}
        }
    	return "The Data: " + listOfData;
    	
	}
    
    @UserFunction
    public String displayData(@Name("dataType") String dataType) throws Exception
	{
    	String listOfData = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		if(dataType.equals("train"))
    		{
    			//FileWriter writer = new FileWriter("D:/Personal_Project/train_output.txt"); 
    			listOfData = "train data: ";
    			for(ArrayList<String> node : trainDataList)
    			{
    				String dataOfNode = "";
    				for(String nodeAttr : node)
    				{
    					dataOfNode = dataOfNode + nodeAttr + ", ";
    					//writer.write(nodeAttr + System.lineSeparator());
    				}
    				listOfData = listOfData + dataOfNode + ")";
    			}
    			try {
    			    FileOutputStream fos = new FileOutputStream("D:/Personal_Project/train_output.txt");
    			    ObjectOutputStream oos = new ObjectOutputStream(fos);   
    			    oos.writeObject(trainDataList); // write MenuArray to ObjectOutputStream
    			    oos.close(); 
    			} catch(Exception ex) {
    			    ex.printStackTrace();
    			}
    		}
    		else
    		{
    			//FileWriter writer = new FileWriter("D:/Personal_Project/test_output.txt"); 
    			listOfData = "test data: ";
    			for(ArrayList<String> node : testDataList)
    			{
    				String dataOfNode = "";
    				for(String nodeAttr : node)
    				{
    					dataOfNode = dataOfNode + nodeAttr + ", ";
    					//writer.write(nodeAttr + System.lineSeparator());
    				}
    				listOfData = listOfData + dataOfNode + "|";
    			}
    			//writer.close();
    			try {
    			    FileOutputStream fos = new FileOutputStream("D:/Personal_Project/test_output.txt");
    			    ObjectOutputStream oos = new ObjectOutputStream(fos);   
    			    oos.writeObject(testDataList); // write MenuArray to ObjectOutputStream
    			    oos.close(); 
    			} catch(Exception ex) {
    			    ex.printStackTrace();
    			}
    		}

        }
    	return "The " + listOfData;
    	
	}
    
    /**
     * User defined function to create the decision tree with nodes and relationships in neo4j
     * @param path
     * @return
     * @throws Exception
     */
    @UserFunction
    public String createTreeFromDB(@Name("target") String target) throws Exception {
    	
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		String listOfData = "";
    		
    		
    		boolean isTrainListEmpty = trainDataList.isEmpty();
    		boolean isTestListEmpty = testDataList.isEmpty();
    		if(isTrainListEmpty && isTestListEmpty) {
    			return target + "False";
    		}else {
		
				EvaluateTree mine = new EvaluateTree(trainDataList, testDataList, target);

				confusionMatrix = mine.calculateAccuracy();

				PrintTree tree = new PrintTree();

				tree.createNodesForGraph(mine.getRoot());

				for (ArrayList<String> nodeDetail : tree.nodesBucket) {
					connector.createNode("DTInfoGain", "create nodes in neo4j", nodeDetail);
				}

				for (ArrayList<String> relationshipDetail : tree.relationshipsBucket) {
					System.out.println("Relationship " + relationshipDetail);
					connector.createRelationship("DTInfoGain", "create relationship in neo4j \n", relationshipDetail);
				}
				 
				 
				 
    		}
    		return "Create the Information Gain Decision Tree successful, " + listOfData;
    		
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
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			EvaluateTreeGI mine = new EvaluateTreeGI(paths[0], paths[1]);

			confusionMatrix = mine.calculateAccuracy();

			PrintTree tree = new PrintTree();

			tree.createNodesForGraph(mine.getRoot());
			
			
			in.close();
			
			for (ArrayList<String> nodeDetail : tree.nodesBucket) {
				connector.createNode("DTGini","create nodes in neo4j", nodeDetail);
			}
			
			for (ArrayList<String> relationshipDetail : tree.relationshipsBucket) {
				System.out.println("Relationship " + relationshipDetail);
				connector.createRelationship("DTGini","create relationship in neo4j \n", relationshipDetail);
			}
        }
    	
    	return "Create the Gini Index Decision Tree successful, " + confusionMatrix;
    	
	}
    
    @UserFunction
    public String createTreeGainRatio(@Name("path") String path) throws Exception
	{
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			EvaluateTreeGR mine = new EvaluateTreeGR(paths[0], paths[1]);

			confusionMatrix = mine.calculateAccuracy();

			PrintTree tree = new PrintTree();

			tree.createNodesForGraph(mine.getRoot());
			
			
			in.close();
			
			for (ArrayList<String> nodeDetail : tree.nodesBucket) {
				connector.createNode("DTGainRatio","create nodes in neo4j", nodeDetail);
			}
			
			for (ArrayList<String> relationshipDetail : tree.relationshipsBucket) {
				System.out.println("Relationship " + relationshipDetail);
				connector.createRelationship("DTGainRatio","create relationship in neo4j \n" , relationshipDetail);
			}
        }
    	return "Create the Gain Ratio Decision Tree successful, " + confusionMatrix;
	}
    
    @UserFunction
    public String createTreeInfoGain(@Name("path") String path) throws Exception
	{
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			EvaluateTree mine = new EvaluateTree(paths[0], paths[1]);

			confusionMatrix = mine.calculateAccuracy();

			PrintTree tree = new PrintTree();

			tree.createNodesForGraph(mine.getRoot());
			
			
			in.close();
			
			for (ArrayList<String> nodeDetail : tree.nodesBucket) {
				connector.createNode("DTInfoGain","create nodes in neo4j", nodeDetail);
			}
			
			for (ArrayList<String> relationshipDetail : tree.relationshipsBucket) {
				System.out.println("Relationship " + relationshipDetail);
				connector.createRelationship("DTInfoGain","create relationship in neo4j \n", relationshipDetail);
			}
        }
    	
    	return "Create the Info Gain Decision Tree successful, " + confusionMatrix;
    	
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
			EvaluateTree mine = new EvaluateTree(paths[0], paths[1]);

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
			EvaluateTreeGR mine = new EvaluateTreeGR(paths[0], paths[1]);

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
			EvaluateTreeGI mine = new EvaluateTreeGI(paths[0], paths[1]);

			confusionMatrix = mine.calculateAccuracy();
			return "The confusion Matrix for Gini Index DT: " + confusionMatrix;
		}
	}
	
   

}
