/**
 * Main class to build the decision tree and test on the test dataset
 */

import java.io.IOException;
import java.util.Scanner;
import MineData.C45MineData;
import ProcessOutput.PrintTree;

public class ClassifyMain {
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		long startTime = System.nanoTime();
		
		C45MineData mine = new C45MineData("/Users/tahab/Desktop/Graph-Database-Learning-Algorithms-Neo4j-/Heart Failure Prediction/Decision Tree Codes/OVGU_C4.5_Decision_Tree/data/train.csv", "/Users/tahab/Desktop/Graph-Database-Learning-Algorithms-Neo4j-/Heart Failure Prediction/Decision Tree Codes/OVGU_C4.5_Decision_Tree/data/test.csv");
		
		mine.calculateAccuracy();
		
		long endTime = System.nanoTime();
	    long elapsedTime = endTime - startTime;
	    double msTime = (double) elapsedTime / 1000000.0;
	    System.out.println("Time taken: " + msTime + " ms\n");
		
	    PrintTree tree = new PrintTree();
	    System.out.println(tree.printDFS(mine.getRoot()));
	    
		in.close();
	}
}