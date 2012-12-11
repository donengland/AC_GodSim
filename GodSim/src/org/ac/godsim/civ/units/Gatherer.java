package org.ac.godsim.civ.units;

import org.ac.godsim.utils.constants.GodSimConstants;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-December-2012
 */
public class Gatherer implements IUnitType, GodSimConstants {
	
	public String getType(){
		return "gatherer";
	}
	
	public void update(Unit myUnit, float deltaTime){
		//TODO -- implement update();
		float tempX = myUnit.getX();
		float tarX  = myUnit.getTargetX();
		float move  = gathererSpeed * deltaTime;
		if(tarX > tempX){
			if(tempX+move > tarX)
				myUnit.setX(tarX);
			else
				myUnit.setX(tempX+move);
		} else{
			if(tempX-move < tarX)
				myUnit.setX(tarX);
			else
				myUnit.setX(tempX-move);
		}
	}
}
