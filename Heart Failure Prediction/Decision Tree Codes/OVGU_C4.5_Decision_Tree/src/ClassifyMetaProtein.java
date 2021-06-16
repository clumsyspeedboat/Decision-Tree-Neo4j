import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import CV.CrossValidation;
import CV.CrossValidationWithPruning;
import MineData.C45MineData;
import ProcessOutput.PrintTree;
import ProcessInput.ProcessInputData;

public class ClassifyMetaProtein {
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
		CrossValidation cv = new CrossValidation("data/Metaprotein_Pre_processed.csv");
		CrossValidationWithPruning cvP = new CrossValidationWithPruning("data/Metaprotein_Pre_processed.csv");

		ArrayList<Double> final_score = cv.validate(10);
		ArrayList<Double> final_score_P = cvP.validate(10);

		double r = 0;
		double rP = 0;

		for (int i = 0; i < final_score.size(); i++) {
			r += final_score.get(i);
			rP += final_score_P.get(i);
		}

		PrintTree tree = new PrintTree();

		System.out.println("**********************");
		System.out.println("Cross Validation Accuracy before Pruning: " + (r / 10) * 100 + "%");
		System.out.println("Cross Validation Accuracy after Pruning: " + (rP / 10)* 100 + "%");
		
		System.out.println(tree.printDFS(cv.getRoot()));
		 
	    
		in.close();
	}
}
