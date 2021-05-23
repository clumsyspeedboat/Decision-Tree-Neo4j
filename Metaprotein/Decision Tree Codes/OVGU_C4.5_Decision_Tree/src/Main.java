import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import CV.CrossValidation;
import CV.CrossValidationWithPruning;
import ProcessOutput.PrintTree;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        System.out.println("===============================================================");
        
        long startTime = System.nanoTime();
        CrossValidation cv = new CrossValidation("/Users/tahab/Desktop/Graph-Database-Learning-Algorithms-Neo4j-/Metaprotein/Decision Tree Codes/OVGU_C4.5_Decision_Tree/data/Metaprotein_10.csv");
        CrossValidationWithPruning cvP = new CrossValidationWithPruning("/Users/tahab/Desktop/Graph-Database-Learning-Algorithms-Neo4j-/Metaprotein/Decision Tree Codes/OVGU_C4.5_Decision_Tree/data/Metaprotein_10.csv");

        ArrayList<Double> final_score = cv.validate(10);
        ArrayList<Double> final_score_P = cvP.validate(10);

        double r = 0;
        double rP = 0;
        
        for(int i = 0; i < final_score.size(); i++) {
            r += final_score.get(i);
            rP += final_score_P.get(i);
        }

        PrintTree print = new PrintTree();

        System.out.println("**********************");
        System.out.println("Cross Validation Accuracy before Pruning: " + (r / 10) * 100 + "%");
        System.out.println("Cross Validation Accuracy after Pruning: " + (rP / 10)* 100 + "%");
        
        // Calculate and print total time taken.
        
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double msTime = (double) elapsedTime / 1000000.0;
        System.out.println("Time taken: " + msTime + " ms\n");
        	
		System.out.println("**********************");
		System.out.println("Do you want to see the tree before pruning? (y/n)");
		String printTreeBefore = in.nextLine();
		if (printTreeBefore.equals("y")) {
			System.out.println("Tree before Pruning: \n" + print.printDFS(cv.getRoot()));
		}
		System.out.println("**********************");
		System.out.println("Do you want to see the tree after pruning? (y/n)");
		String printTreeAfter = in.nextLine();
		if (printTreeAfter.equals("y")) {
			System.out.println("Tree after Pruning: \n" + print.printDFS(cvP.getRoot()));
		}
		
        in.close();
        
        
    }
}