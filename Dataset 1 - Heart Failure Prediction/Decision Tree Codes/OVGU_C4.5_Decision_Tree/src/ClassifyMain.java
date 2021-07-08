/**
 * Main class to build the decision tree and test on the test dataset
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import MineData.C45MineData;
import ProcessOutput.PrintTree;

public class ClassifyMain {
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		long startTime = System.nanoTime();
		
		C45MineData mine = new C45MineData("data/train.csv", "data/test.csv");
		
		mine.calculateAccuracy();
		
		
		long endTime = System.nanoTime();
	    long elapsedTime = endTime - startTime;
	    double msTime = (double) elapsedTime / 1000000.0;
	    System.out.println("Time taken: " + msTime + " ms\n");
		
	    
	    PrintTree tree = new PrintTree();
	    //ArrayList<String> res = tree.printDFS(mine.getRoot());
	    tree.createNodesForGraph(mine.getRoot());
	    //System.out.println(res);
	    
	    
		/*
		 * for(int i=0; i< res.size(); i++) { System.out.println(res.get(i)); }
		 */
	    
	    //tree.printDFS(mine.getRoot());
	    
	    
		in.close();
	}
}