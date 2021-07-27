package main;

/**
 * Main class to build the decision tree and test on the test dataset
 */

import java.io.IOException;
import java.util.Scanner;

import evaluate.C45MineData;
import output.PrintTree;

public class ClassifyMain {
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		long strTime = System.nanoTime();
		
		
		String pathos = "data/train.csv,data/test.csv";
		
		String[] paths = pathos.split(",");
		
		C45MineData mine = new C45MineData(paths[0], paths[1]);
		
	    
		mine.calculateAccuracy();
		
		long eTime = System.nanoTime();
	    long elTime = eTime - strTime;
	    double mTime = (double) elTime / 1000000.0;
	    System.out.println("Time taken to generate prediction: " + mTime + " ms\n");
		
	    
	    PrintTree tree = new PrintTree();
	    
		/*
		 * ArrayList<String> res = tree.printDFS(mine.getRoot());
		 * System.out.println(res);
		 */
		
		tree.createNodesForGraph(mine.getRoot());
		
		in.close();
	}
}