package main;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import scala.util.Random;

public class Unsupervised {
	
	public static ArrayList<String> dummyData(){
		ArrayList<String> data = new ArrayList<String>();
//		String line1 ="anaemia:0,serum_creatinine:1.9,sex:1,ejection_fraction:20,creatinine_phosphokinase:582,platelets:265000.0,DEATH_EVENT:1,high_blood_pressure:1,smoking:0,time:4,serum_sodium:130,diabetes:0,age:75";
//		String line2 ="anaemia:0,serum_creatinine:1.1,sex:1,ejection_fraction:38,creatinine_phosphokinase:7861,platelets:263358.03,DEATH_EVENT:1,high_blood_pressure:0,smoking:0,time:6,serum_sodium:136,diabetes:0,age:55";
//		String line3 ="anaemia:0,serum_creatinine:1.3,sex:1,ejection_fraction:20,creatinine_phosphokinase:146,platelets:162000.0,DEATH_EVENT:1,high_blood_pressure:0,smoking:1,time:7,serum_sodium:129,diabetes:0,age:65";
//		String line4 ="anaemia:1,serum_creatinine:1.9,sex:1,ejection_fraction:20,creatinine_phosphokinase:111,platelets:210000.0,DEATH_EVENT:1,high_blood_pressure:0,smoking:0,time:7,serum_sodium:137,diabetes:0,age:50";
//		String line5 ="anaemia:1,serum_creatinine:2.7,sex:0,ejection_fraction:20,creatinine_phosphokinase:160,platelets:327000.0,DEATH_EVENT:1,high_blood_pressure:0,smoking:0,time:8,serum_sodium:116,diabetes:1,age:65|anaemia:1,serum_creatinine:2.1,sex:1,ejection_fraction:40,creatinine_phosphokinase:47,platelets:204000.0,DEATH_EVENT:1,high_blood_pressure:1,smoking:1,time:8,serum_sodium:132,diabetes:0,age:90";
		String line1 = "anaemia:0,serum_creatinine:1.9";
		String line2 = "anaemia:0,serum_creatinine:1.1";
		String line3 = "anaemia:0,serum_creatinine:1.3";
		String line4 = "anaemia:1,serum_creatinine:1.9";
		String line5 = "anaemia:1,serum_creatinine:2.7";
		data.add(line1);
		data.add(line2);
		data.add(line3);
		data.add(line4);
		data.add(line5);
		return data;
	}
	
	/**
	 * This is the main method to perform k-means clustering.
	 * @param inputData is a variable where the nodes from Neo4j are stored
	 * @param numberOfCentroids store the number of centroids specified by user for clustering
	 * @param numberOfInteration saves user specified iteration to find convergence
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> KmeanClust (ArrayList<String> inputData, int numberOfCentroids, int numberOfInteration, String distanceMeasure)
	{
		HashMap<String, ArrayList<String>> kmeanAssign = new HashMap<String, ArrayList<String>>();
		ArrayList<String> listOfCentroid = new ArrayList<String>();
		ArrayList<String> listOfRemain = new ArrayList<String>(inputData);
		// Initializing centroid by random choice
		for(int i = 0; i < numberOfCentroids; i++)
		{
			Random rand = new Random();
			int randomNum = rand.nextInt((listOfRemain.size()-1 - 0) + 1) + 0;
			listOfCentroid.add(listOfRemain.get(randomNum));
			listOfRemain.remove(randomNum);
		}
		// First clusters
		HashMap<String, ArrayList<String>> hashClusterAssign = distanceAssign(listOfCentroid,listOfRemain, distanceMeasure);
		// All iterations
		kmeanAssign = kmeanInteration(hashClusterAssign,numberOfInteration,inputData);
		for (String name: kmeanAssign.keySet()) {
		    ArrayList<String> something = kmeanAssign.get(name);
		    System.out.println(name);
		    System.out.println(something);
		}
		return kmeanAssign;
	}
	
	/**
	 * Method to perform the iterations of k-means
	 * @param clusterAssign contains the first cluster assignments
	 * @param numberOfInteration specified by user
	 * @param inputData specified by user
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> kmeanInteration (HashMap<String, ArrayList<String>> clusterAssign, int numberOfInteration, ArrayList<String> inputData)
	{
		ArrayList<String> listOfCentroid = new ArrayList<String>();
		ArrayList<String> listOfNewCentroid = new ArrayList<String>();
		for(int i = 0; i < numberOfInteration; i++)
		{
			listOfCentroid.clear();
			if(i == 0)
			{
				for (String key : clusterAssign.keySet()) 
				{
					clusterAssign.get(key).add(key);
					String newCentroid = calculateNewCentroid(clusterAssign.get(key));
					listOfCentroid.add(newCentroid);
				}	
			}
			else
			{
				for (String key: clusterAssign.keySet())
				{
					String newCentroid = calculateNewCentroid(clusterAssign.get(key));
					listOfCentroid.add(newCentroid);
				}
			}
		}
		return clusterAssign;
	}
	
	/**
	 * Method to calculate new centroid points after each iteration
	 * @param listOfNodesInCluster nodes assigned to each cluster
	 * @return returns new centroids after each iteration
	 */
	public static String calculateNewCentroid (ArrayList<String> listOfNodesInCluster)
	{
		String[] atrributeName = new String[listOfNodesInCluster.get(0).split(",").length];
		Double[] atrributeValue = new Double[listOfNodesInCluster.get(0).split(",").length];
		for (String node : listOfNodesInCluster)
		{
			for (int i=0; i<atrributeValue.length;i++)
			{
				if(atrributeValue[i]==null)
				{
					atrributeValue[i] = Double.parseDouble(node.split(",")[i].split(":")[1]);
					atrributeName[i] = node.split(",")[i].split(":")[0];
				}
				else
				{
					atrributeValue[i] = atrributeValue[i] + Double.parseDouble(node.split(",")[i].split(":")[1]);
				}
			}
		}
		String newCentroid = "";
		for (int i = 0; i < atrributeValue.length; i++)
		{
			atrributeValue[i] = atrributeValue[i]/listOfNodesInCluster.size();
			if (i == 0)
			{
				newCentroid = newCentroid + atrributeName[i] + ":" + atrributeValue[i];
			}
			else
			{
				newCentroid = newCentroid + "," +atrributeName[i] + ":" + atrributeValue[i] + ",";
			}
		}
		return newCentroid;
	}
	
