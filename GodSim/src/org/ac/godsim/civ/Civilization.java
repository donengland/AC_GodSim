package org.ac.godsim.civ;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ac.godsim.civ.units.Unit;
import org.ac.godsim.utils.constants.GodSimConstants;
import org.andengine.entity.Entity;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-December-2012
 */
public class Civilization extends Entity implements GodSimConstants{

	private final List<Unit> myUnits = new LinkedList<Unit>();
	
	private UNIT_COLOR myColor;
	
	public Civilization(UNIT_COLOR color){
		myColor = color;
	}
	
	public void updateUnits(float deltaTime){
		Iterator<Unit> iter = myUnits.iterator();
        while (iter.hasNext()) {
            Unit currentUnit = (Unit) iter.next();
            currentUnit.performUpdate(deltaTime);
        }
	}
	
	public UNIT_COLOR getTeamColor(){
		return myColor;
	}
	
	public void addGatherer(Unit thisUnit){
		this.myUnits.add(thisUnit);
	}
	public void addScholar(Unit thisUnit){
		this.myUnits.add(thisUnit);
	}
	public void addWarrior(Unit thisUnit){
		this.myUnits.add(thisUnit);
	}
	public void addCiv(Unit thisUnit){
		this.setX(thisUnit.getX());
		this.setY(thisUnit.getY());
		setBase();
		this.myUnits.add(thisUnit);
	}
	
	public void setBase(){
		Iterator<Unit> iter = myUnits.iterator();
        while (iter.hasNext()) {
            Unit currentUnit = (Unit) iter.next();
            currentUnit.setBaseX(this.getX());
            currentUnit.setBaseY(this.getY());
        }		
	}
}
