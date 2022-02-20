package main;


/**
 * A class to declare all the constants of the plugin
 * 
 * @author Nasim Ahmed
 *
 */
public final class Constants {
	//Path of the csv training and test set

	public static final String LOCAL_DATASET = "data/heart_train.csv,data/heart_test.csv";
	//Target attribute string
	public static final String TARGET_ATTRIBUTE = "DEATH_EVENT";
	
	public static final String IS_PRUNED= "True";

	public static final int MAX_DEPTH= 3;
}
