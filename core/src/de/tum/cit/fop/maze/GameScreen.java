package de.tum.cit.fop.maze;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.utils.viewport.*;


/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 * It extends the LibGDX screen class.
 */
public class GameScreen implements Screen {


    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private Level_1 level1;
    private float sinusInput = 0f;
    private Viewport viewport;
    private Player player;
    private Enemy enemy;
    private Texture arrowtexture;
    private Sprite arrowsprite;

    private static final float WIDTH = Gdx.graphics.getWidth();
    private static final float HEIGHT = Gdx.graphics.getHeight();

    /**
     * Below are the attributes/variables that are used by the methods in this class.
     */
    private float elapsedTime = 0f;
    private boolean hasCollided = false;
    private long timeAfterColl = 0;
    private static final int duration = 2000;
    private float playerOpacity = 1f;
    private int LivesLeft = 3;
    private int keysobtained = 0;
    private int Powup = 0;
    public boolean ThirdOb = false;
    public Music LivesCollectMusic;
    public Music powerupMusic;
    public Music ThirdObMusic;
    public Music keysJiggle;
    public static Music death;
    public static int gameState;
    public final int playState = 1;
    public final int pauseState = 0;
    public static boolean ShiftOff = false;
    private Stage stage;
    private Table table;


    /**
     * Constructor for GameScreen. Sets up the camera, font, the player, and the enemy.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        level1 = new Level_1();
        font = game.getSkin().getFont("font");


        this.player = new Player("character", -20, 30, 100);
        this.enemy = new Enemy("enemy", 160, 160, 47);

        camera = new OrthographicCamera(player.getPosition().x, player.getPosition().y);
        camera.setToOrtho(false);
        camera.zoom = 0.35f;
        viewport = new ScreenViewport(camera);
        gameState = playState;
    }

    public static boolean getShiftOff() {
        return ShiftOff;
    }


    @Override
    public void show() {
        stage = new Stage(new ScreenViewport(camera));
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        arrowtexture = new Texture(Gdx.files.internal("Arrow.png"));
        arrowsprite = new Sprite(arrowtexture);
        arrowsprite.setOriginCenter();
    }

    public static void setGameState(int state) {
        gameState = state;
    }

    /**
     * This method checks whether the character is on a specific tile.
     * This method is used to detect whether the player interacts with some of the game elements like traps and more.
     *
     * @param playerBounds
     * @param tileBounds
     *
     * @return a boolean value indicating if character is on tile or not
     */
    private boolean isPlayerOnTile(Rectangle playerBounds, Rectangle tileBounds) {
        float playerCenterX = playerBounds.x + playerBounds.width / 2;
        float playerCenterY = playerBounds.y + playerBounds.height / 2;
        return tileBounds.contains(playerCenterX, playerCenterY);
    }

