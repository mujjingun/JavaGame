
package geon.game.topview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public enum BlockType {
	Air(0), Dirt(1);
	private int blockId;
	private ModelInstance[] blockModel = new ModelInstance[6];
	private ModelBuilder modelBuilder = new ModelBuilder();

	private Model createPlaneModel (final float width, final float height, final Material material) {
		modelBuilder.begin();
		MeshPartBuilder bPartBuilder = modelBuilder.part("rect", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal
			| Usage.TextureCoordinates, material);
		// NOTE ON TEXTURE REGION, MAY FILL OTHER REGIONS, USE GET region.getU() and so on
		bPartBuilder.setUVRange(0, 0, 1, 1);
		bPartBuilder.rect(-(width * 0.5f), -(height * 0.5f), .5f, (width * 0.5f), -(height * 0.5f), .5f, (width * 0.5f),
			(height * 0.5f), .5f, -(width * 0.5f), (height * 0.5f), .5f, 0, 0, 1);
		return (modelBuilder.end());
	}

	public static boolean anisotropicSupported = false;
	public static float maxAnisotropy;
	public static Texture makeTexture(String name) {
		Texture t = new Texture(Gdx.files.internal(name));
		if(anisotropicSupported) {
			t.bind();
			Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAX_ANISOTROPY_EXT, Math.min(16, maxAnisotropy));
		}
		return t;
	}
	
	private BlockType (int blockId) {
		this.blockId = blockId;
		if (blockId != 0) {
			Texture t = makeTexture("id" + blockId + ".png");
			
			Material m = new Material(new TextureAttribute(TextureAttribute.Diffuse, t));
			blockModel[0] = new ModelInstance(createPlaneModel(1, 1, m));
			blockModel[0].transform.setToRotation(Vector3.Y, -90);
			blockModel[1] = new ModelInstance(createPlaneModel(1, 1, m));
			blockModel[1].transform.setToRotation(Vector3.Y, 90);
			blockModel[2] = new ModelInstance(createPlaneModel(1, 1, m));
			blockModel[2].transform.setToRotation(Vector3.X, 90);
			blockModel[3] = new ModelInstance(createPlaneModel(1, 1, m));
			blockModel[3].transform.setToRotation(Vector3.X, -90);
			blockModel[4] = new ModelInstance(createPlaneModel(1, 1, m));
			blockModel[4].transform.setToRotation(Vector3.Y, 180);
			blockModel[5] = new ModelInstance(createPlaneModel(1, 1, m));
		}
	}

	public ModelInstance[] getModel () {
		return blockModel;
	}
}
