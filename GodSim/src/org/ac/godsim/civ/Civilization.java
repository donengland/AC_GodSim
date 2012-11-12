package org.ac.godsim.civ;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ac.godsim.civ.units.Unit;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-November-2012
 */
public class Civilization {

	private List<Unit> myUnits;
	
	public Civilization(){
		myUnits = new LinkedList<Unit>();
		//myUnits.add(new Unit(new Gatherer()));
	}
	
	public void updateUnits(){
		Iterator<Unit> iter = myUnits.iterator();
        while (iter.hasNext()) {
            //Unit currentUnit = (Unit) iter.next();
            //currentUnit.update();
        }
	}
}
