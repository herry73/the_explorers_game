package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
 * The PauseScreen class is responsible for pausing the gameplay and allowing the user to resume or start afresh.
 * It extends the LibGDX Screen class.
 */
public class PauseScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;
    private final GameScreen gameScreen;

    /**
     * Constructor for PauseScreen. Sets up the camera, viewport, stage, and Text.
     *
     * @param game The main game class, used to access global resources and methods.
     * @param gameScreen to ensure game is resumed and not started afresh.
     */
    public PauseScreen(MazeRunnerGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f;

        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(new Label("Game Paused!", game.getSkin(), "title")).padBottom(80).row();

        /**
         * The buttons here handle the pause and resume aspect of the game.
         * Resume brings the game back as it is.
         * Main menu button redirects the user to main menu.
         */

        TextButton ResumeButton = new TextButton("Resume game", game.getSkin());
        table.add(ResumeButton).width(500).row();
        ResumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(gameScreen);
                game.backgroundMusic.pause();
                game.MenuMusic.play();
            }
        });

        TextButton MenuButton = new TextButton("Go to main menu", game.getSkin());
        table.add(MenuButton).width(500).row();
        MenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu();
            }
        });
    }

    /**
     * The main task of the render method here is to draw the stage.
     * @param delta
     */

    @Override
    public void render(float delta) {
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
