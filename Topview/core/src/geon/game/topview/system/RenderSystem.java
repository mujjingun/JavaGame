
package geon.game.topview.system;

import geon.game.topview.DepthShader;
import geon.game.topview.MainShader;
import geon.game.topview.Resources;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** <pre>
 * geon.game.topview
 * 	|_RenderSystem
 * 
 * 개요 : 
 * 작성일 : 2015. 9. 25.
 * </pre>
 *
 * @author 박건
 * @version 1.0 */
public class RenderSystem extends EntitySystem {

	//private FrameBuffer depthBuffer, normalBuffer;
	private Texture depthTexture, normalTexture;

	private MyShaderProvider provider;
	private ModelBatch modelBatch;
	private SpriteBatch spriteBatch;
	private Environment environment;

	// TODO: DEBUG
	private ModelInstance xyz, point;

	private final ModelBuilder modelBuilder = new ModelBuilder();

	@Override
	public void addedToEngine (Engine engine) {
//
//		depthBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
//		depthTexture = depthBuffer.getColorBufferTexture();
//
//		normalBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
//		normalTexture = normalBuffer.getColorBufferTexture();

		provider = new MyShaderProvider();
		modelBatch = new ModelBatch(provider);

		spriteBatch = Resources.batch;

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		environment.add(new PointLight().set(0.8f, 0.8f, 0.8f, -7f, -3f, -7f, 100.0f));
//
//		modelBuilder.begin();
//		MeshPartBuilder bPartBuilder = modelBuilder.part("rect", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal
//			| Usage.TextureCoordinates, new Material());
//		// NOTE ON TEXTURE REGION, MAY FILL OTHER REGIONS, USE GET region.getU() and so on
//		bPartBuilder.setUVRange(0, 0, 1, 1);
//		bPartBuilder.rect(-1, -1, 0, 1, -1, 0, 1, 1, 0, -1, 1, 0, 0, 0, 1);
//		screenModel = new ModelInstance(modelBuilder.end());
		// TODO: DEBUG
		xyz = new ModelInstance(modelBuilder.createXYZCoordinates(0.5f, new Material(), Usage.Position | Usage.ColorPacked));
		point = new ModelInstance(modelBuilder.createBox(0.3f, 0.3f, 0.3f, new Material(new ColorAttribute(ColorAttribute.Diffuse,
			Color.RED)), Usage.Position | Usage.Normal));

	}

	@Override
	public void removedFromEngine (Engine engine) {
		Gdx.app.log("test", "log");
	}

	@Override
	public void update (float deltaTime) {

		CameraSystem camSystem = getEngine().getSystem(CameraSystem.class);
		MapSystem mapGen = getEngine().getSystem(MapSystem.class);
		PlayerSystem playerSystem = getEngine().getSystem(PlayerSystem.class);

		// TODO: DEBUG
		xyz.transform.setTranslation(camSystem.getCamPos().cpy().add(camSystem.getDirection().cpy().scl(3)));
		xyz.calculateTransforms();
//
//		// render the depth buffer
//		depthBuffer.begin();
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//		Gdx.gl.glClearColor(0, 0, 0.3f, 0);
//
//		provider.useShader(MyShaderProvider.DEPTH_SHADER);
//
//		modelBatch.begin(cam);
//
//		modelBatch.render(point, environment);
//		modelBatch.render(xyz, environment);
//		mapGen.render(modelBatch, environment);
//
//		modelBatch.end();
//		depthBuffer.end();

//		normalBuffer.begin();
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//		Gdx.gl.glClearColor(0, 0, 0, 0);
//
//		provider.useShader(MyShaderProvider.NORMAL_SHADER);
//
//		modelBatch.begin(cam);
//
//		modelBatch.render(point, environment);
//		modelBatch.render(xyz, environment);
//		mapGen.render(modelBatch, environment);
//
//		modelBatch.end();
//		normalBuffer.end();

		// draw the real screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.7f, 0.7f, 1f, 0);

		provider.useShader(MyShaderProvider.DEFAULT_SHADER);

		modelBatch.begin(camSystem.getCam());
		
		modelBatch.render(point, environment);
		//modelBatch.render(xyz, environment);
		mapGen.render(modelBatch, environment);

		modelBatch.end();

		spriteBatch.setProjectionMatrix(camSystem.getOrthCam().combined);
		spriteBatch.begin();

		// spriteBatch.draw(depthTexture, 0, 0, 300, 300);

		// sp.draw(spriteBatch);
		// spriteBatch.draw(tex2, 0, 0, width, height);
		String debug = String.format(
			"x: %.3f\ny: %.3f\nz: %.3f\nFPS: %d\nvel: %s",
			camSystem.getCamPos().x,
			camSystem.getCamPos().y,
			camSystem.getCamPos().z,
			Gdx.graphics.getFramesPerSecond(),
			playerSystem.getVel()
			);
		//Resources.font.draw(spriteBatch, debug, 0, Gdx.graphics.getHeight());
		spriteBatch.end();
	}

	class MyShaderProvider implements ShaderProvider {

		private ArrayList<MainShader> mainShaders = new ArrayList<>();
		public static final int MAIN_SHADER = 0;
		private Shader depthShader; // 1
		public static final int DEPTH_SHADER = 1;
		private ArrayList<DefaultShader> defaultShaders = new ArrayList<>();
		public static final int DEFAULT_SHADER = 2;
		public static final int NORMAL_SHADER = 3;
		private Shader normalShader; // 3
		private int currentShader = DEFAULT_SHADER;
		
		public void useShader (int shader) {
			this.currentShader = shader;
		}

		@Override
		public Shader getShader (Renderable renderable) {
			if (currentShader == MAIN_SHADER) {
				for (MainShader s : mainShaders) {
					if (s.canRender(renderable)) {
						s.setTextures(depthTexture, normalTexture);
						return s;
					}
				}
				MainShader mainShader = new MainShader(renderable);
				mainShader.init();
				mainShader.setTextures(depthTexture, normalTexture);
				mainShaders.add(mainShader);
				return mainShader;
			} else if (currentShader == DEPTH_SHADER) {
				if (depthShader == null) {
					depthShader = new DepthShader(renderable);
					depthShader.init();
				}
				return depthShader;
			} else if (currentShader == DEFAULT_SHADER) {
				for (Shader s : defaultShaders) {
					if (s.canRender(renderable)) return s;
				}
				DefaultShader.Config config = new DefaultShader.Config();
				config.vertexShader = Gdx.files.internal("shader.vert").readString();
				config.fragmentShader = Gdx.files.internal("shader.frag").readString();
				DefaultShader defaultShader = new DefaultShader(renderable, config);
				defaultShader.init();
				defaultShaders.add(defaultShader);
				return defaultShader;
			} else if (currentShader == NORMAL_SHADER) {
				if (normalShader == null) {
					normalShader = new DefaultShader(renderable, new Config(Gdx.files.internal("normalShader.vert").readString(),
						Gdx.files.internal("normalShader.frag").readString()));
					normalShader.init();
				}
				return normalShader;
			}
			return null;
		}

		@Override
		public void dispose () {
			for (Shader s : mainShaders)
				s.dispose();
			if (depthShader != null) depthShader.dispose();
			for (Shader s : defaultShaders)
				s.dispose();
		}
	}
}
