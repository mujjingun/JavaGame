package geon.game.topview.screen;

import geon.game.topview.GameMain;
import geon.game.topview.MyActions;
import geon.game.topview.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * <pre>
 * geon.game.topview
 * 	|_StoryScreen
 * 
 * 개요 : 
 * 작성일 : Nov 14, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class StoryScreen implements Screen {

	Stage stage;
	ScreenViewport viewport;
	
	public StoryScreen (final GameMain game) {
		viewport = new ScreenViewport();
		stage = new Stage(viewport, Resources.batch);
		Gdx.input.setInputProcessor(stage);
		
		final Table table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		
		final Label loadingLabel = new Label("맵을 생성하는 중...", Resources.skin);
		loadingLabel.addAction(MyActions.blinkAction());
		table.add(loadingLabel);
		
		table.pack();
		
		Actor delayActor = new Actor();
		stage.addActor(delayActor);
		delayActor.addAction(Actions.sequence(Actions.delay(0.5f), new Action() {
			@Override
			public boolean act (float delta) {
				table.addAction(Actions.color(Color.WHITE, 0.5f));
				loadingLabel.addAction(Actions.sequence(Actions.fadeOut(0.5f), new Action() {
					
					@Override
					public boolean act (float delta) {
						loadingLabel.remove();
						game.switchToGamePlay();
						return true;
					}
				}));
				return true;
			}
		}));
	}
	
	@Override
	public void show () {
		
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose () {
		// TODO Auto-generated method stub
		
	}

}
