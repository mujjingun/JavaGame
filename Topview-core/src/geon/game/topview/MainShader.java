
package geon.game.topview;

import geon.game.topview.system.CameraSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader.Uniform;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

public class MainShader extends DefaultShader {
	
	private int u_linearDepth, u_depthTexture, u_normalTexture, u_viewTrans, u_tanHalfFov, u_aspectRatio, u_viewPortHeight, u_projTrans;
	private Texture depthTexture, normalTexture;

	public void setTextures (Texture depthTexture, Texture normalTexture) {
		this.depthTexture = depthTexture;
		this.normalTexture = normalTexture;
	}

	/**
	 * @param renderable
	 */
	public MainShader (Renderable renderable) {
		super(renderable, new Config(Gdx.files.internal("gameShader.vert").readString(), Gdx.files.internal("gameShader.frag").readString()));
	}

	@Override
	public void init () {
		u_linearDepth = register(new Uniform("u_linearDepth"));
		u_depthTexture = register(new Uniform("u_depthTexture"));
		u_normalTexture = register(new Uniform("u_normalTexture"));
		u_viewTrans = register(new Uniform("u_viewTrans"));
		u_projTrans = register(new Uniform("u_projTrans"));
		u_tanHalfFov = register(new Uniform("u_tanHalfFov"));
		u_aspectRatio = register(new Uniform("u_aspectRatio"));
		u_viewPortHeight = register(new Uniform("u_viewPortHeight"));
		
		super.init();
	}
	
	@Override
	public void begin (Camera camera, RenderContext context) {
		super.begin(camera, context);
		// must set uniform after super.begin()
		set(u_linearDepth, camera.far - camera.near);
		set(u_viewTrans, camera.view);
		set(u_projTrans, camera.projection);
		set(u_tanHalfFov, (float) Math.tan(CameraSystem.FOV / 2));
		set(u_aspectRatio, camera.viewportWidth / camera.viewportHeight);
		set(u_viewPortHeight, camera.viewportHeight);
		set(u_depthTexture, depthTexture);
		set(u_normalTexture, normalTexture);
	}

	@Override
	public void render (Renderable renderable) {
		super.render(renderable);
	}
	

}
