package org.ac.godsim.civ.units;

import org.ac.godsim.utils.constants.GodSimConstants;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-December-2012
 */
public class Civ implements IUnitType, GodSimConstants {

	public String getType() {
		return "civilization";
	}
	
	public void update(Unit myUnit, float deltaTime){
		// civilizations don't move
	}
}
