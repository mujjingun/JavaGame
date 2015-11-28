
package geon.game.topview.screen;

import geon.game.topview.AnimatedActor;
import geon.game.topview.GameMain;
import geon.game.topview.Resources;
import geon.game.topview.SpeechLabel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** <pre>
 * geon.game.topview
 * 	|_StoryScreen
 * 
 * 개요 : The Story.
 * 작성일 : Nov 14, 2015
 * </pre>
 *
 * @author 박건
 * @version 1.0 */
public class StoryScreen implements Screen {

	Stage stage;
	Table table;
	GameMain game;
	Label text;

	public StoryScreen (final GameMain game) {
		final ScreenViewport viewport = new ScreenViewport();
		stage = new Stage(viewport, Resources.batch);

		this.game = game;
		Gdx.input.setInputProcessor(stage);

		table = new Table();
		// table.debug();
		stage.addActor(table);
		table.setFillParent(true);
		text = new Label("병학은 세상에서 가장 강한 학이다.", Resources.skin);

		final AnimatedActor titleImage = new AnimatedActor(Resources.title, 2f);

		table.pack();

		Table skipTable = new Table();
		stage.addActor(skipTable);
		skipTable.setFillParent(true);
		TextButton skip = new TextButton("Skip>>", Resources.skin);
		skipTable.add(skip).expand().bottom().right().pad(10f, 10f, 10f, 10f);
		skip.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, 0.5f),
			Actions.forever(Actions.sequence(Actions.alpha(0.2f, 0.5f), Actions.alpha(1, 0.5f)))));
		skip.addListener(new ClickListener() {

			@Override
			public void clicked (InputEvent event, float x, float y) {
				loadingComplete();
			}

		});
		skipTable.pack();

		// loadingComplete();

		final AnimatedActor junlae = new AnimatedActor(Resources.junlae, 0f);
		final AnimatedActor munguang = new AnimatedActor(Resources.munguang, 0f);
		final AnimatedActor byonghak = new AnimatedActor(Resources.byonghak, 0f);

		stage.addAction(Actions.sequence(
		// fade in
			new RunnableAction() {
				public void run () {
					table.add(text);
					text.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, 1f)));
				}
			}, Actions.delay(3f), new RunnableAction() {
				public void run () {
					text = new Label("근데 병 학이 먹으려고 하던 파오후쿰척쿰척 토스트가 없어졌다.", Resources.skin);
					table.add(text);
					text.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, 1f)));
				}
			}, Actions.delay(4f), new RunnableAction() {
				public void run () {
					text = new Label("그래서 병학은 무고한 준래를 의심하는데...", Resources.skin);
					table.add(text);
					text.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, 1f)));
				}
			}, Actions.delay(2f), new RunnableAction() {
				public void run () {
					// show classroom, junlae and mungwang
					table.clear();
					stage.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, 2f)));
					Image back = new Image(Resources.classroom);
					table.add(back).expand().fill();
					back.setScaling(Scaling.fit);

					float relX = viewport.getWorldWidth();
					float relY = viewport.getWorldHeight();
					junlae.setPosition(relX * 0.4f, relY * 0.5f);
					stage.addActor(junlae);
					munguang.setPosition(relX * 0.2f, relY * 0.6f);
					stage.addActor(munguang);
					byonghak.setPosition(relX * 1.2f, relY * 0.5f);
					stage.addActor(byonghak);
					byonghak.addAction(Actions.moveTo(relX * 0.7f, relY * 0.55f, 1f));
				}
			}, Actions.delay(1.5f), new RunnableAction() {
				public void run () {
					SpeechLabel speech = new SpeechLabel("야 박준래 니가 내 \n쿰척쿰척 토스트 먹었냐?", 2f, Resources.skin);
					speech.setPosition(byonghak.getX(), byonghak.getY());
					stage.addActor(speech);
				}
			}, Actions.delay(2f), new RunnableAction() {
				public void run () {
					SpeechLabel speech = new SpeechLabel("아니", 0.6f, Resources.skin);
					speech.setPosition(junlae.getX(), junlae.getY());
					stage.addActor(speech);
				}
			}, Actions.delay(2f), new RunnableAction() {
				public void run () {
					SpeechLabel speech = new SpeechLabel("야 따라와 거짓말하면 죽는다", 1f, Resources.skin);
					speech.setPosition(byonghak.getX(), byonghak.getY());
					stage.addActor(speech);
				}
			}, Actions.delay(3f), new RunnableAction() {
				public void run () {

					float relX = viewport.getWorldWidth();
					float relY = viewport.getWorldHeight();

					junlae.addAction(Actions.moveTo(byonghak.getX() - relX * 0.05f, byonghak.getY(), 1f));
				}
			}, Actions.delay(1f), new RunnableAction() {
				public void run () {

					float relX = viewport.getWorldWidth();
					float relY = viewport.getWorldHeight();

					junlae.addAction(Actions.moveTo(relX * 1.2f, relY * 0.2f, 2f));
					byonghak.addAction(Actions.moveTo(relX * 1.2f, relY * 0.2f, 2f));
				}
			}, Actions.delay(2f), new RunnableAction() {
				public void run () {
					SpeechLabel speech = new SpeechLabel("안돼 이럴 순 없어\n준래를 구해야겠어!!!", 3f, Resources.skin);
					speech.setPosition(munguang.getX(), munguang.getY());
					stage.addActor(speech);
				}
			}, Actions.delay(4f), new RunnableAction() {
				public void run () {
					stage.addAction(Actions.sequence(Actions.alpha(1), Actions.alpha(0, 3f), Actions.alpha(1)));
				}
			}, Actions.delay(3f), new RunnableAction() {
				public void run () {
					junlae.remove();
					munguang.remove();
					byonghak.remove();
					table.clear();
					table.add(titleImage).size(600, 300);
					titleImage.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, 2f), Actions.delay(2f),
						Actions.alpha(0, 1f)));
				}
			}, Actions.delay(6f), new RunnableAction() {
				public void run () {
					table.clear();
					loadingComplete();
				}
			}));
	}

	public void stoty2Screen () {

	}

	public void loadingComplete () {
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
