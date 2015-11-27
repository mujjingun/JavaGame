package geon.game.topview.system;

import geon.game.topview.components.CollisionComponent;
import geon.game.topview.components.RenderableComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

/**
 * <pre>
 * geon.game.topview.system
 * 	|_EnemySystem
 * 
 * 개요 : 
 * 작성일 : Nov 25, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class EnemySystem extends IteratingSystem {

	public EnemySystem () {
		super(Family.all(RenderableComponent.class, CollisionComponent.class).get());
	}

	/* (non-Javadoc)
	 * @see com.badlogic.ashley.systems.IteratingSystem#processEntity(com.badlogic.ashley.core.Entity, float)
	 */
	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		RenderableComponent renderableComponent = entity.getComponent(RenderableComponent.class);
		CollisionComponent col = entity.getComponent(CollisionComponent.class);
		
		renderableComponent.model.transform.setTranslation(col.pos);
	}

}
