package org.ac.godsim.civ.units;

import org.ac.godsim.utils.constants.GodSimConstants;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-December-2012
 */
public class Gatherer implements IUnitType, GodSimConstants {
	
	private int toBase = 1;
	
	public String getType(){
		return "gatherer";
	}
	
	public void update(Unit myUnit, float deltaTime){
		float tempX = myUnit.getX();
		float tempY = myUnit.getY();
		float tarX, tarY;
		
		if(toBase == 1){
			tarX = myUnit.getBaseX();
			tarY = myUnit.getBaseY();
		}else{
			tarX = myUnit.getTargetX();
			tarY = myUnit.getTargetY();
		}
		
		float move  = gathererSpeed * deltaTime;
		if(tarX == tempX && tarY == tempY)
			toBase = 1 - toBase;
		
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
