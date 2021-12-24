package main;
import java.util.Scanner;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

import cv.CrossValidation;
import evaluate.EvaluateTree;
import gainratio.EvaluateTreeGR;
import gini.EvaluateTreeGI;
import input.ProcessInputData;
import output.PrintTree;

import static org.neo4j.driver.Values.parameters;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Value;
import org.neo4j.driver.util.Pair;


/**
 * 
 * This class is used to fetch nodes from graph database or from csv and call the functions to generate decision tree 
 * with confusion matrix, generation time and prediction time for the output 
 * 
 * @author minh dung
 *
 */

public class OutputDecisionTreeNeo4j implements AutoCloseable{
	
	private static Driver driver;	
	private static List<Record> dataKey = new ArrayList<>();
	private static ArrayList<String> testDataList =  new ArrayList<String>();
	private static ArrayList<String> trainDataList =  new ArrayList<String>();
	private static ArrayList<String> autoSplitDataList =  new ArrayList<String>();
	private static ArrayList<String> classificationDataList = new ArrayList<String>();
	
	/**
	 * Creation of driver object using bolt protocol
	 * @param uri Uniform resource identifier for bolt
	 * @param user Username
	 * @param password Password
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
	
	/**
	 * Close the driver object
	 */
	@Override
    public void close() throws Exception
    {
        driver.close();
    }
	
	
	public HashMap<String, ArrayList<String>> createDummmyDataHashMap ()
	{
		HashMap<String, ArrayList<String>> hashMapClassify = new HashMap<String, ArrayList<String>>();
		ArrayList<String> arrayList1 = new ArrayList<String>();
		arrayList1.add("anaemia:0, serum_creatinine:1.9, sex:1, ejection_fraction:20, creatinine_phosphokinase:582, platelets:265000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:0, time:4, serum_sodium:130, diabetes:0, age:75");
		arrayList1.add("anaemia:0, serum_creatinine:1.1, sex:1, ejection_fraction:38, creatinine_phosphokinase:7861, platelets:263358.03, DEATH_EVENT:1, high_blood_pressure:0, smoking:0, time:6, serum_sodium:136, diabetes:0, age:55");
		arrayList1.add("anaemia:0, serum_creatinine:1.3, sex:1, ejection_fraction:20, creatinine_phosphokinase:146, platelets:162000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:1, time:7, serum_sodium:129, diabetes:0, age:65");
		hashMapClassify.put("classLabel:1", arrayList1);
		ArrayList<String> arrayList2 = new ArrayList<String>();
		arrayList2.add("anaemia:1, serum_creatinine:1.9, sex:1, ejection_fraction:20, creatinine_phosphokinase:111, platelets:210000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:0, time:7, serum_sodium:137, diabetes:0, age:50");
		arrayList2.add("anaemia:1, serum_creatinine:2.7, sex:0, ejection_fraction:20, creatinine_phosphokinase:160, platelets:327000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:0, time:8, serum_sodium:116, diabetes:1, age:65");
		hashMapClassify.put("classLabel:0", arrayList2);
		return hashMapClassify;
	}
	
