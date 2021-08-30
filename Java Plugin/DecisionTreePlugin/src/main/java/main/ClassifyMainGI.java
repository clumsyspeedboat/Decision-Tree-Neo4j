package main;

import java.io.IOException;
import java.util.Scanner;

import gini.EvaluateTreeGI;
import output.PrintTree;

public class ClassifyMainGI {
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
		String pathos = "data/train.csv,data/test.csv";
		
		String[] paths = pathos.split(",");
		
		EvaluateTreeGI mine = new EvaluateTreeGI(paths[0],paths[1],"DEATH_EVENT");
		
	    mine.calculateAccuracy();

	    PrintTree tree = new PrintTree();
	    
		//System.out.println(tree.printDFS(mine.getRoot()));
		 
		tree.createNodesForGraph(mine.getRoot());


		in.close();
	
	}

}
