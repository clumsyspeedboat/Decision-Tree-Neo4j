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
import java.io.FileWriter;
import java.util.ArrayList;
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
       			        	if(valueOfNode != "")
			        		{
    			        		String valueKey = ":" + value.get(nodeKey);
    			        		//valueKey = valueKey.replaceAll("^[\"']+|[\"']+$", "");
			        			valueOfNode = valueOfNode + "," + nodeKey + valueKey;
			        		}
			        		else
			        		{
			        			String valueKey = ":" + value.get(nodeKey);
			        			//valueKey = valueKey.replaceAll("^[\"']+|[\"']+$", "");
			        			valueOfNode = nodeKey + valueKey;
			        		}
//       			        	if(value.get(nodeKey).getClass().equals(String.class))
//       			        	{
//       			        		if(valueOfNode != "")
//       			        		{
//       			        			valueOfNode = valueOfNode + "," + nodeKey + ":" + value.get(nodeKey);
//       			        		}
//       			        		else
//       			        		{
//       			        			valueOfNode = nodeKey + ":" + value.get(nodeKey);
//       			        		}
//       			   
//       			        	}
//       			        	else
//       			        	{
//       			        		if(valueOfNode != "")
//       			        		{
//       			        			String converValueToString = String.valueOf(value.get(nodeKey));
//               			        	valueOfNode = valueOfNode + "," + nodeKey + ":" + converValueToString;
//       			        		}
//       			        		else
//       			        		{
//       			        			String converValueToString = String.valueOf(value.get(nodeKey));
//               			        	valueOfNode =  nodeKey + ":" + converValueToString;
//       			        		}
//       			        	}
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
    			        	if(valueOfNode != "")
			        		{
    			        		String valueKey = ":" + value.get(nodeKey);
    			        		valueKey = valueKey.replaceAll("^[\"']+|[\"']+$", "");
			        			valueOfNode = valueOfNode + "," + nodeKey + valueKey;
    			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
			        		}
			        		else
			        		{
			        			String valueKey = ":" + value.get(nodeKey);
			        			//valueKey = valueKey.replaceAll("^[\"']+|[\"']+$", "");
			        			valueOfNode = nodeKey + valueKey;
    			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
			        		}
