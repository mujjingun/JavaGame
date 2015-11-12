
package geon.game.topview;

import geon.game.topview.system.*;

import java.nio.FloatBuffer;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;

public class Game implements ApplicationListener {
	PooledEngine engine;

	Vector3 spPos = new Vector3(0, 0, 0);
	public static Material material;
	public static Texture texture;

	private static Texture makeTexture(String name) {
		Texture t = new Texture(Gdx.files.internal(name));
		return t;
	}
	
	void init () {
		Gdx.app.log("init", "init");
		
		texture = makeTexture("id1.png");
		material = new Material(
			new TextureAttribute(TextureAttribute.Diffuse, texture)
		);

		engine = new PooledEngine();
		engine.addSystem(new MapSystem());
		engine.addSystem(new PlayerSystem());
		engine.addSystem(new CameraSystem());
		engine.addSystem(new RenderSystem());
		engine.addSystem(new ControlSystem());
		engine.addSystem(new InputSystem());
		
	}

	
	public Material getMaterial () {
		return material;
	}

	@Override
	public void create () {
		init();
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
