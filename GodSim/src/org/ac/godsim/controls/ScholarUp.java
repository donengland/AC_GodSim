package org.ac.godsim.controls;

import java.util.List;

import org.ac.godsim.persistentdata.GodSimDB;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */
public class ScholarUp implements ControlType {

	public String getType() {
		return "scholarUp";
	}
	
	public void updateLevel() {
		System.err.println("increasing scholar level");
		//TODO: Handle level change request
		
		//Sample code to interact with the database
		
		/* To retrieve a unit value - you'll need this if you are dealing with units individually */
		/* this will retrieve all of the units associated with a particular civilization */
		//List<String> units = GodSimDB.getUnits("1");
		
		/* To update a unit value - you'll need to capture the number of the unit you are targeting */
		/* pass this unit number along with the new health, x, y values to the update function */
//		GodSimDB.updateHealth(unit, oldValue, newValue);
//		GodSimDB.getX(unit);
//		GodSimDB.getY(unit);
		
		
		
	}
}