//    			        	if(value.get(nodeKey).getClass().equals(String.class))
//    			        	{
//    			        		if(valueOfNode != "")
//    			        		{
//    			        			valueOfNode = valueOfNode + "," + nodeKey + ":" + value.get(nodeKey);
//        			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
//    			        		}
//    			        		else
//    			        		{
//    			        			valueOfNode = nodeKey + ":" + value.get(nodeKey);
//        			        		//nodeData.add(nodeKey+":"+value.get(nodeKey));
//    			        		}
//    			        	}
//    			        	else
//    			        	{
//    			        		if(valueOfNode != "")
//    			        		{
//    			        			String converValueToString = String.valueOf(value.get(nodeKey));
//            			        	valueOfNode = valueOfNode + "," + nodeKey + ":" + converValueToString;
//            			        	//nodeData.add(nodeKey+":"+converValueToString);
//    			        		}
//    			        		else
//    			        		{
//    			        			String converValueToString = String.valueOf(value.get(nodeKey));
//            			        	valueOfNode =  nodeKey + ":" + converValueToString;
//            			        	//nodeData.add(nodeKey+":"+converValueToString);
//    			        		}
//    			        	}
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
    public String createTreeInfoGainFromDB(@Name("target") String target) throws Exception {
    	
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
    public String createTreeGiniFromDB(@Name("target") String target) throws Exception {
    	
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
    public String createTreeGainRatioFromDB(@Name("target") String target) throws Exception {
    	
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
    public String createTreeGini(@Name("path") String path) throws Exception
	{
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			ArrayList<String> trainFile = ProcessInputData.CustomListFromCSV(paths[0]);
			ArrayList<String> testFile = ProcessInputData.CustomListFromCSV(paths[1]);
			EvaluateTreeGI mine = new EvaluateTreeGI(trainFile, testFile, paths[2]);

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
    public String createTreeGainRatio(@Name("path") String path) throws Exception
	{
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			
			ArrayList<String> trainFile = ProcessInputData.CustomListFromCSV(paths[0]);
			ArrayList<String> testFile = ProcessInputData.CustomListFromCSV(paths[1]);
			EvaluateTreeGR mine = new EvaluateTreeGR(trainFile, testFile, paths[2]);

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
    public String createTreeInfoGain(@Name("path") String path) throws Exception
	{
    	String confusionMatrix = "";
    	try ( OutputDecisionTreeNeo4j connector = new OutputDecisionTreeNeo4j( "bolt://localhost:7687", "neo4j", "123" ) )
        {
    		Scanner in = new Scanner(System.in);

			String[] paths = path.split(",");
			ArrayList<String> trainFile = ProcessInputData.CustomListFromCSV(paths[0]);
			ArrayList<String> testFile = ProcessInputData.CustomListFromCSV(paths[1]);
			
			EvaluateTree mine = new EvaluateTree(trainFile, testFile, paths[2]);

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
	public String confusionMatrixIG(@Name("path") String path,@Name("target") String target) throws Exception
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
			EvaluateTreeGI mine = new EvaluateTreeGI(paths[0], paths[1], paths[2]);

			confusionMatrix = mine.calculateAccuracy();
			return "The confusion Matrix for Gini Index DT: " + confusionMatrix;
		}
	}
    
    @UserFunction
    @Description("cross validation time for data from graph database")
	public String crossValidationFromDB(@Name("target") String target, @Name("numberOfFold") String numberOfFold) throws Exception
	{
		if(target == null)
		{
			return null;
		}
		else
		{
			 CrossValidation cv = new CrossValidation(autoSplitDataList, target);

	        ArrayList<Double> final_score = cv.validate(Integer.parseInt(numberOfFold));	        
	        
	        double r = 0;
	        double gt = 0;
	        
	        for(int i = 0; i < final_score.size(); i++) {
	            r += final_score.get(i);
	        }
	        
	        if(cv.getCvGenerationTime() != null) {
	        	ArrayList<Double> totalGenerationTime = cv.getCvGenerationTime();
	        	for(int i = 0; i < totalGenerationTime.size(); i++) {
	                gt += totalGenerationTime.get(i);
	                
	            }
	        }
	       
	        String accuracyPruning = "Cross Validation Accuracy before Pruning: " + (r / 10) * 100 + "%";
	        String timePruning = "Cross Validation Generation Time before pruning: " + (gt/10) + "sec";
	        return accuracyPruning + " \n " + timePruning ;
		}
	}
	
    @UserFunction
    @Description("cross validation time for data from csv")
	public String crossValidationFromCSV(@Name("path") String path, @Name("target") String target, @Name("numberOfFold") String numberOfFold) throws Exception
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			ArrayList<String> customList = ProcessInputData.CustomListFromCSV(path);
			
			CrossValidation cv = new CrossValidation(customList, target);

	        ArrayList<Double> final_score = cv.validate(Integer.parseInt(numberOfFold));	        
	        
	        double r = 0;
	        double gt = 0;
	        
	        for(int i = 0; i < final_score.size(); i++) {
	            r += final_score.get(i);
	        }
	        
	        if(cv.getCvGenerationTime() != null) {
	        	ArrayList<Double> totalGenerationTime = cv.getCvGenerationTime();
	        	for(int i = 0; i < totalGenerationTime.size(); i++) {
	                gt += totalGenerationTime.get(i);
	                
	            }
	        }
	       
	        String accuracyPruning = "Cross Validation Accuracy before Pruning: " + (r / 10) * 100 + "%";
	        String timePruning = "Cross Validation Generation Time before pruning: " + (gt/10) + "sec";
	        return accuracyPruning + " \n " + timePruning ;
		}
	}

}
