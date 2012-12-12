package org.ac.godsim.civ.units;

import org.ac.godsim.utils.constants.GodSimConstants;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 12-December-2012
 */
public class Warrior implements IUnitType, GodSimConstants {

	public String getType() {
		return "warrior";
	}
	
	public void update(Unit myUnit, float deltaTime){
		float tempX = myUnit.getX();
		float tempY = myUnit.getY();
		float tarX  = myUnit.getTargetX();
		float tarY  = myUnit.getTargetY();
		float move  = warriorSpeed * deltaTime;
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
		if(tarY > tempY){
			if(tempY+move > tarY)
				myUnit.setY(tarY);
			else
				myUnit.setY(tempY+move);
		} else{
			if(tempY-move < tarY)
				myUnit.setY(tarY);
			else
				myUnit.setY(tempY-move);
		}
	}
}
