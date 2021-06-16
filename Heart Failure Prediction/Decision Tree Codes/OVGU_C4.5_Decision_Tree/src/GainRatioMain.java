/**
 * Main class to build the decision tree and test on the test dataset
 */

import java.io.IOException;
import java.util.Scanner;
import MineData.C45MineData;
import ProcessOutput.PrintTree;
import GainRatio.GainRatioMineData;

public class GainRatioMain {
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		long startTime = System.nanoTime();
		
		GainRatioMineData mine= new GainRatioMineData("/Users/tahab/Desktop/Graph-Database-Learning-Algorithms-Neo4j-/Heart Failure Prediction/Decision Tree Codes/OVGU_C4.5_Decision_Tree/data/maxAccTrain.csv", "/Users/tahab/Desktop/Graph-Database-Learning-Algorithms-Neo4j-/Heart Failure Prediction/Decision Tree Codes/OVGU_C4.5_Decision_Tree/data/maxAccTest.csv");
		
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