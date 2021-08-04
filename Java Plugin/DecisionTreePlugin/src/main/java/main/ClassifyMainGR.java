package main;

import java.io.IOException;
import java.util.Scanner;

import gainratio.GainRatioMineData;
import output.PrintTree;

public class ClassifyMainGR {
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
		String pathos = "data/Flu_Classification_Training_Dataset.csv,data/Flu_Classification_Testing_Dataset.csv";
		
		String[] paths = pathos.split(",");
		
		
		GainRatioMineData mine = new GainRatioMineData(paths[0], paths[1]);
		
	    mine.calculateAccuracy();

	    
	    PrintTree tree = new PrintTree();
	    
		//System.out.println(tree.printDFS(mine.getRoot()));
		 
		tree.createNodesForGraph(mine.getRoot());
		in.close();
	}

}
