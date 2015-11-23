package geon.game.topview;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * <pre>
 * geon.game.topview
 * 	|_MyActions
 * 
 * 개요 : 
 * 작성일 : Nov 15, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class MyActions {
	public static Action blinkAction() {
		return Actions.repeat(3,
			Actions.sequence(
				Actions.alpha(0.2f, 0.5f),
				Actions.alpha(1, 0.5f)
			)
		);
	}
}
