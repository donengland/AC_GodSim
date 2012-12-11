package org.ac.godsim.civ.units;

//import org.andengine.entity.sprite.AnimatedSprite;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-November-2012
 */
public abstract interface IUnitType {
	
	abstract public String getType();
	abstract public void update(Unit unit, float deltaTime);
}
