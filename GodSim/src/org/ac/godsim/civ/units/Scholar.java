package org.ac.godsim.civ.units;

import org.ac.godsim.utils.constants.GodSimConstants;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-December-2012
 */
public class Scholar implements IUnitType, GodSimConstants {

	public String getType() {
		return "scholar";
	}
	
	public void update(Unit myUnit, float deltaTime){
		// Scholars don't update their values
	}
}