	/**
	 * This is the first iteration of k-means clustering algorithm
	 * @param listOfCentroid contains all the initial centroid points 
	 * @param listOfRemain contains the points which have been initialized as centroids
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> distanceAssign (ArrayList<String> listOfCentroid, ArrayList<String> listOfRemain, String distanceMeasure)
	{
		HashMap<String, ArrayList<String>> hashClusterAssign = new HashMap<String, ArrayList<String>>();
		// Calculate distance and assign points to clusters
		for(int i = 0; i < listOfRemain.size(); i++)
		{
			double minDistance = 0;
			ArrayList<String> cluster = new ArrayList<String>();
			String clusterNode = "";
			for(int j = 0; j < listOfCentroid.size(); j++)
			{
				double distance = 0.0;
				
				if(distanceMeasure == "euclidean") {
					distance = calEuclideanDist(listOfRemain.get(i),listOfCentroid.get(j));	
				}
				else if(distanceMeasure == "manhattan") {
					distance = calManhattanDist(listOfRemain.get(i),listOfCentroid.get(j));	
				}
				else if(distanceMeasure == "cosine") {
					distance = calCosineSimilarity(listOfRemain.get(i),listOfCentroid.get(j));
				}
				else if(distanceMeasure == "bray-curtis") {
					distance = calBrayCurtis(listOfRemain.get(i),listOfCentroid.get(j));
				}
				
				if(minDistance == 0)
				{
					minDistance = distance;
					clusterNode = listOfCentroid.get(j);
				}
				else if(distance<minDistance)
				{
					minDistance = distance;
					clusterNode = listOfCentroid.get(j);
				}
			}
			ArrayList<String> valueHashMap = hashClusterAssign.get(clusterNode);
			if(valueHashMap != null)
			{
				valueHashMap.add(listOfRemain.get(i));
			}
			else
			{
				ArrayList<String> tempArray = new ArrayList<String>();
				tempArray.add(listOfRemain.get(i));
				hashClusterAssign.put(clusterNode, tempArray);
			}
		}
		return hashClusterAssign;
	}
	
	/**
	 * Euclidean distance calculation from point A to point B
	 * @param start point A
	 * @param end point B
	 * @return
	 */
	public static double calEuclideanDist (String start, String end)
	{
		double distance = 0.00;
		String[] startSplit =  start.split(",");
		String[] endSplit = end.split(",");
		for(int i = 0; i < startSplit.length; i++)
		{
			float startValue = Float.parseFloat(startSplit[i].split(":")[1]);
			float endValue = Float.parseFloat(endSplit[i].split(":")[1]);
			distance = distance + Math.pow((startValue-endValue),2);
		}
		distance = Math.sqrt(distance);
		return distance;
		
	}
	/**
	 * Calculate Manhattan distance between point A and B
	 * @param start point A
	 * @param end point B
	 * @return
	 */
	public static double calManhattanDist (String start, String end)
	{
		double distance = 0.00;
		String[] startSplit =  start.split(",");
		String[] endSplit = end.split(",");
		for(int i = 0; i < startSplit.length; i++)
		{
			float startValue = Float.parseFloat(startSplit[i].split(":")[1]);
			float endValue = Float.parseFloat(endSplit[i].split(":")[1]);
			distance = distance + Math.abs(startValue - endValue);
		}
		return distance;
		
	}
	/**
	 * Calculate Cosine similarity between point A and B
	 * @param start point A
	 * @param end point B
	 * @return
	 */
	public static double calCosineSimilarity (String start, String end)
	{
		double distance = 0.00;
		double dotProduct = 0.00;
		double normA = 0.00;
		double normB = 0.00;
		String[] startSplit =  start.split(",");
		String[] endSplit = end.split(",");
		for(int i = 0; i < startSplit.length; i++)
		{
			float startValue = Float.parseFloat(startSplit[i].split(":")[1]);
			float endValue = Float.parseFloat(endSplit[i].split(":")[1]);
			dotProduct += startValue * endValue;
			normA += Math.pow(startValue, 2);
			normB += Math.pow(endValue, 2);
		}
		distance = dotProduct/ (Math.sqrt(normA) * Math.sqrt(normB));
		return distance;
		
	}
	/**
	 * Calculate Bray-Curtis dissimilarity between point A and B
	 * @param start point A
	 * @param end point B
	 * @return
	 */
	public static double calBrayCurtis (String start, String end)
	{
		double distance = 0.00;
		double num = 0.00;
		double den = 0.00;
		
		String[] startSplit =  start.split(",");
		String[] endSplit = end.split(",");
		for(int i = 0; i < startSplit.length; i++)
		{
			float startValue = Float.parseFloat(startSplit[i].split(":")[1]);
			float endValue = Float.parseFloat(endSplit[i].split(":")[1]);
			num = num + Math.abs(startValue - endValue);
			den = den + Math.abs(startValue + endValue);
		}
		distance = num/den;
		return distance;
		
	}

//	public static void main(String[] args) {
//		ArrayList<String> inputData = dummyData();
//		KmeanClust(inputData,2,5);
//	}
}
