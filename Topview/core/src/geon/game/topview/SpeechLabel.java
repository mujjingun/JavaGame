package geon.game.topview;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * <pre>
 * geon.game.topview
 * 	|_SpeechLabel
 * 
 * 개요 : Speech Bubbles
 * 작성일 : Nov 28, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class SpeechLabel extends Label {

	private float dur, elasped, one;
	private int th = 0, len;
	private String text;
	/**
	 * @param text
	 * @param skin
	 */
	public SpeechLabel (CharSequence text, float dur, Skin skin) {
		super(text, skin, "speech");
		this.dur = dur + 1f;
		one = dur / text.length();
		len = text.length();
		this.text = text.toString();
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		if(one * th < elasped && th < len) {
			th++;
			setText(text.subSequence(0, th));
		}
		super.draw(batch, parentAlpha);
	}

	@Override
	public void act (float delta) {
		elasped += delta;
		if(elasped > dur) {
			this.remove();
		}
		super.act(delta);
	}

}
