package org.ac.godsim.civ.units;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 12-December-2012
 */
public abstract interface IUnitType {
	
	abstract public String getType();
	abstract public void update(Unit unit, float deltaTime);
}
