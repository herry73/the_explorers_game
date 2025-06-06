package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import java.util.List;
import java.util.ArrayList;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * The Level_1 class is responsible for loading the mazes from the properties file and realizing them.
 * It also loads the specific objects for each of the textures in the game.
 */
public class Level_1 {
    private static final int TILE_SIZE = 32;
    private Map<Vector2, Integer> mazeLayout;
    private static String mazePath = "maps/level-1.properties";
    private TextureRegion wallTexture;
    private TextureRegion pathTexture;
    private TextureRegion trapTexture;
    private TextureRegion enemyTexture;
    private TextureRegion keyTexture;
    private TextureRegion exitTexture;
    private TextureRegion backgroundTexture;
    private TextureRegion livesTexture;
    private TextureRegion powerUpTexture;
    private TextureRegion ObstacleTexture;
    private TextureRegion blockageTexture;
    protected Texture basictilesTexture;
    protected Texture thingsTexture;
    protected Texture objecttexture;
    protected Texture traptexture;
    protected TextureRegion hiddentexture;

    private int mazeWidth;
    private int mazeHeight;

    public static void setMazePath(String Path) {
        mazePath = Path;
    }

    /**
     * Constructor for Level_1.
     * Stores the path to the maze properties file.
     */

    public Level_1() {
        mazeLayout = new HashMap<>();
        loadTextures();
        loadMaze(mazePath);
    }

    private void loadTextures() {
        int tileWidth = 16;
        int tileHeight = 16;

        /**
         * Loads the textures for every object present in the maze.
         * Also, loads the png files for the textures below.
         */

        basictilesTexture = new Texture(Gdx.files.internal("basictiles.png"));
        thingsTexture = new Texture(Gdx.files.internal("mobs.png"));
        objecttexture = new Texture(Gdx.files.internal("objects.png"));
        traptexture = new Texture(Gdx.files.internal("things.png"));

        wallTexture = new TextureRegion(basictilesTexture, 0, 0, tileWidth, tileHeight);
        pathTexture = new TextureRegion(basictilesTexture, 1 * tileWidth, 11 * tileHeight, tileWidth, tileHeight);
        trapTexture = new TextureRegion(basictilesTexture, 4 * tileWidth, 7 * tileHeight, tileWidth, tileHeight);
        enemyTexture = new TextureRegion(thingsTexture, 2 * tileWidth, 2 * tileHeight, tileWidth, tileHeight);
        keyTexture = new TextureRegion(basictilesTexture, 4 * tileWidth, 4 * tileHeight, tileWidth, tileHeight);
        exitTexture = new TextureRegion(basictilesTexture, 2 * tileWidth, 6 * tileHeight, tileWidth, tileHeight);
        backgroundTexture = new TextureRegion(basictilesTexture, 2 * tileWidth, 1 * tileHeight, tileWidth, tileHeight);
        livesTexture = new TextureRegion(objecttexture, 0 * tileWidth, 3 * tileHeight, tileWidth, tileHeight);
        powerUpTexture = new TextureRegion(basictilesTexture, 3 * tileWidth, 3 * tileHeight, tileWidth, tileHeight);
        ObstacleTexture = new TextureRegion(basictilesTexture, 3 * tileWidth, 9 * tileHeight, tileWidth, tileHeight);
        blockageTexture = new TextureRegion(traptexture, 10 * tileWidth, 4 * tileHeight, tileWidth, tileHeight);
        hiddentexture = new TextureRegion(basictilesTexture, 6 * tileWidth, 2 * tileHeight, tileWidth, tileHeight);

    }

    /**
     * Loads a maze layout from a file and stores it.
     * @param filePath
     * @throws IOException and prints the stack trace
     */

