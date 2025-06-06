package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The GameOverScreen class is responsible for ending the gameplay once the player loses all lives.
 * It extends the LibGDX Screen class.
 */
public class GameOverScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;

    /**
     * Constructor for GameOverScreen. Sets up the camera, viewport, stage, and text.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameOverScreen(MazeRunnerGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f;

        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(new Label("Game Over, You lost all lives", game.getSkin(), "title")).padBottom(80).row();

    }

    /**
     * The render method is used here for ensuring the menu is accessible from the game over screen upon clicking escape.
     * It also ensures the music is stopped.
     * @param delta
     */
    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
            game.MenuMusic.stop();
            GameScreen.death.stop();
            game.backgroundMusic.play();
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Updates the stage's viewport to match the new dimensions when game window is resized.
     * It also centers the camera.
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    /**
     * Enables user input events.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}

