package geon.game.topview.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * <pre>
 * geon.game.topview.components
 * 	|_CollisionComponent
 * 
 * 개요 : 
 * 작성일 : Nov 25, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class CollisionComponent implements Component {
	
	// 발이 눈에서 얼마나 멀리 떨어져 있는지
   public final Vector3[] FEET_OFFSETS = {
           new Vector3(-.4f, 0, -.4f),
           new Vector3(-.4f, 0, .4f),
           new Vector3(.4f, 0, .4f),
           new Vector3(.4f, 0, -.4f),
   };
   
   public final Vector3 pos = new Vector3(0, 100, 0);
   public final Vector3 acc = new Vector3(0, -30, 0);
   public final Vector3 vel = new Vector3(0, 0, 0);
	public final Vector3 go = new Vector3();
	
	public boolean isOnGround = false, isColliding = false;
	
	public CollisionComponent (float x, float y, float z) {
	}
}
