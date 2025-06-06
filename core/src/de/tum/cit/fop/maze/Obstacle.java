package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    private Vector2 position;
    private Texture texture;
    private Rectangle bounds;

    public Obstacle(String texture, float startX, float startY) {
        this.position = new Vector2(startX, startY);
        this.texture = new Texture(Gdx.files.internal("basictiles.png"));
        this.bounds = new Rectangle(startX, startY, this.texture.getWidth(), this.texture.getHeight());
    }
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
    public void dispose() {
        texture.dispose();
    }
    public Rectangle getBounds() {
        return bounds;
    }
}
