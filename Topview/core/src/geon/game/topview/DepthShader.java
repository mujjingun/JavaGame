package geon.game.topview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader.Uniform;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

/**
 * <pre>
 * geon.game.topview
 * 	|_DepthShader
 * 
 * 개요 : 
 * 작성일 : 2015. 9. 28.
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class DepthShader extends DefaultShader {

	private int u_linearDepth, u_viewTrans, u_projTrans;
	/**
	 * @param renderable
	 */
	public DepthShader (Renderable renderable) {
		super(renderable, new Config(Gdx.files.internal("depthShader.vert").readString(),
			Gdx.files.internal("depthShader.frag").readString()));
	}

	@Override
	public void init () {
		u_linearDepth = register(new Uniform("u_linearDepth"));
		u_viewTrans = register(new Uniform("u_viewTrans"));
		u_projTrans = register(new Uniform("u_projTrans"));
		super.init();
	}

	@Override
	public void begin (Camera camera, RenderContext context) {
		super.begin(camera, context);
		set(u_linearDepth, camera.far - camera.near);
		set(u_viewTrans, camera.view);
		set(u_projTrans, camera.projection);
	}

}
