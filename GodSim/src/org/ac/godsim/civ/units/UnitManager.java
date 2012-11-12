package org.ac.godsim.civ.units;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**This class has been merged into {@link Civilization}
 * 
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-November-2012
 */
public class UnitManager {
	
	private List<Unit> myUnits;

	private VertexBufferObjectManager mvbo;
	private TiledTextureRegion mGathererTextureRegion;
	private TiledTextureRegion mScholarTextureRegion;
	private TiledTextureRegion mWarriorTextureRegion;
	private TiledTextureRegion mCivilizationTextureRegion;
	
	private Scene mScene;
	
	public UnitManager(VertexBufferObjectManager vbo){
		mvbo = vbo;
		myUnits = new LinkedList<Unit>();
	}
	public void setGathererTexRegion(TiledTextureRegion gtex){
		mGathererTextureRegion = gtex;
	}
	public void setScholarTexRegion(TiledTextureRegion gtex){
		mScholarTextureRegion = gtex;
	}
	public void setWarriorTexRegion(TiledTextureRegion gtex){
		mWarriorTextureRegion = gtex;
	}
	public void setCivilizationTexRegion(TiledTextureRegion gtex){
		mCivilizationTextureRegion = gtex;
	}	
	public void setScene(Scene scene){
		mScene = scene;
	}
	
	public void updateUnits(){
		Iterator<Unit> iter = myUnits.iterator();
        while (iter.hasNext()) {
            //Unit currentUnit = (Unit) iter.next();
            //currentUnit.update();
        }
	}
	
	public void addGatherer(float pX, float pY){
		Unit thisUnit = new Unit(new Gatherer(), pX, pY, mGathererTextureRegion, mvbo);
		thisUnit.animate(200, true);
		this.mScene.registerTouchArea(thisUnit);
		this.mScene.attachChild(thisUnit);		
		myUnits.add(thisUnit);
	}
	public void addScholar(float pX, float pY){
		Unit thisUnit = new Unit(new Scholar(), pX, pY, mScholarTextureRegion, mvbo);
		thisUnit.animate(200, true);
		this.mScene.registerTouchArea(thisUnit);
		this.mScene.attachChild(thisUnit);		
		myUnits.add(thisUnit);
	}
	public void addWarrior(float pX, float pY){
		Unit thisUnit = new Unit(new Warrior(), pX, pY, mWarriorTextureRegion, mvbo);
		thisUnit.animate(200, true);
		this.mScene.registerTouchArea(thisUnit);
		this.mScene.attachChild(thisUnit);		
		myUnits.add(thisUnit);
	}
	public void addCiv(float pX, float pY){
		Unit thisUnit = new Unit(new Civ(), pX, pY, mCivilizationTextureRegion, mvbo);
		thisUnit.animate(1000, true);
		this.mScene.registerTouchArea(thisUnit);
		this.mScene.attachChild(thisUnit);		
		myUnits.add(thisUnit);
	}
}
