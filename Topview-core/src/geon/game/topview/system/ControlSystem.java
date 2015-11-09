package geon.game.topview.system;

import com.badlogic.gdx.math.Vector3;
import geon.game.topview.BlockType;
import geon.game.topview.DDA;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.collision.Ray;

/**
 * <pre>
 * geon.game.topview.system
 * 	|_ControlSystem
 * 
 * 개요 : 
 * 작성일 : 2015. 9. 25.
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class ControlSystem extends EntitySystem {

	@Override
	public void addedToEngine (Engine engine) {
	}

	@Override
	public void removedFromEngine (Engine engine) {
	}

	@Override
	public void update (float deltaTime) {
		PerspectiveCamera cam = getEngine().getSystem(CameraSystem.class).getCam();
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		MapSystem mapGen = getEngine().getSystem(MapSystem.class);
		
		Ray mouseRay = cam.getPickRay(width / 2, height / 2);
		//Gdx.app.log("test", ""+mouseRay);

		DDA PointDDA = new DDA(mouseRay, 100);
		boolean hit = false;
		Vector3 curPos = new Vector3();
		for (int i = 0; i < 30; i++) {
			PointDDA.step(curPos);
			if (mapGen.getBlock(curPos).getBlockType() != BlockType.Air) {
				hit = true;
				mapGen.setSelectPos(curPos);
				break;
			}
		}
		if (!hit) mapGen.getSelectPos().set(-100, -100, -100);
	}

}
