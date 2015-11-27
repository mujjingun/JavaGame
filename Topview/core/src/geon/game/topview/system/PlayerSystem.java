
package geon.game.topview.system;

import geon.game.topview.components.CollisionComponent;
import geon.game.topview.components.PlayerComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PlayerSystem extends IteratingSystem {

	public PlayerSystem () {
		super(Family.all(PlayerComponent.class).get());
	}

	private final float walkSpeed = 5, sprintSpeed = 10;
	private static final Vector3 tmp = new Vector3();

	private boolean isWalking = false;
	private float swayTime = 0;

	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		InputSystem input = getEngine().getSystem(InputSystem.class);
		CameraSystem cameraSystem = getEngine().getSystem(CameraSystem.class);
		Vector2 front = cameraSystem.getFront();
		Vector2 right = cameraSystem.getRight();
		MapSystem mapGen = getEngine().getSystem(MapSystem.class);
		CollisionComponent col = entity.getComponent(CollisionComponent.class);
		
		col.vel.sub(col.go);

		isWalking = false;
		if(col.isColliding)
			col.go.set(0, 0, 0);
		
		if (input.isPressed(Input.Keys.W) || input.isPressed(Input.Keys.SHIFT_LEFT)) {
			isWalking = true;
			col.go.add(front.x, 0, front.y);
		}
		if (input.isPressed(Input.Keys.S)) col.go.add(-front.x, 0, -front.y);
		if (input.isPressed(Input.Keys.A)) col.go.add(-right.x, 0, -right.y);
		if (input.isPressed(Input.Keys.D)) col.go.add(right.x, 0, right.y);

		// sprint
		if (input.isPressed(Input.Keys.SHIFT_LEFT))
			col.go.nor().scl(sprintSpeed);
		else
			col.go.nor().scl(walkSpeed);

		// only jump when on ground
		if (col.isOnGround && input.hasPushed(Input.Keys.SPACE)) col.vel.add(0, 8, 0);

		if (col.pos.y < -100) {
			col.vel.set(0, 0, 0);
			col.pos.set(1f, 10, 1f);
		}
		
		col.vel.add(col.go);
		
		if (isWalking) {
			if (input.isPressed(Input.Keys.SHIFT_LEFT))
				swayTime += deltaTime * 3;
			else
				swayTime += deltaTime;
			cameraSystem.sway(true, swayTime);
		} else
			cameraSystem.sway(false, 0);

		cameraSystem.setCamPos(tmp.set(col.pos).add(0, 1.837f, 0));

	}
}
