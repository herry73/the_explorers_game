package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

/**
 * The Enemy class is responsible for handling the movement of the enemy.
 * It randomizes where the enemy goes to.
 */

public class Enemy {
    private Vector2 position;
    private TextureRegion texture;
    private float speed;
    private Rectangle bounds;
    private Random random;
    private Vector2 currdirection;
    private float moveTimer;
    private static final float directionChangefreq = 0.5f;
    protected Texture thingsTexture;

    /**
     * Constructor for the Enemy class.
     * @param texture
     * @param startX
     * @param startY
     * @param speed
     */
    public Enemy(String texture, float startX, float startY, float speed) {
        this.position = new Vector2(startX, startY);
        this.speed = speed;
        this.bounds = new Rectangle(startX, startY, 25,25);
        this.random = new Random();
        this.moveTimer = 0;
        this.currdirection = new Vector2(0, 0);
        this.thingsTexture = new Texture(Gdx.files.internal("mobs.png"));
        this.texture = new TextureRegion(thingsTexture, 6 * 16, 4 * 16, 16, 16);
    }

    /**
     * The update method is responsible for updating the enemy's position and behavior in the game.
     * Checks whether to change direction or not and checks whether the move is valid or not.
     * @param delta
     * @param level
     * @param player
     */

    public void update(float delta, Level_1 level, Player player) {
        moveTimer += delta;

        if (moveTimer >= directionChangefreq) {
            chooseRandomDirection();
            moveTimer = 0;
        }

        Vector2 newPosition = new Vector2(position);
        newPosition.add(currdirection);

        if (isValidMove(level, newPosition)) {
            position.set(newPosition);
            bounds.setPosition(position.x, position.y);
        } else {
            chooseRandomDirection();
        }
    }

    /**
     * The chooseRandomDirection method is a switch case which sets the direction based on the random number chosen.
     */

    private void chooseRandomDirection() {
        int directionChoice = random.nextInt(4);
        switch (directionChoice) {
            case 0: currdirection.set(0, 3); break;
            case 1: currdirection.set(0, -3); break;
            case 2: currdirection.set(3, 0); break;
            case 3: currdirection.set(-3, 0); break;
            default: currdirection.set(0, 0); break;
        }
    }

    /**
     * The method isValidMove checks if the path is walkable or not.
     * It does so by calling the IsPathAtPositionforEnemy method from the Level_1 class.
     * @param level
     * @param newPosition
     * @return
     */

    private boolean isValidMove(Level_1 level, Vector2 newPosition) {
        return level.isPathAtPositionforEnemy(newPosition.x + bounds.width/2, newPosition.y + bounds.height/2) &&
                level.isPathAtPositionforEnemy(newPosition.x, newPosition.y) &&
                level.isPathAtPositionforEnemy(newPosition.x + bounds.width, newPosition.y + bounds.height);
    }

    /**
     * The render method is responsible for drawing the enemy on the screen using SpriteBatch.
     * @param batch
     */

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, bounds.width, bounds.height);
    }
    /**
     * ensures the cleanup of graphical resources.
     */
    public void dispose() {
        if (thingsTexture != null) {
            thingsTexture.dispose();
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}