    /**
     * The render method is used to update game state and logic, handle input, and perform appropriate tasks based on the situation in the game.
     *
     * @param delta
     */
    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
            game.MenuMusic.stop();
            game.backgroundMusic.play();
            game.setScreen(new PauseScreen(game, this));
            return;
        }
        /**
         * Zooming in and out of the game.
         */
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (!hasCollided) {
                    camera.zoom += amountY * 0.1f;
                    camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 2.0f);
                }
                return true;
            }
        });

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 2.0f);


        ScreenUtils.clear(0, 0, 0, 1);


        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        if (gameState == playState) {
            player.update(delta, level1);
            enemy.update(delta,level1,player);
            elapsedTime += delta;
        }
        if (gameState == pauseState) {}

        if (hasCollided) {
            ShiftOff = true;
        }

        /**
         * conditional statement which detects if enemy and player overlap.
         * if they do, the number of lives is reduced by one and a timer is started.
         *
         * the camera is zoomed in till the timer turns off. During this time, the player can't lose lives.
         * after timer is up, game is back to normal.
        */
        if (!hasCollided && player.getBounds().overlaps(enemy.getBounds()) && player.getBounds().getCenter(new Vector2()).dst(enemy.getBounds().getCenter(new Vector2())) < 20f) {
            hasCollided = true;
            timeAfterColl = System.currentTimeMillis();
            LivesLeft --;
            camera.zoom -= 0.15f;
            ThirdObMusic = Gdx.audio.newMusic(Gdx.files.internal("cannon_fire.wav"));
            ThirdObMusic.play();
        }

        if (LivesLeft == 0) {
            System.out.println("Game Over");
            game.setScreen(new GameOverScreen(game));
            death = Gdx.audio.newMusic(Gdx.files.internal("death.wav"));
            death.setLooping(true);
            death.play();
        }

        /**
         * Below are three iterators, which iterate through the maze and look for the respective elements.
         * These include: hearts on ground, power ups, and keys.
         *
         * The respective objects are removed from the maze upon interaction with the player.
         * Upon interaction, a distinct sound effect is played for each different object in the maze.
         */

        Iterator<Rectangle> livesIterator = level1.getLives().iterator();
        while (livesIterator.hasNext()) {
            Rectangle life = livesIterator.next();
            if (isPlayerOnTile(player.getBounds(), life)) {
                LivesLeft++;
                level1.removeLife(life);
                livesIterator.remove();
                LivesCollectMusic = Gdx.audio.newMusic(Gdx.files.internal("coin.mp3"));
                LivesCollectMusic.play();
            }
        }
        /**
         * In this case the power up as well as the closest trap to the power up are removed simultaneously.
         */

        Iterator<Rectangle> PowerUpIterator = level1.getPowerUps().iterator();
        while (PowerUpIterator.hasNext()) {
            Rectangle powerup = PowerUpIterator.next();
            if (isPlayerOnTile(player.getBounds(), powerup)) {
                level1.removePowerUp(powerup);
                PowerUpIterator.remove();
                powerupMusic = Gdx.audio.newMusic(Gdx.files.internal("powerupMusic.wav"));
                powerupMusic.play();

                Rectangle closestTrap = null;
                float minDistance = Float.MAX_VALUE;

                for (Rectangle trap : level1.getTraps()) {
                    float distance = player.getPosition().dst(new Vector2(trap.x, trap.y));
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestTrap = trap;
                    }
                }

                if (closestTrap != null) {
                    level1.removeTrap(closestTrap);
                }
            }
        }

        Iterator<Rectangle> keyIterator = level1.getKeys().iterator();
        while (keyIterator.hasNext()) {
            Rectangle key = keyIterator.next();
            if (isPlayerOnTile(player.getBounds(), key)) {
                keysobtained++;
                level1.removeKey(key);
                keyIterator.remove();
                keysJiggle = Gdx.audio.newMusic(Gdx.files.internal("Key Jiggle.wav"));
                keysJiggle.play();

                if (keysobtained == 3) {
                    for (Rectangle block : level1.getBlock()){
                        level1.removeBlockage(block);
                    }
                }
            }
        }

        /**
         * Below are for loops which detect whether the player has interacted with these objects or not.
         *
         * Unlike the objects with the iterators, these objects don't disappear upon interaction with the player.
         */

        for (Rectangle exit : level1.getExit()) {
            if (isPlayerOnTile(player.getBounds(), exit)) {
                game.setScreen(new VictoryScreen(game, elapsedTime));
                return;
            }
        }

        for (Rectangle obstacle : level1.getObstacle()) {
            if (isPlayerOnTile(player.getBounds(), obstacle)) {
                if (!hasCollided) {
                    hasCollided = true;
                    ThirdOb = true;
                    camera.zoom -= 0.15f;
                    timeAfterColl = System.currentTimeMillis();
                    LivesLeft--;
                    player.speedMultiplier = 0.5f;
                    System.out.println(timeAfterColl);
                    ThirdObMusic = Gdx.audio.newMusic(Gdx.files.internal("cannon_fire.wav"));
                    ThirdObMusic.play();
                }
            }
        }

        for (Rectangle trap : level1.getTraps()) {
            if (isPlayerOnTile(player.getBounds(), trap)) {

                if (!hasCollided) {
                    hasCollided = true;
                    camera.zoom -= 0.15f;
                    timeAfterColl = System.currentTimeMillis();
                    LivesLeft--;
                    System.out.println("trap!!! Lives left: " + LivesLeft);
                    ThirdObMusic = Gdx.audio.newMusic(Gdx.files.internal("cannon_fire.wav"));
                    ThirdObMusic.play();
                }

                if (LivesLeft == 0) {
                    System.out.println("Game Over");
                    game.setScreen(new GameOverScreen(game));
                    death = Gdx.audio.newMusic(Gdx.files.internal("death.wav"));
                    death.setLooping(true);
                    death.play();
                }
            }
        }

        if (hasCollided && (System.currentTimeMillis() - timeAfterColl) > duration) {
            hasCollided = false;
            ShiftOff = false;
            camera.zoom += 0.15f;
            player.speedMultiplier=1f;
            ThirdOb = false;
        }

        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        /**
         * Below is the code snippet for handling the rendering of the defined game elements using SpriteBatch.
         */

        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin();

        level1.render(game.getSpriteBatch());

        font.draw(game.getSpriteBatch(), " ", textX, textY);
        renderLivesLeft();
        renderKeys();
        renderTimer();

        game.getSpriteBatch().draw(
                game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                textX - 96,
                textY - 64,
                64,
                128
        );

        game.getSpriteBatch().setColor(Color.WHITE);
        updateArrowDirection();
        arrowsprite.draw(game.getSpriteBatch());
        player.render(game.getSpriteBatch());
        enemy.render(game.getSpriteBatch());

        game.getSpriteBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * The updateArrowDirection method takes the arrow sprite and rotates it around the center to always point to the exit.
     */

    private void updateArrowDirection() {
        float playerX = player.getPosition().x;
        float playerY = player.getPosition().y;

        List<Rectangle> Exit = level1.getExit();

        if (!Exit.isEmpty()) {
            Rectangle exit = Exit.get(0);
            float exitX = exit.getX() + exit.getWidth()/4;
            float exitY = exit.getY() + exit.getHeight()/4;

            float angle = (float) Math.toDegrees(Math.atan2(exitY - playerY, exitX - playerX));

            float distX = Math.abs(playerX - exitX);
            float distY = Math.abs(playerY - exitY);

            if (distX < 100 && distY < 10) {
                angle = 0;
            }

            arrowsprite.setRotation(angle);
            arrowsprite.setScale(0.05f);
            arrowsprite.setPosition(playerX - 300, playerY - 140);
        }
    }

    /**
     * Below are the methods responsible for rendering the UI elements that are visible at the top right of the screen.
     * These methods look after what is to be displayed and where.
     * They also ensure that the text is positioned with the screen.
     */

    private void renderKeys() {
        font.setColor(Color.WHITE);
        game.getSpriteBatch().setProjectionMatrix(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()).combined);

        float x = (Gdx.graphics.getWidth() - font.getRegion().getRegionWidth()) / 2 - 40;
        float y = (Gdx.graphics.getHeight() - font.getLineHeight()) / 2 - 250;

        font.draw(game.getSpriteBatch(), "Keys: " + keysobtained, x, y);
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
    }


    private void renderLivesLeft() {
        font.setColor(Color.WHITE);
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().setProjectionMatrix(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()).combined);

        float x = (Gdx.graphics.getWidth() - font.getRegion().getRegionWidth()) / 2 - 125;
        float y = (Gdx.graphics.getHeight() + font.getLineHeight()) / 2 - 30;

        font.draw(game.getSpriteBatch(), "Lives left: " + LivesLeft, x, y);
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
    }


    private void renderTimer() {

        font.setColor(Color.WHITE);
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().setProjectionMatrix(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()).combined);

        float x = (Gdx.graphics.getWidth() - font.getRegion().getRegionWidth()) / 2 - 70;
        float y = (Gdx.graphics.getHeight() + font.getLineHeight()) / 2 - 140;

        font.draw(game.getSpriteBatch(), "Time: " + String.format("%.1f", elapsedTime), x, y);
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
        viewport.update(width, height, true);
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


    @Override
    public void dispose() {
        player.dispose();
        enemy.dispose();
    }
}
