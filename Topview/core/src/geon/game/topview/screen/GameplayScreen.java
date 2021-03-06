package geon.game.topview.screen;

import geon.game.topview.components.HealthComponent;
import geon.game.topview.components.PlayerComponent;
import geon.game.topview.system.CameraSystem;
import geon.game.topview.system.ControlSystem;
import geon.game.topview.system.InputSystem;
import geon.game.topview.system.MapSystem;
import geon.game.topview.system.PlayerSystem;
import geon.game.topview.system.RenderSystem;
import geon.game.topview.system.UISystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

/**
 * <pre>
 * geon.game.topview.screen
 * 	|_GameplayScreen
 * 
 * 개요 : 
 * 작성일 : Nov 14, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class GameplayScreen implements Screen {

	private PooledEngine engine;

	private float timeWarp = 1;
	
	public GameplayScreen(final Game game) {
		Gdx.app.log("init", "init");

		engine = new PooledEngine();
		
		engine.addSystem(new MapSystem());
		engine.addSystem(new PlayerSystem());
		engine.addSystem(new CameraSystem());
		engine.addSystem(new RenderSystem());
		engine.addSystem(new UISystem());
		engine.addSystem(new ControlSystem());
		engine.addSystem(new InputSystem());
		
		Entity player = new Entity();
		player.add(new HealthComponent());
		player.add(new PlayerComponent());
		
		engine.addEntity(player);
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show () {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render (float delta) {
		final float deltaTime = Gdx.graphics.getDeltaTime() * timeWarp;
		engine.update(deltaTime);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize (int width, int height) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause () {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume () {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide () {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose () {
		// TODO Auto-generated method stub
		
	}

}
