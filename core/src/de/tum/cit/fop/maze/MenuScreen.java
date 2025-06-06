package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
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
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f;

        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        table.add(new Label("Choose your maze!", game.getSkin(), "title")).padBottom(80).row();

        /**
         * The text buttons below allow the user to load a desired maze from the properties file.
         * User just clicks on the maze they want to play in.
         */

        TextButton maze1button = new TextButton("Maze 1", game.getSkin());
        table.add(maze1button).width(300).row();
        maze1button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Level_1.setMazePath("maps/level-1.properties");
                game.goToGame();
            }
        });

        TextButton maze2button = new TextButton("Maze 2", game.getSkin());
        table.add(maze2button).width(300).row();
        maze2button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Level_1.setMazePath("maps/level-2.properties");
                game.goToGame();
            }
        });

        TextButton maze3button = new TextButton("Maze 3", game.getSkin());
        table.add(maze3button).width(300).row();
        maze3button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Level_1.setMazePath("maps/level-3.properties");
                game.goToGame();
            }
        });

        TextButton maze4button = new TextButton("Maze 4", game.getSkin());
        table.add(maze4button).width(300).row();
        maze4button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Level_1.setMazePath("maps/level-4.properties");
                game.goToGame();
            }
        });

        TextButton maze5button = new TextButton("Maze 5", game.getSkin());
        table.add(maze5button).width(300).row();
        maze5button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Level_1.setMazePath("maps/level-5.properties");
                game.goToGame();
            }
        });

    }

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
