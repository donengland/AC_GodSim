package org.ac.godsim.civ;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ac.godsim.civ.units.Civ;
import org.ac.godsim.civ.units.Gatherer;
import org.ac.godsim.civ.units.Scholar;
import org.ac.godsim.civ.units.Unit;
import org.ac.godsim.civ.units.Warrior;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-November-2012
 */
public class Civilization extends Entity {

	private final List<Unit> myUnits = new LinkedList<Unit>();
	
	public Civilization(){
	}
	
	public void updateUnits(){
		Iterator<Unit> iter = myUnits.iterator();
        while (iter.hasNext()) {
            //Unit currentUnit = (Unit) iter.next();
            //currentUnit.update();
        }
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
		this.myUnits.add(thisUnit);
	}
}
