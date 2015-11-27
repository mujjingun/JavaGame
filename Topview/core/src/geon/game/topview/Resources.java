package geon.game.topview;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.UBJsonReader;

/**
 * <pre>
 * geon.game.topview
 * 	|_Resources
 * 
 * 개요 : 
 * 작성일 : Nov 14, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class Resources {
	
	public static Material material;
	public static Texture texture;
	public static SpriteBatch batch;

	public static BitmapFont font;

	public static Skin skin;
	public static Drawable healthBar, healthBarFill, cross;
	public static AnimationDrawable title;
	
	public static ModelInstance byeonghack;
	public static AnimationController byeonghackController;
	
	public static Drawable alphaBackground;

	private static Texture makeTexture(String name) {
		Texture t = new Texture(Gdx.files.internal(name));
		return t;
	}
	
	public static void load() {

		FloatBuffer buffer = BufferUtils.newFloatBuffer(64);
		Gdx.gl.glGetFloatv(GL20.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, buffer);
		float maxAnisotropy = buffer.get(0);
		System.out.println(maxAnisotropy);
		
		texture = makeTexture("id1.png");
		
		Texture occlusion = makeTexture("occlusion.png");
		occlusion.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		material = new Material(
			new TextureAttribute(TextureAttribute.Diffuse, texture),
			new TextureAttribute(TextureAttribute.Ambient, occlusion)
		);
		batch = new SpriteBatch();
		
		font = new BitmapFont(Gdx.files.internal("barun2.fnt"));
		font.getData().setScale(2f);
		
		skin = new Skin();
		skin.add("default", font);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;
		skin.add("default", labelStyle);
		
		healthBar = new NinePatchDrawable(new NinePatch(makeTexture("healthbar.png"), 5, 5, 5, 5));
		healthBarFill = new NinePatchDrawable(new NinePatch(makeTexture("healthbarfill.png"), 5, 5, 5, 5));
		
		cross = new SpriteDrawable(new Sprite(makeTexture("cross.png")));
		
		UBJsonReader jsonReader = new UBJsonReader();
		G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
		
		Model byeonghackModel = modelLoader.loadModel(Gdx.files.internal("byeonghack.g3db"));
		byeonghack = new ModelInstance(byeonghackModel);
		byeonghack.transform.scale(0.4f, 0.4f, 0.4f);
		
		byeonghackController = new AnimationController(byeonghack);
		byeonghackController.setAnimation("Armature|walk", -1);
		
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.drawPixel(0, 0, Color.argb8888(0, 0, 0, 0.5f));
		alphaBackground = new SpriteDrawable(new Sprite(new Texture(pixmap)));
		
		Texture titleTexture=makeTexture("title.png");
		TextureRegion[][] titleTex=TextureRegion.split(titleTexture,100,44);
		Animation titleAnim=new Animation(0.2f, titleTex[0]);
		
		title=new AnimationDrawable(titleAnim);
	}
}