    private void loadMaze(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
            int maxX = 0;
            int maxY = 0;

            for (String key : properties.stringPropertyNames()) {
                String[] coords = key.split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int value = Integer.parseInt(properties.getProperty(key));
                mazeLayout.put(new Vector2(x, y), value);


                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
            }

            mazeWidth = maxX + 1;
            mazeHeight = maxY + 1;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Detects if the tile ahead of the character and enemy is walkable i.e. is there a wall ahead?
     * @param x
     * @param y
     * @return a boolean indicating the path is either walkable or not.
     */
    public boolean isPathAtPosition(float x, float y) {
        int tileX = (int) (x / TILE_SIZE) ;
        int tileY = (int) (y / TILE_SIZE) ;

        Integer tileType = mazeLayout.get(new Vector2(tileX, tileY));

        return tileType == null || tileType != 0 && tileType != 9 && tileType != 10;
    }

    public boolean isPathAtPositionforEnemy(float x, float y) {
        int tileX = (int) (x / TILE_SIZE) ;
        int tileY = (int) (y / TILE_SIZE) ;

        Integer tileType = mazeLayout.get(new Vector2(tileX, tileY));

        return tileType == null || tileType != 0 && tileType != 1 && tileType != 2 && tileType != 9;
    }

    /**
     * Renders the maze on the screen.
     * Draws a background for the maze.
     * Renders specific maze elements based on maze layout.
     * @param batch
     */

    public void render(SpriteBatch batch) {

        for (int x = 0; x < mazeWidth; x++) {
            for (int y = 0; y < mazeHeight; y++) {
                batch.draw(backgroundTexture, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }


        for (Map.Entry<Vector2, Integer> entry : mazeLayout.entrySet()) {
            Vector2 position = entry.getKey();
            int type = entry.getValue();

            TextureRegion texture = getTextureForType(type);
            if (texture != null) {
                batch.draw(texture, position.x * TILE_SIZE, position.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    /**
     * A switch case for defining which integer represents which game element.
     * These are used in the properties file beside the coordinates.
     * @param type
     * @return the respective textures.
     */

    private TextureRegion getTextureForType(int type) {
        switch (type) {
            case 0: return wallTexture;
            case 1: return pathTexture;
            case 2: return exitTexture;
            case 3: return trapTexture;
            case 4: return enemyTexture;
            case 5: return keyTexture;
            case 6: return livesTexture;
            case 7: return powerUpTexture;
            case 8: return ObstacleTexture;
            case 9: return blockageTexture;
            case 10: return hiddentexture;
            default: return null;
        }
    }

    /**
     * The getters below detect the game elements.
     * They are used in the GameScreen class to detect collision and perform a task.
     */

    public List<Rectangle> getTraps() {
        List<Rectangle> traps = new ArrayList<>();
        for (Map.Entry<Vector2, Integer> entry : mazeLayout.entrySet()) {
            if (entry.getValue() == 3 || entry.getValue() == 4) {
                Vector2 position = entry.getKey();
                traps.add(new Rectangle(position.x * TILE_SIZE, position.y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
            }
        }
        return traps;
    }

    public List<Rectangle> getObstacle() {
        List<Rectangle> obstacles = new ArrayList<>();
        for (Map.Entry<Vector2, Integer> entry : mazeLayout.entrySet()) {
            if (entry.getValue() == 8) {
                Vector2 position = entry.getKey();
                obstacles.add(new Rectangle(position.x * TILE_SIZE, position.y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
            }
        }
        return obstacles;
    }

    public List<Rectangle> getPowerUps() {
        List<Rectangle> traps = new ArrayList<>();
        for (Map.Entry<Vector2, Integer> entry : mazeLayout.entrySet()) {
            if (entry.getValue() == 7) {
                Vector2 position = entry.getKey();
                traps.add(new Rectangle(position.x * TILE_SIZE, position.y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
            }
        }
        return traps;
    }

    public List<Rectangle> getKeys() {
        List<Rectangle> keys = new ArrayList<>();
        for (Map.Entry<Vector2, Integer> entry : mazeLayout.entrySet()) {
            if (entry.getValue() == 5) {
                Vector2 position = entry.getKey();
                keys.add(new Rectangle(position.x * TILE_SIZE, position.y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
            }
        }
        return keys;
    }

    public List<Rectangle> getExit() {
        List<Rectangle> Exit = new ArrayList<>();
        for (Map.Entry<Vector2, Integer> entry : mazeLayout.entrySet()) {
            if (entry.getValue() == 2) {
                Vector2 position = entry.getKey();
                Exit.add(new Rectangle(position.x * TILE_SIZE, position.y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
            }
        }
        return Exit;
    }

    public List<Rectangle> getLives() {
        List<Rectangle> lives = new ArrayList<>();
        for (Map.Entry<Vector2, Integer> entry : mazeLayout.entrySet()) {
            if (entry.getValue() == 6) {
                Vector2 position = entry.getKey();
                lives.add(new Rectangle(position.x * TILE_SIZE, position.y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
            }
        }
        return lives;
    }

    public List<Rectangle> getBlock() {
        List<Rectangle> blocks = new ArrayList<>();
        for (Map.Entry<Vector2, Integer> entry : mazeLayout.entrySet()) {
            if (entry.getValue() == 9) {
                Vector2 position = entry.getKey();
                blocks.add(new Rectangle(position.x * TILE_SIZE, position.y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
            }
        }
        return blocks;
    }

    public Map<Vector2, Integer> getMap() {
        return mazeLayout;
    }

    /**
     * The methods below remove the respective game objects.
     * These are also used in the GameScreen class.
     */

    public void removeKey(Rectangle key) {
        Vector2 keyPosition = new Vector2(key.x / TILE_SIZE, key.y / TILE_SIZE);
        mazeLayout.remove(keyPosition);
    }

    public void removeLife(Rectangle life) {
        Vector2 lifePosition = new Vector2(life.x / TILE_SIZE, life.y / TILE_SIZE);
        mazeLayout.remove(lifePosition);
    }
    public void removePowerUp(Rectangle powerUp) {
        Vector2 Powerupposition = new Vector2(powerUp.x / TILE_SIZE, powerUp.y / TILE_SIZE);
        mazeLayout.remove(Powerupposition);
    }
    public void removeTrap(Rectangle trap) {
        Vector2 trapposition = new Vector2(trap.x / TILE_SIZE, trap.y / TILE_SIZE);
        mazeLayout.remove(trapposition);
    }
    public void removeBlockage(Rectangle block) {
        Vector2 blockPosition = new Vector2(block.x / TILE_SIZE, block.y / TILE_SIZE);
        mazeLayout.remove(blockPosition);
    }

    /**
     * ensures the cleanup of graphical resources.
     */
    public void dispose() {
        basictilesTexture.dispose();
        thingsTexture.dispose();
    }
}
