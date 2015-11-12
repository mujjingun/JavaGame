package geon.game.topview.system;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * <pre>
 * geon.game.topview.system
 * 	|_CameraSystem
 * 
 * 개요 : 
 * 작성일 : 2015. 9. 25.
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class CameraSystem extends EntitySystem {

	private PerspectiveCamera cam;
	public static final float FOV = 70;
	
	public PerspectiveCamera getCam () {
		return cam;
	}

	@Override
	public void addedToEngine (Engine engine) {
		
		cam = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 256f;
		cam.update();
		
	}

	@Override
	public void removedFromEngine (Engine engine) {
	}

	private final Vector3 camPos = new Vector3(1f, 10f, 1f);
	private final Vector2 camAngle = new Vector2(0, -1f), destAngle = camAngle.cpy();
	private float mouseSpeed = 1;
	private final Vector3 direction = new Vector3(), up = new Vector3();
	private final Vector2 tmp = new Vector2();
	private final float cameraSmooth = 20f;
	private final Vector2 front = new Vector2(), right = new Vector2();

	private void moveCamera (float width, float height, float deltaTime) {

		InputSystem input = getEngine().getSystem(InputSystem.class);

		destAngle.x += mouseSpeed * deltaTime * -input.getMouseMoved().x;
		destAngle.y += mouseSpeed * deltaTime * input.getMouseMoved().y;
		destAngle.y = Math.max(-PI / 2 + 0.01f, Math.min(PI / 2 - 0.01f, destAngle.y));

		// smooth movement
		camAngle.add(tmp.set(destAngle).scl(cameraSmooth * deltaTime)).scl(1 / (1 + cameraSmooth * deltaTime));

		direction.set(cos(camAngle.y) * sin(camAngle.x), sin(camAngle.y), cos(camAngle.y) * cos(camAngle.x)).nor();
		right.set(sin(camAngle.x - PI / 2), cos(camAngle.x - PI / 2));
		up.set(right.x, 0, right.y).crs(direction);
		cam.position.set(camPos);
		cam.direction.set(direction);
		cam.up.set(up);
		
		front.set(direction.x, direction.z);
		front.nor();

		cam.update();

	}

    public Vector2 getFront() {
        return front;
    }

    public Vector2 getRight() {
        return right;
    }

    public Vector3 getCamPos () {
		return camPos;
	}

	public void setCamPos (Vector3 camPos) {
		this.camPos.set(camPos);
	}

	public Vector3 getDirection () {
		return direction;
	}

	public void setDirection (Vector3 direction) {
		this.direction.set(direction);
	}

	@Override
	public void update (float deltaTime) {
		final int width = Gdx.graphics.getWidth();
		final int height = Gdx.graphics.getHeight();
		
		moveCamera(width, height, deltaTime);
		
	}

}
