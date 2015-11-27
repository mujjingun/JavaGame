package geon.game.topview.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * <pre>
 * geon.game.topview.components
 * 	|_RenderableComponent
 * 
 * 개요 : 
 * 작성일 : Nov 25, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class RenderableComponent implements Component {
	
	public ModelInstance model;

	/**
	 * @param model
	 */
	public RenderableComponent (ModelInstance model) {
		this.model = model;
	}
	
	
}
