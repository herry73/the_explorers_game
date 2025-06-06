package de.tum.cit.fop.maze;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The Player class is responsible for handling the player movement.
 * It handles the user inputs and makes the player move accordingly.
 */

public class Player {


    private Vector2 position;
    private Texture texture;
    private float speed;
    private float x, y;
    private float width, height;
    private float velocityX, velocityY;
    private Rectangle bounds;
    public float speedMultiplier = 1.0f;
    private Level_1 level;
    private static final int TILE_SIZE = 32;

    /**
     * The attributes describe the movement directions
     */

    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterLeftAnimation;
    private Animation<TextureRegion> characterRightAnimation;
    private Animation<TextureRegion> currentAnimation;
    private float stateTime;
    private Sprite sprite;


    /**
     * Constructor for Player.
     * Loads character animation from character.png file
     * Sets the rectangle bounds of the character, current direction of movement.
     * @param texture
     * @param startX
     * @param startY
     * @param speed
     */
    public Player(String texture, float startX, float startY, float speed) {
        this.bounds = new Rectangle(startX, startY, 16, 27);
        this.position = new Vector2(startX, startY);
        this.texture = new Texture(Gdx.files.internal("character.png"));
        this.sprite = new Sprite(this.texture);
        this.speed = speed;
        loadcharacteranimation();
        currentAnimation = characterDownAnimation;
    }

    /**
     * Loads the character animation.
     * It also ensures that the character looks like it is walking when in motion.
     */

    private void loadcharacteranimation() {
        int framewidth = 16;
        int frameheight = 32;
        int frames = 4;

        TextureRegion[][] frame = TextureRegion.split(texture, framewidth, frameheight);

        Array<TextureRegion> downframes = new Array<>(TextureRegion.class);
        Array<TextureRegion> upframes = new Array<>(TextureRegion.class);
        Array<TextureRegion> leftframes = new Array<>(TextureRegion.class);
        Array<TextureRegion> rightframes = new Array<>(TextureRegion.class);


        for (int col = 0; col < frames; col++) {
            downframes.add(frame[0][col]);
            rightframes.add(frame[1][col]);
            upframes.add(frame[2][col]);
            leftframes.add(frame[3][col]);
        }
        characterDownAnimation  = new Animation<>(0.1f, downframes);
        characterUpAnimation = new Animation<>(0.1f, upframes);
        characterLeftAnimation = new Animation<>(0.1f, leftframes);
        characterRightAnimation = new Animation<>(0.1f, rightframes);
    }

    /**
     * The update method is the main method of this class which defines the movement of the character.
     * It handles input from user and updates the logic as necessary.
     * @param delta
     * @param level
     */

    public void update (float delta, Level_1 level) {
        stateTime += delta;
        boolean isMoving = false;
        Vector2 newPosition = new Vector2(position);
        float tileSize = 32;


        if (!GameScreen.getShiftOff() && (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SHIFT_RIGHT))) {
            speedMultiplier = 2.0f;
        }
        else if (speedMultiplier != 0.5f) {
            speedMultiplier = 1.0f;
        }
        else {
            speedMultiplier = 0.5f;
        }


        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            newPosition.y += speed * speedMultiplier * Gdx.graphics.getDeltaTime();
            if (level.isPathAtPosition(position.x + bounds.width/2, newPosition.y + bounds.height)) {
                position.y = newPosition.y;
                currentAnimation = characterUpAnimation;
                isMoving = true;
            }

        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            newPosition.y -= speed * speedMultiplier * Gdx.graphics.getDeltaTime();
            if (level.isPathAtPosition(position.x + bounds.width/2, newPosition.y)) {
                position.y = newPosition.y;
                currentAnimation = characterDownAnimation;
                isMoving = true;
            }


        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            newPosition.x += speed * speedMultiplier * Gdx.graphics.getDeltaTime();
            if (level.isPathAtPosition(newPosition.x + bounds.width, position.y + bounds.height/2)) {
                position.x = newPosition.x;
                currentAnimation = characterRightAnimation;
                isMoving = true;
            }
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            newPosition.x -= speed * speedMultiplier * Gdx.graphics.getDeltaTime();
            if (level.isPathAtPosition(newPosition.x, position.y + bounds.height/2)) {
                position.x = newPosition.x;
                currentAnimation = characterLeftAnimation;
                isMoving = true;
            }
        }

        if (!isMoving) {
            stateTime = 0;
        }
        bounds.setPosition(position.x, position.y);

    }

    /**
     * The render method here is responsible for drawing the player character.
     * @param batch
     */
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }


    public Rectangle getBounds() {
        return bounds;
    }


    public float getSpeed() {
        return speed;
    }


    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
