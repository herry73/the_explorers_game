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
 * The VictoryScreen class is responsible for displaying the score and ending gameplay and accessing the menu screen.
 * It extends the LibGDX Screen class.
 */
public class VictoryScreen implements Screen {

    private final Stage stage;
    private final float elapsedTime;
    private Label timeLabel;
    private final MazeRunnerGame game;
    public Music VictoryMusic;

    /**
     * Constructor for VictoryScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public VictoryScreen(MazeRunnerGame game, float elapsedTime) {
        this.game = game;
        this.elapsedTime = elapsedTime;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f;


        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(new Label("Victory!!", game.getSkin(), "title")).padBottom(80).row();

        String timeText = String.format("Time: %.1f seconds", elapsedTime);
        timeLabel = new Label(timeText, game.getSkin(), "default");
        table.add(timeLabel).padBottom(20).row();


        game.MenuMusic.stop();
        VictoryMusic = Gdx.audio.newMusic(Gdx.files.internal("round_end.wav"));
        VictoryMusic.setLooping(true);
        VictoryMusic.setVolume(0.5f);
        VictoryMusic.play();
    }

    /**
     * The render method is used here for ensuring the menu is accessible from the victory screen upon clicking escape.
     * @param delta
     */

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
            game.MenuMusic.stop();
            VictoryMusic.stop();
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


