package main;

import java.io.FileOutputStream;

/**
 * Main class to build the decision tree and test on the test dataset
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import evaluate.EvaluateTree;
import output.PrintTree;

public class ClassifyMainIG {
	
	public static void main(String[] args) throws IOException {		
		Scanner in = new Scanner(System.in);
		
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
		ArrayList<String> t11= new ArrayList<String>();
		t11.add("anaemia:1, serum_creatinine:0.19, sex:1, ejection_fraction:45, creatinine_phosphokinase:562, platelets:1232000.0, DEATH_EVENT:0, high_blood_pressure:0, smoking:1, time:71, serum_sodium:139, diabetes:1, age:'58.0'");
		ArrayList<String> t12= new ArrayList<String>();
		t12.add("anaemia:1, serum_creatinine:111.0, sex:0, ejection_fraction:235, creatinine_phosphokinase:125, platelets:237000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:0, time:15, serum_sodium:140, diabetes:0, age:'70.0'");
		ArrayList<String> t13= new ArrayList<String>();
		t13.add("anaemia:0, serum_creatinine:2.39, sex:1, ejection_fraction:220, creatinine_phosphokinase:168, platelets:119000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:1, time:64, serum_sodium:127, diabetes:0, age:'60.0'");
		ArrayList<String> t14= new ArrayList<String>();
		t14.add("anaemia:1, serum_creatinine:21.1, sex:1, ejection_fraction:338, creatinine_phosphokinase:1628, platelets:276000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:0, time:11, serum_sodium:137, diabetes:0, age:'50.0'");
		ArrayList<String> t15= new ArrayList<String>();
		t15.add("anaemia:0, serum_creatinine:11.3, sex:1, ejection_fraction:415, creatinine_phosphokinase:1222, platelets:284000.0, DEATH_EVENT:1, high_blood_pressure:1, smoking:1, time:26, serum_sodium:136, diabetes:1, age:'70.0'");
		ArrayList<String> t16= new ArrayList<String>();
		t16.add("anaemia:1, serum_creatinine:12.1, sex:0, ejection_fraction:610, creatinine_phosphokinase:5838, platelets:194000.0, DEATH_EVENT:1, high_blood_pressure:0, smoking:0, time:33, serum_sodium:142, diabetes:1, age:'60.0'");
		
		
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
		testList.add(t11);
		testList.add(t12);
		testList.add(t13);
		testList.add(t14);
		testList.add(t15);
		testList.add(t16);
		
		try {
		    FileOutputStream fos = new FileOutputStream("D:/Personal_Project/testList_output.txt");
		    ObjectOutputStream oos = new ObjectOutputStream(fos);   
		    oos.writeObject(testList); // write MenuArray to ObjectOutputStream
		    oos.close(); 
		} catch(Exception ex) {
		    ex.printStackTrace();
		}
		
	
		//String pathos = "data/Flu_Classification_Training_Dataset.csv,data/Flu_Classification_Testing_Dataset.csv";


		//String[] paths = pathos.split(",");
		
		
		EvaluateTree mine = new EvaluateTree(trainList, testList, "DEATH_EVENT");
	
		mine.calculateAccuracy();

	    
	    PrintTree tree = new PrintTree();
	    
		//System.out.println(tree.printDFS(mine.getRoot()));
		 
		tree.createNodesForGraph(mine.getRoot());
		
		System.out.println(tree.nodesBucket);
		
		
		in.close();
	}
	
}