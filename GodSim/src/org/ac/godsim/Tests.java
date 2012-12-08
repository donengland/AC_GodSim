package org.ac.godsim;

import java.util.List;

import org.ac.godsim.persistentdata.GodSimDB;

/* This class is simply here to throw some test values at the program and see how they conform */

public class Tests {
	
	public static void runTests() {
	
		
		/* TEST #1 - ADD 10 CIVS and output them for verification */
		/* This will update the GAMES_TABLE for game #1
		 * and insert 10 records into the CIVS_TABLE
		 * Note that the unit values in the CIVS_TABLE will be null after this test
		 */
		System.err.println("verifying ability to add new civs");
		for (int i = 1; i <= 10; i++) {GodSimDB.addCiv("1");}
		System.err.println("verifying ability to retrieve new civs");
		//query the GAMES_TABLE for all civs related to game #1 
		List<String> civTest = GodSimDB.getCivs("1"); 
		for (String c : civTest) {System.err.printf("Game 1, civ: %s\n", c);}
		
		//**********************************************************************************************************************
		
		/* TEST #2 - ADD 10 UNITS and output them for verification */
		/* This will update the CIVS_TABLE for unit #1
		 * and insert 10 records into the UNITS_TABLE
		 * The health, x, and y values for each unit are set to arbitrary values
		 */
		
		System.out.println("verifying ability to add new units");
		for (int i = 1; i <= 10; i++) {GodSimDB.addUnit(i, i*10, i*20, i*30);}
		System.err.println("verifying ability to retrieve units");
		for (int i = 1; i <= 10; i++) {
			System.err.printf("Health: %d, X: %d, Y: %d\n", GodSimDB.getHealth(i), GodSimDB.getX(i), GodSimDB.getY(i));
		}
		
		//**********************************************************************************************************************		
		
		/* TEST #3 - UPDATE the CIVS and output them for verification */
		System.err.println("Verifying ability to update existing civs");
		for (int i = 1; i <= 10; i++) {
			GodSimDB.updateCiv(i, i*10);
		}
		civTest = GodSimDB.getCivs("1"); 
		for (String c : civTest) {System.err.printf("Game 1, Updated Value for Civ: %s\n", c);}
		
		
		/* TEST #4 - UPDATE the HEALTH, X, and Y values and output them for verification */
		for (int i = 1; i <= 10; i++) {
			GodSimDB.updateHealth(i, i*10, i*10+1);
			GodSimDB.updateX(i, i*20, i*20+1);
			GodSimDB.updateY(i, i*30, i*30+1);
		}
		for (int i = 1; i <= 10; i++) {
			System.err.printf("Health: %d, X: %d, Y: %d\n", GodSimDB.getHealth(i), GodSimDB.getX(i), GodSimDB.getY(i));
		}
		
		
		
	}

}
