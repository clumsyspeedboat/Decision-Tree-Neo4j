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
    			        		valueOfNode = valueOfNode + ", " + nodeKey + ":" + value.get(nodeKey);
    			        		nodeData.add(nodeKey+":"+value.get(nodeKey));
    			        	}
    			        	else
    			        	{
    			        		String converValueToString = String.valueOf(value.get(nodeKey));
        			        	valueOfNode = valueOfNode + ", " + nodeKey + ":" + converValueToString;
        			        	nodeData.add(nodeKey+":"+converValueToString);
    			        	}
    			        }
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
    			        		valueOfNode = valueOfNode + "," + nodeKey + ":" + value.get(nodeKey);
    			        		nodeData.add(nodeKey+":" +value.get(nodeKey));
    			        	}
    			        	else
    			        	{
    			        		String convertValueToString = String.valueOf(value.get(nodeKey));
        			        	valueOfNode = valueOfNode+ ", "+nodeKey+":"+convertValueToString;
        			        	nodeData.add(nodeKey+ ":" +convertValueToString);
    			        	}
    			        }
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
    			listOfData = "train data: ";
    			for(ArrayList<String> node : trainDataList)
    			{
    				String dataOfNode = "";
    				for(String nodeAttr : node)
    				{
    					dataOfNode = dataOfNode + nodeAttr + ", ";
    				}
    				listOfData = listOfData + dataOfNode + ")";
    			}
    		}
    		else
    		{
    			listOfData = "test data: ";
    			for(ArrayList<String> node : testDataList)
    			{
    				String dataOfNode = "";
    				for(String nodeAttr : node)
    				{
    					dataOfNode = dataOfNode + nodeAttr + ", ";
    				}
    				listOfData = listOfData + dataOfNode + "|";
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

    			ArrayList<ArrayList<String>> trainList = new ArrayList<>();
    			ArrayList<String> b1= new ArrayList<String>();
    			b1.add("anaemia:1, serum_creatinine:1.2, sex:1, ejection_fraction:40, creatinine_phosphokinase:170, platelets:336000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:0, time:250, serum_sodium:135, diabetes:1, age:'55.0'");
    			ArrayList<String> b2= new ArrayList<String>();
    			b2.add("anaemia:0, serum_creatinine:1.4, sex:1, ejection_fraction:50, creatinine_phosphokinase:224, platelets:481000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:1, time:192, serum_sodium:138, diabetes:0, age:'78.0'");
    			ArrayList<String> b3= new ArrayList<String>();
    			b3.add("anaemia:1, serum_creatinine:0.8, sex:0, ejection_fraction:40, creatinine_phosphokinase:101, platelets:226000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:0, time:187, serum_sodium:141, diabetes:0, age:'40.0'");
    			ArrayList<String> b4= new ArrayList<String>();
    			b4.add("anaemia:0, serum_creatinine:1.18, sex:0, ejection_fraction:60, creatinine_phosphokinase:582, platelets:263358.03, DEATH_EVENT:0, high_blood_pressure:0, smoking:0, time:82, serum_sodium:137, diabetes:0, age:'42.0'");
    			ArrayList<String> b5= new ArrayList<String>();
    			b5.add("anaemia:0, serum_creatinine:0.9, sex:1, ejection_fraction:25, creatinine_phosphokinase:231, platelets:253000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:1, time:10, serum_sodium:140, diabetes:0, age:'62.0'");
    			ArrayList<String> b6= new ArrayList<String>();
    			b6.add("anaemia:1, serum_creatinine:1.0, sex:0, ejection_fraction:35, creatinine_phosphokinase:249, platelets:319000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:0, time:28, serum_sodium:128, diabetes:1, age:'50.0'");
    			ArrayList<String> b7= new ArrayList<String>();
    			b7.add("anaemia:0, serum_creatinine:1.2, sex:1, ejection_fraction:35, creatinine_phosphokinase:60, platelets:228000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:1, time:90, serum_sodium:135, diabetes:0, age:'55.0'");
    			ArrayList<String> b8= new ArrayList<String>();
    			b8.add("anaemia:1, serum_creatinine:0.9, sex:0, ejection_fraction:40, creatinine_phosphokinase:84, platelets:229000.0, DEATH_EVENT:0, high_blood_pressure:1, smoking:0, time:110, serum_sodium:141, diabetes:0, age:'61.0'");
    			ArrayList<String> b9= new ArrayList<String>();
    			b9.add("anaemia:0, serum_creatinine:0.9, sex:1, ejection_fraction:25, creatinine_phosphokinase:1380, platelets:271000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:0, time:38, serum_sodium:130, diabetes:0, age:'51.0'");
    			ArrayList<String> b10= new ArrayList<String>();
    			b10.add("anaemia:1, serum_creatinine:2.7, sex:0, ejection_fraction:20, creatinine_phosphokinase:160, platelets:327000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:0, time:8, serum_sodium:116, diabetes:1, age:'65.0'");
    			ArrayList<String> b11= new ArrayList<String>();
    			b11.add("anaemia:0, serum_creatinine:3.8, sex:1, ejection_fraction:30, creatinine_phosphokinase:64, platelets:215000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:1, time:250, serum_sodium:128, diabetes:0, age:'42.0'");
    			ArrayList<String> b12= new ArrayList<String>();
    			b12.add("anaemia:0, serum_creatinine:0.9, sex:1, ejection_fraction:35, creatinine_phosphokinase:198, platelets:281000.0, DEATH_EVENT:0, high_blood_pressure:1, smoking:1, time:146, serum_sodium:137, diabetes:1, age:'65.0'");
    			ArrayList<String> b13= new ArrayList<String>();
    			b13.add("anaemia:1, serum_creatinine:1.0, sex:0, ejection_fraction:30, creatinine_phosphokinase:80, platelets:427000.0, DEATH_EVENT:0, high_blood_pressure:1, smoking:0, time:12, serum_sodium:138, diabetes:0, age:'49.0'");
    			ArrayList<String> b14= new ArrayList<String>();
    			b14.add("anaemia:0, serum_creatinine:1.1, sex:1, ejection_fraction:38, creatinine_phosphokinase:582, platelets:25100.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:0, time:246, serum_sodium:140, diabetes:1, age:'70.0'");
    			ArrayList<String> b15= new ArrayList<String>();
    			b15.add("anaemia:0, serum_creatinine:1.3, sex:1, ejection_fraction:45, creatinine_phosphokinase:748, platelets:263000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:0, time:88, serum_sodium:137, diabetes:0, age:'55.0'");
    			ArrayList<String> b16= new ArrayList<String>();
    			b16.add("anaemia:1, serum_creatinine:1.8, sex:1, ejection_fraction:50, creatinine_phosphokinase:55, platelets:172000.0, DEATH_EVENT:0, high_blood_pressure:1, smoking:0, time:78, serum_sodium:133, diabetes:0, age:'79.0'");
    			ArrayList<String> b17= new ArrayList<String>();
    			b17.add("anaemia:1, serum_creatinine:1.5, sex:1, ejection_fraction:35, creatinine_phosphokinase:582, platelets:263358.03, DEATH_EVENT:0, high_blood_pressure:0, smoking:1, time:145, serum_sodium:136, diabetes:1, age:'51.0'");
    			ArrayList<String> b18= new ArrayList<String>();
    			b18.add("anaemia:0, serum_creatinine:1.0, sex:1, ejection_fraction:38, creatinine_phosphokinase:132, platelets:253000.0, DEATH_EVENT:0, high_blood_pressure:1, smoking:0, time:230, serum_sodium:139, diabetes:1, age:'58.0'");
    			ArrayList<String> b19= new ArrayList<String>();
    			b19.add("anaemia:0, serum_creatinine:0.9, sex:0, ejection_fraction:45, creatinine_phosphokinase:244, platelets:275000.0, DEATH_EVENT:0, high_blood_pressure:1, smoking:0, time:174, serum_sodium:140, diabetes:0, age:'40.0'");
    			ArrayList<String> b20= new ArrayList<String>();
    			b20.add("anaemia:0, serum_creatinine:1.1, sex:1, ejection_fraction:25, creatinine_phosphokinase:2017, platelets:314000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:0, time:214, serum_sodium:138, diabetes:0, age:'55.0'");
    			
    			
    		
    			
    			trainList.add(b1);
    			trainList.add(b2);
    			trainList.add(b3);
    			trainList.add(b4);
    			trainList.add(b5);
    			trainList.add(b6);
    			trainList.add(b7);
    			trainList.add(b8);
    			trainList.add(b9);
    			trainList.add(b10);
    			trainList.add(b11);
    			trainList.add(b12);
    			trainList.add(b13);
    			trainList.add(b14);
    			trainList.add(b15);
    			trainList.add(b16);
    			trainList.add(b17);
    			trainList.add(b18);
    			trainList.add(b19);
    			trainList.add(b20);
    			
    			
    			
    			ArrayList<ArrayList<String>> testList = new ArrayList<>();
    			ArrayList<String> t1= new ArrayList<String>();
    			t1.add("anaemia:0, serum_creatinine:1.1, sex:0, ejection_fraction:35, creatinine_phosphokinase:618, platelets:327000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:0, time:245, serum_sodium:142, diabetes:0, age:'70.0'");
    			ArrayList<String> t2= new ArrayList<String>();
    			t2.add("anaemia:1, serum_creatinine:1.4, sex:1, ejection_fraction:30, creatinine_phosphokinase:159, platelets:302000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:0, time:29, serum_sodium:138, diabetes:0, age:'50.0'");
    			ArrayList<String> t3= new ArrayList<String>();
    			t3.add("anaemia:1, serum_creatinine:0.8, sex:0, ejection_fraction:35, creatinine_phosphokinase:335, platelets:235000.0, DEATH_EVENT:0, high_blood_pressure:1, smoking:0, time:120, serum_sodium:136, diabetes:0, age:'65.0'");
    			ArrayList<String> t4= new ArrayList<String>();
    			t4.add("anaemia:1, serum_creatinine:1.7, sex:0, ejection_fraction:30, creatinine_phosphokinase:328, platelets:621000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:1, time:88, serum_sodium:138, diabetes:0, age:'72.0'");
    			ArrayList<String> t5= new ArrayList<String>();
    			t5.add("anaemia:0, serum_creatinine:0.9, sex:1, ejection_fraction:35, creatinine_phosphokinase:582, platelets:122000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:1, time:71, serum_sodium:139, diabetes:1, age:'58.0'");
    			ArrayList<String> t6= new ArrayList<String>();
    			t6.add("anaemia:1, serum_creatinine:1.0, sex:0, ejection_fraction:25, creatinine_phosphokinase:125, platelets:237000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:0, time:15, serum_sodium:140, diabetes:0, age:'70.0'");
    			ArrayList<String> t7= new ArrayList<String>();
    			t7.add("anaemia:0, serum_creatinine:2.9, sex:1, ejection_fraction:20, creatinine_phosphokinase:68, platelets:119000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:1, time:64, serum_sodium:127, diabetes:0, age:'60.0'");
    			ArrayList<String> t8= new ArrayList<String>();
    			t8.add("anaemia:1, serum_creatinine:1.1, sex:1, ejection_fraction:38, creatinine_phosphokinase:168, platelets:276000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:0, time:11, serum_sodium:137, diabetes:0, age:'50.0'");
    			ArrayList<String> t9= new ArrayList<String>();
    			t9.add("anaemia:0, serum_creatinine:1.3, sex:1, ejection_fraction:45, creatinine_phosphokinase:122, platelets:284000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:1, time:26, serum_sodium:136, diabetes:1, age:'70.0'");
    			ArrayList<String> t10= new ArrayList<String>();
    			t10.add("anaemia:1, serum_creatinine:1.1, sex:0, ejection_fraction:60, creatinine_phosphokinase:588, platelets:194000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:0, time:33, serum_sodium:142, diabetes:1, age:'60.0'");
    			
    			testList.add(t1);
    			testList.add(t2);
    			testList.add(t3);
    			testList.add(t4);
    			testList.add(t5);
    			testList.add(t6);
    			testList.add(t7);
    			testList.add(t8);
    			testList.add(t9);
    			testList.add(t10);
    			
    			
    		    if(trainDataList.equals(trainList)) {
    		    	listOfData = listOfData+"train data is same";
    		    }
    		    
    		    if(testDataList.equals(testList)) {
    		    	listOfData = listOfData+"test data is same";
    		    }
    		    
    		    
   
				/*
				 * EvaluateTree mine = new EvaluateTree(trainDataList, testDataList, target);
				 * 
				 * 
				 * confusionMatrix = mine.calculateAccuracy();
				 * 
				 * PrintTree tree = new PrintTree();
				 * 
				 * tree.createNodesForGraph(mine.getRoot());
				 * 
				 * 
				 * 
				 * for (ArrayList<String> nodeDetail : tree.nodesBucket) {
				 * connector.createNode("DTInfoGain", "create nodes in neo4j", nodeDetail); }
				 * 
				 * for (ArrayList<String> relationshipDetail : tree.relationshipsBucket) {
				 * System.out.println("Relationship " + relationshipDetail);
				 * connector.createRelationship("DTInfoGain", "create relationship in neo4j \n",
				 * relationshipDetail); }
				 */
				 
				 
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
