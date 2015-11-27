
package geon.game.topview.system;

import geon.game.topview.DDA;
import geon.game.topview.components.CollisionComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

/** <pre>
 * geon.game.topview.system
 * 	|_CollisionSystem
 * 
 * 개요 : 
 * 작성일 : Nov 25, 2015
 * </pre>
 *
 * @author 박건
 * @version 1.0 */
public class CollisionSystem extends IteratingSystem {

	public CollisionSystem () {
		super(Family.all(CollisionComponent.class).get());
	}

	private static final Vector3 tmp = new Vector3(), deltaS = new Vector3();
	private static final Vector3 feetPos = new Vector3();
	private final Ray ray = new Ray();

	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		MapSystem mapGen = getEngine().getSystem(MapSystem.class);
		CollisionComponent com = entity.getComponent(CollisionComponent.class);

		com.vel.add(tmp.set(com.acc).scl(deltaTime)); // dv = at

		deltaS.set(tmp.set(com.vel).scl(deltaTime)); // ds = vt

		com.isOnGround = false;
		com.isColliding = false;

		for (Vector3 feetOffset : com.FEET_OFFSETS) {
			feetPos.set(com.pos).add(feetOffset);
			for (int i = 0; i < 3; i++) {
				ray.set(feetPos, deltaS);
				DDA.Collision collision;
				float len = deltaS.len();
				if ((collision = mapGen.doesCollide(ray, len)) != null) {
					float lenDot = deltaS.dot(collision.getNormal());
					float distDot = tmp.set(deltaS).nor().scl(collision.getDist()).dot(collision.getNormal());
					if (-distDot <= -lenDot + 0.101f) {
						com.isColliding = true;
						if (collision.getNormal().y > 0) {
							com.isOnGround = true;
						}
						collide(com.vel, collision.getNormal());
						collide(com.go, collision.getNormal());
						deltaS.sub(tmp.set(collision.getNormal()).scl(lenDot - distDot - 0.1f));
					}
				}
			}
		}
		
		com.pos.add(deltaS);

	}

	private static void collide (Vector3 v, Vector3 norm) {
		norm = tmp.set(norm);
		v.sub(norm.scl(norm.dot(v)));
	}

}
