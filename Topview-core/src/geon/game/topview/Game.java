
package geon.game.topview;

import geon.game.topview.system.*;

import java.nio.FloatBuffer;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;

public class Game implements ApplicationListener {
	PooledEngine engine;

	Vector3 spPos = new Vector3(0, 0, 0);

	void init () {
		Gdx.app.log("init", "init");

		engine = new PooledEngine();
		engine.addSystem(new MapSystem());
		engine.addSystem(new PlayerSystem());
		engine.addSystem(new CameraSystem());
		engine.addSystem(new RenderSystem());
		engine.addSystem(new ControlSystem());
		engine.addSystem(new InputSystem());
	}

	
	@Override
	public void create () {
		init();

		String extensions = Gdx.gl.glGetString(GL20.GL_EXTENSIONS);
		Gdx.app.log("test", extensions);
		boolean anisotropicSupported = extensions.contains("GL_EXT_texture_filter_anisotropic");
		BlockType.anisotropicSupported = anisotropicSupported;
		if (anisotropicSupported) {
			FloatBuffer buffer = BufferUtils.newFloatBuffer(64);
			Gdx.gl20.glGetFloatv(GL20.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, buffer);

			float maxAnisotropy = buffer.get(0);
			BlockType.maxAnisotropy = maxAnisotropy;
		}

	}


	private float timeWarp = 1;
	
	@Override
	public void render () {
		final float deltaTime = Gdx.graphics.getDeltaTime() * timeWarp;
		engine.update(deltaTime);
	}

	@Override
	public void dispose () {
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}
}
