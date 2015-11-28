package geon.game.topview.screen;

import geon.game.topview.AnimatedActor;
import geon.game.topview.GameMain;
import geon.game.topview.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
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
	Table table;
	GameMain game;
	Label text;
	
	public StoryScreen (final GameMain game) {
		viewport = new ScreenViewport();
		stage = new Stage(viewport, Resources.batch);
		this.game = game;
		Gdx.input.setInputProcessor(stage);
		
		table = new Table();
		table.debug();
		stage.addActor(table);
		table.setFillParent(true);
		text=new Label("병학은 세상에서 가장 강한 학이다.", Resources.skin);
		
		
		final AnimatedActor titleImage=new AnimatedActor(Resources.title, 2f);
		
		table.pack();
		
		stage.addAction(Actions.sequence(
				new RunnableAction(){
					public void run(){
						table.add(text);
						text.addAction(Actions.sequence(
								Actions.alpha(0),
								Actions.alpha(1, 1f)
							));
					}
				}
				,
				Actions.delay(3f)
				,
				new RunnableAction(){
					public void run(){
						text=new Label("근데 병 학이 먹으려고하던 파오후 쿰척쿰척 토스트가 없어졌다.",Resources.skin);
						table.add(text);
						text.addAction(Actions.sequence(
								Actions.alpha(0),
								Actions.alpha(1, 1f)
							));
					}
				}
				,
				Actions.delay(4f)
				,
				new RunnableAction(){
					public void run(){
						text=new Label("그래서...",Resources.skin);
						table.add(text);
						text.addAction(Actions.sequence(
								Actions.alpha(0),
								Actions.alpha(1, 1f)
							));
					}
				}
				,
				Actions.delay(3f)
				,
				new RunnableAction(){
					public void run(){
						table.clear();
					}
				}
				,
				Actions.delay(3f)
				,
				new RunnableAction(){
					public void run(){
						table.add(titleImage).size(600, 300);
						titleImage.addAction(Actions.sequence(
								Actions.alpha(0),
								Actions.alpha(1, 1f)
							));
					}
				}
				,
				Actions.delay(5f)
				,
				new RunnableAction(){
					public void run(){
						table.clear();
					}
				}
				,
				Actions.delay(2f)
				,
				//----------------------------------------------여기 채우면 됨
				,
				new RunnableAction() {
					public void run() {
						loadingComplete();
					}
				}
		));
	}
	
	public void stoty2Screen() {
		
	}
	
	public void loadingComplete(){
		stage.addAction(Actions.sequence(Actions.delay(0.5f), new Action() {
			@Override
			public boolean act (float delta) {
				table.addAction(Actions.color(Color.WHITE, 0.5f));
				game.switchToGamePlay();
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