	public void connectNodeToClassLabel(final String message, final String classLabel, final String node)
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                	//a is present for the node
            		Result result = tx.run( "MERGE (a {" + node +"}) " +
            				"MERGE (b {" + classLabel +"}) " +
            				"MERGE (a)-[:link]->(b) "
            				+ "RETURN a.message");
				    return result.single().get( 0 ).asString();
                }
            } );
        }
    }
	
	 @UserFunction
	    public String classifyOfNodes(@Name("nodeType") String nodeType, @Name("classLabel") String targetAttribute ) throws Exception
	    {
	    	String output = "";
	    	classificationDataList.clear();
	    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
	        {
	    		HashMap<String, ArrayList<String>> hashMapClassify = new HashMap<String, ArrayList<String>>();
		    	hashMapClassify = createDummmyDataHashMap();
		    	for (String classLabel: hashMapClassify.keySet()) {
	        		ArrayList<String> arrayNodes = hashMapClassify.get(classLabel);
	        		for (String node : arrayNodes)
	        		{
	        			connector.connectNodeToClassLabel("create relationship in classification of nodes",classLabel,node);
	        		}
		    	}
		    	output = "classify successfully";
	        }
	 
	    	return output;
	    }
	
	
	/**
	 * 
	 * Create nodes in Neo4j using Java
	 * @param dtType Type of decision tree
	 * @param message String The message that print to Console
	 * @param nodeDetail ArrayList<String> Detail of a node
	 */
    public void createNode(final String dtType, final String message, final ArrayList<String> nodeDetail)
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
     * @param dtType Type of decision tree
     * @param message String the message that print to Console
     * @param relationshipDetail ArrayList<String> Detail of a relationship
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
     * This function is used to query the data from graph database
     * @param nodeType type of node
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
    
    
    /** 
     * This function is used to split the nodes from database based on training ratio given 
     * @param nodeType
     * @param trainRatio
     * @return String with train ratio and test ratio
     * @throws Exception
     */
    @UserFunction
    public String queryAutoSplitData(@Name("nodeType") String nodeType, @Name("trainRatio") String trainRatio ) throws Exception
   	{
       	String listOfData = "";
       	double testRatio = 0;
       	autoSplitDataList.clear();
       	testDataList.clear();
   		trainDataList.clear();
       	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
           {
       		queryData(nodeType);
       		for (Record key : dataKey) {
       			List<Pair<String,Value>> values = key.fields();
       			for (Pair<String,Value> nodeValues: values) {
       				String valueOfNode = "";
       				if ("n".equals(nodeValues.key())) { 
       			        Value value = nodeValues.value();
       			        for (String nodeKey : value.keys())
       			        {
       			        	if(value.get(nodeKey).getClass().equals(String.class))
       			        	{
       			        		if(valueOfNode != "")
       			        		{
       			        			valueOfNode = valueOfNode + ", " + nodeKey + ":" + value.get(nodeKey);
       			        		}
       			        		else
       			        		{
       			        			valueOfNode = nodeKey + ":" + value.get(nodeKey);
       			        		}
       			   
       			        	}
       			        	else
       			        	{
       			        		if(valueOfNode != "")
       			        		{
       			        			String converValueToString = String.valueOf(value.get(nodeKey));
               			        	valueOfNode = valueOfNode + ", " + nodeKey + ":" + converValueToString;
       			        		}
       			        		else
       			        		{
       			        			String converValueToString = String.valueOf(value.get(nodeKey));
               			        	valueOfNode =  nodeKey + ":" + converValueToString;
       			        		}   			        		
       			        	}
       			        }
       			        autoSplitDataList.add(valueOfNode);
       			    }
       			}
       		}
       		int size = autoSplitDataList.size();
       		double sizeForTrain = Math.floor(size * Double.parseDouble(trainRatio));
       		int startTestData =  (int) sizeForTrain;
       		testRatio = 1 - Double.parseDouble(trainRatio);
       		//Add data to trainDataList
       		for (int i = 0; i < startTestData; i++)
       		{
       			trainDataList.add(autoSplitDataList.get(i));
       		}
       		//Add data to testDataList
       		for (int i = startTestData; i < size; i++)
       		{
       			testDataList.add(autoSplitDataList.get(i));
       		}
       }
       	return "The Data has been split -  Train Ratio: " + trainRatio + " Test Ratio: " + testRatio;
   	}
    
    
    
    /**
     * This function is used to query the test dataset from Neo4j and populate the global arraylist of Java Code
     * @param nodeType The name of the type of node.For example, P_test for Test
     * @return String showing the data queried
     * @throws Exception if connection to Neo4j fails
     */
    @UserFunction
    public String queryTestData(@Name("nodeType") String nodeType) throws Exception
   	{
       	String listOfData = "";
       	testDataList.clear();
       	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
           {
       		queryData(nodeType);
       		for (Record key : dataKey) {
       			List<Pair<String,Value>> values = key.fields();
       			for (Pair<String,Value> nodeValues: values) {
       				String valueOfNode = "";
       				if ("n".equals(nodeValues.key())) { 
       			        Value value = nodeValues.value();
       			        for (String nodeKey : value.keys())
       			        {
       			        	if(value.get(nodeKey).getClass().equals(String.class))
       			        	{
       			        		if(valueOfNode != "")
    			        		{
    			        			String valueKey = ":" + value.get(nodeKey);
    			        			valueOfNode = valueOfNode + "," + nodeKey +  valueKey.replaceAll("^\"|\"$", "");
        			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
    			        		}
    			        		else
    			        		{
    			        			String valueKey = ":" + value.get(nodeKey);
    			        			valueOfNode = nodeKey + valueKey.replaceAll("^\"|\"$", "");
        			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
    			        		}
       			   
       			        	}
       			        	else
       			        	{
       			        		if(valueOfNode != "")
    			        		{
    			        			String converValueToString = String.valueOf(value.get(nodeKey));
            			        	valueOfNode = valueOfNode + "," + nodeKey + ":" + converValueToString.replaceAll("^\"|\"$", "");
            			        	//nodeData.add(nodeKey+":"+converValueToString);
    			        		}
    			        		else
    			        		{
    			        			String converValueToString = String.valueOf(value.get(nodeKey));
            			        	valueOfNode =  nodeKey + ":" + converValueToString.replaceAll("^\"|\"$", "");
            			        	//nodeData.add(nodeKey+":"+converValueToString);
    			        		}
       			        		
       			        	}
       			        }
       			        testDataList.add(valueOfNode);
       			    }
       				listOfData = listOfData + valueOfNode + " | ";
       			}
       		 }
           }
       	return "The Data: " + listOfData;
   	}
    
    /** 
     * This function is used to query the training dataset from Neo4j and populate the global trainDataList of Java Code
     * 
     * @param nodeType
     * @return
     * @throws Exception
     */
    
    @UserFunction
    public String queryTrainData(@Name("nodeType") String nodeType) throws Exception
	{
    	String listOfData = "";
    	trainDataList.clear();
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		queryData(nodeType);
    		for (Record key : dataKey) {
    			List<Pair<String,Value>> values = key.fields();
    			for (Pair<String,Value> nodeValues: values) {
    				String valueOfNode = "";
    				if ("n".equals(nodeValues.key())) { 
    			        Value value = nodeValues.value();
    			        for (String nodeKey : value.keys())
    			        {
    			        	if(value.get(nodeKey).getClass().equals(String.class))
    			        	{
    			        		if(valueOfNode != "")
    			        		{
    			        			String valueKey = ":" + value.get(nodeKey);
    			        			valueOfNode = valueOfNode + "," + nodeKey +  valueKey.replaceAll("^\"|\"$", "");
        			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
    			        		}
    			        		else
    			        		{
    			        			String valueKey = ":" + value.get(nodeKey);
    			        			valueOfNode = nodeKey + valueKey.replaceAll("^\"|\"$", "");
        			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
    			        		}
    			        	}
    			        	else
    			        	{
    			        		if(valueOfNode != "")
    			        		{
    			        			String converValueToString = String.valueOf(value.get(nodeKey));
            			        	valueOfNode = valueOfNode + "," + nodeKey + ":" + converValueToString.replaceAll("^\"|\"$", "");
            			        	//nodeData.add(nodeKey+":"+converValueToString);
    			        		}
    			        		else
    			        		{
    			        			String converValueToString = String.valueOf(value.get(nodeKey));
            			        	valueOfNode =  nodeKey + ":" + converValueToString.replaceAll("^\"|\"$", "");
            			        	//nodeData.add(nodeKey+":"+converValueToString);
    			        		}
    			        	}
    			        }
    			        trainDataList.add(valueOfNode);
    			    }
    				listOfData = listOfData + valueOfNode + " | ";
    			}
    		}
        }
    	return "The Data: " + listOfData;
	}
    /**
     * This function is used to display the nodes which has been queried and populated already. Used for testing purpose.
     * @param dataType 
     * @return String showing the data queried
     * @throws Exception if connection to Neo4j fails
     */
    
    @UserFunction
    public String displayData(@Name("dataType") String dataType) throws Exception
   	{
       	String listOfData = "";
       	int countLine = 0;
       	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
           {
       		if(dataType.equals("train"))
       		{
       			
       			listOfData = "train data: ";
       			for(String node : trainDataList)
       			{
       				countLine++;
       				listOfData = listOfData + node + "|";
       			}
       		}
       		else if (dataType.equals("all"))
       		{
       			
       			listOfData = "all data: ";
       			for(String node : autoSplitDataList)
       			{
       				countLine++;
       				listOfData = listOfData + node + "|";
       			}
       		}
       		else
       		{
       			
       			listOfData = "test data: ";
       			for(String node : testDataList)
       			{
       				countLine++;
       				listOfData = listOfData + node + "|";
       			}
       			
       		}
           }
       	return "Number of Lines: " + countLine + " The " + listOfData;
       	
   	}
    
    /**
     * User defined function to create the decision tree with nodes and relationships in neo4j. This creates a tree based on information gain. 
     * @param target attribute
     * @return
     * @throws Exception
     */
    @UserFunction
    public String createTreeIG(@Name("target") String target) throws Exception {
    	
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    	
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
    		return "Create the Information Gain Decision Tree successful, " + confusionMatrix;
        }
    	
    }
    
    /**
     * User defined function to create the decision tree with nodes and relationships in neo4j. This creates a tree based on gini index. 
     * @param target attribute
     * @return
     * @throws Exception
     */
    @UserFunction
    public String createTreeGI(@Name("target") String target) throws Exception {
    	
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    	
    		boolean isTrainListEmpty = trainDataList.isEmpty();
    		boolean isTestListEmpty = testDataList.isEmpty();
    		if(isTrainListEmpty && isTestListEmpty) {
    			return target + "False";
    		}else {
		
				EvaluateTreeGI mine = new EvaluateTreeGI(trainDataList, testDataList, target);

				confusionMatrix = mine.calculateAccuracy();

				PrintTree tree = new PrintTree();

				tree.createNodesForGraph(mine.getRoot());

				for (ArrayList<String> nodeDetail : tree.nodesBucket) {
					connector.createNode("DTGini", "create nodes in neo4j", nodeDetail);
				}

				for (ArrayList<String> relationshipDetail : tree.relationshipsBucket) {
					System.out.println("Relationship " + relationshipDetail);
					connector.createRelationship("DTGini", "create relationship in neo4j \n", relationshipDetail);
				}

    		}
    		return "Create the Gini Index Decision Tree successful, " + confusionMatrix;
        }
    	
    }
    
    /**
     * User defined function to create the decision tree with nodes and relationships in neo4j. This creates a tree based on gain ratio. 
     * @param target attribute
     * @return
     * @throws Exception
     */
    @UserFunction
    public String createTreeGR(@Name("target") String target) throws Exception {
    	
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    	
    		boolean isTrainListEmpty = trainDataList.isEmpty();
    		boolean isTestListEmpty = testDataList.isEmpty();
    		if(isTrainListEmpty && isTestListEmpty) {
    			return target + "False";
    		}else {
		
				EvaluateTreeGR mine = new EvaluateTreeGR(trainDataList, testDataList, target);

				confusionMatrix = mine.calculateAccuracy();

				PrintTree tree = new PrintTree();

				tree.createNodesForGraph(mine.getRoot());

				for (ArrayList<String> nodeDetail : tree.nodesBucket) {
					connector.createNode("DTGainRatio", "create nodes in neo4j", nodeDetail);
				}

				for (ArrayList<String> relationshipDetail : tree.relationshipsBucket) {
					System.out.println("Relationship " + relationshipDetail);
					connector.createRelationship("DTGainRatio", "create relationship in neo4j \n", relationshipDetail);
				}

    		}
    		return "Create the Gain Ratio Decision Tree successful, " + confusionMatrix;
        }
    	
    }
    
    
    /**
     * User defined function to create the decision tree with nodes and relationships in neo4j
     * @param path
     * @return
     * @throws Exception
     */
    @UserFunction
    public String createTreeGICsv(@Name("path") String path) throws Exception
	{
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			EvaluateTreeGI mine = new EvaluateTreeGI(paths[0], paths[1], paths[2]);

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
    /**
     * This function creates tree from csv path which is based on gain ratio
     * @param path The path is composed of 3 parts, 1st-training dataset, 2nd-test dataset, 3rd- target attribute(as string)
     * @return
     * @throws Exception
     */
    
    @UserFunction
    public String createTreeGRCsv(@Name("path") String path) throws Exception
	{
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			EvaluateTreeGR mine = new EvaluateTreeGR(paths[0], paths[1], paths[2]);

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
    
    
    /**
     * This function creates tree from csv path which is based on information gain
     * 
     * @param path - The path is composed of 3 parts, 1st-training dataset, 2nd-test dataset, 3rd- target attribute(as string)
     * @return
     * @throws Exception
     */
    
    @UserFunction
    public String createTreeIGCsv(@Name("path") String path) throws Exception
	{
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			EvaluateTree mine = new EvaluateTree(paths[0], paths[1], paths[2]);

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
    
    /**
     * This function retrieves the confusion matrix of decision tree based on information gain
     * @param path
     * @param target
     * @return
     * @throws Exception
     */
    @UserFunction
    @Description("retrieve the confusion matrix Information Gain Decision Tree")
	public String confmIG(@Name("path") String path,@Name("target") String target) throws Exception
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
			EvaluateTree mine = new EvaluateTree(paths[0], paths[1], target);

			confusionMatrix = mine.calculateAccuracy();
			return "The confusion Matrix for Information Gain DT : " + confusionMatrix;
		}
	}
    
    
    /**
     * 
     * This function retrieves the confusion matrix of decision tree based on gain ratio
     * @param path
     * @return
     * @throws Exception
     */
    
    @UserFunction
    @Description("retrieve the confusion matrix Gain Ratio Decision Tree")
	public String confmGR(@Name("path") String path) throws Exception
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
			EvaluateTreeGR mine = new EvaluateTreeGR(paths[0], paths[1], paths[2]);

			confusionMatrix = mine.calculateAccuracy();
			return "The confusion Matrix for Gain Ratio DT: " + confusionMatrix;
		}
	}
    
    /**
     * 
     * This function retrieves the confusion matrix of decision tree based on gini index 
     * @param path - The path is composed of 3 parts, 1st-training dataset, 2nd-test dataset, 3rd- target attribute(as string)
     * @return A string with confusion matrix
     * @throws Exception
     */
    
    @UserFunction
    @Description("retrieve the confusion matrix Gini Index Decision Tree")
	public String confmGI(@Name("path") String path) throws Exception
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
			EvaluateTreeGI mine = new EvaluateTreeGI(paths[0], paths[1], paths[2]);

			confusionMatrix = mine.calculateAccuracy();
			return "The confusion Matrix for Gini Index DT: " + confusionMatrix;
		}
	}
    
    @UserFunction
    @Description("cross validation time for data from graph database for InfoGain")
	public String cvIG(@Name("target") String target, @Name("numberOfFold") String numberOfFold) throws Exception
	{
		if(target == null)
		{
			return null;
		}
		else
		{
			
			CrossValidation cv = new CrossValidation(autoSplitDataList, target);
   			
			ArrayList<Double> final_score = cv.validate(Integer.parseInt(numberOfFold), "InfoGain");
			double mcc = cv.getMccAverage();
			double generateTime = cv.getCvGenerationTimeAverage();
			double score = cv.getScoreAverage();
			String result = "calculated average mcc: " + mcc + "\n" + "calculated average generateTime: " + generateTime + "\n" + "calculated average accuracy: " + score;
			 
	       
	        return result;
		}
	}
    
   
    
    
  
    @UserFunction
    @Description("cross validation time for data from graph database for GainRatio")
	public String cvGR(@Name("target") String target, @Name("numberOfFold") String numberOfFold) throws Exception
	{
		if(target == null)
		{
			return null;
		}
		else
		{
	        
			CrossValidation cv = new CrossValidation(autoSplitDataList, target);
   			
			ArrayList<Double> final_score = cv.validate(Integer.parseInt(numberOfFold), "GainRatio");
			double mcc = cv.getMccAverage();
			double generateTime = cv.getCvGenerationTimeAverage();
			double score = cv.getScoreAverage();
			String result = "calculated average mcc: " + mcc + "\n" + "calculated average generateTime: " + generateTime + "\n" + "calculated average accuracy: " + score;
			 
	       
	        return result;
		}
	}
    
    @UserFunction
    @Description("cross validation time for data from graph database for GiniIndex")
	public String cvGI(@Name("target") String target, @Name("numberOfFold") String numberOfFold) throws Exception
	{
		if(target == null)
		{
			return null;
		}
		else
		{
			 
	        CrossValidation cv = new CrossValidation(autoSplitDataList, target);
	       			
			ArrayList<Double> final_score = cv.validate(Integer.parseInt(numberOfFold), "GiniIndex");
			double mcc = cv.getMccAverage();
			double generateTime = cv.getCvGenerationTimeAverage();
			double score = cv.getScoreAverage();
			String result = "calculated average mcc: " + mcc + "\n" + "calculated average generateTime: " + generateTime + "\n" + "calculated average accuracy: " + score;
			 
	       
	        return result;
		}
	}
	
    @UserFunction
    @Description("cross validation time for data from csv for InfoGain")
	public String cvIGCsv(@Name("path") String path, @Name("target") String target, @Name("numberOfFold") String numberOfFold) throws Exception
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			ArrayList<String> customList = ProcessInputData.CustomListFromCSV(path);
			CrossValidation cv = new CrossValidation(customList, target);
			
			ArrayList<Double> final_score = cv.validate(Integer.parseInt(numberOfFold), "InfoGain");
			double mcc = cv.getMccAverage();
			double generateTime = cv.getCvGenerationTimeAverage();
			double score = cv.getScoreAverage();
			String result = "calculated average mcc: " + mcc + "\n" + "calculated average generateTime: " + generateTime + "\n" + "calculated average accuracy: " + score;
				 
	       
	        return result ;
		}
	}
    
    

    
    /**
     * To calculate the average of a list 
     * @param final_score
     * @return
     */

    private double calculateAverage(ArrayList<Double> final_score) {
        return final_score.stream()
                    .mapToDouble(d -> d)
                    .average()
                    .orElse(0.0);
    }
    
    
    @UserFunction
    @Description("cross validation time for data from csv for GainRatio")
	public String cvGRCsv(@Name("path") String path, @Name("target") String target, @Name("numberOfFold") String numberOfFold) throws Exception
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			
			ArrayList<String> customList = ProcessInputData.CustomListFromCSV(path);
			CrossValidation cv = new CrossValidation(customList, target);
			
			ArrayList<Double> final_score = cv.validate(Integer.parseInt(numberOfFold), "GainRatio");
			double mcc = cv.getMccAverage();
			double generateTime = cv.getCvGenerationTimeAverage();
			double score = cv.getScoreAverage();
			String result = "calculated average mcc: " + mcc + "\n" + "calculated average generateTime: " + generateTime + "\n" + "calculated average accuracy: " + score;
				 
	       
	        return result ;
		}
	}
    
    @UserFunction
    @Description("cross validation time for data from csv for GiniIndex")
	public String cvGICsv(@Name("path") String path, @Name("target") String target, @Name("numberOfFold") String numberOfFold) throws Exception
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			
			ArrayList<String> customList = ProcessInputData.CustomListFromCSV(path);
			CrossValidation cv = new CrossValidation(customList, target);
			
			ArrayList<Double> final_score = cv.validate(Integer.parseInt(numberOfFold), "GiniIndex");
			double mcc = cv.getMccAverage();
			double generateTime = cv.getCvGenerationTimeAverage();
			double score = cv.getScoreAverage();
			String result = "calculated average mcc: " + mcc + "\n" + "calculated average generateTime: " + generateTime + "\n" + "calculated average accuracy: " + score;
				 
	       
	        return result ;
		}
	}

}
