package ca.vanzeben.game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import ca.vanzeben.game.Physics;
import ca.vanzeben.game.entities.Entity;
import ca.vanzeben.game.entities.Player;
import ca.vanzeben.game.entities.PlayerMP;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.level.tiles.Tile;
import ca.vanzeben.mathlib.Vector2;

public class Level {

    private byte[] tiles;
    public int width;
    public int height;
    private List<Entity> entities = new ArrayList<Entity>();
    private String imagePath;
    private BufferedImage image;

    public Level(String imagePath) {
        if (imagePath != null) {
            this.imagePath = imagePath;
            this.loadLevelFromFile();
        } else {
            this.width = 64;
            this.height = 64;
            tiles = new byte[width * height];
            this.generateLevel();
        }
    }

    private void loadLevelFromFile() {
        try {
            this.image = ImageIO.read(Level.class.getResource(this.imagePath));
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();
            tiles = new byte[width * height];
            this.loadTiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTiles() {
        int[] tileColours = this.image.getRGB(0, 0, width, height, null, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tileCheck: for (Tile t : Tile.tiles) {
                    if (t != null && t.getLevelColour() == tileColours[x + y * width]) {
                        this.tiles[x + y * width] = t.getId();
                        break tileCheck;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void saveLevelToFile() {
        try {
            ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alterTile(int x, int y, Tile newTile) {
        this.tiles[x + y * width] = newTile.getId();
        image.setRGB(x, y, newTile.getLevelColour());
    }

    public void generateLevel() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x * y % 10 < 7) {
                    tiles[x + y * width] = Tile.GRASS.getId();
                } else {
                    tiles[x + y * width] = Tile.STONE.getId();
                }
            }
        }
    }

    public synchronized List<Entity> getEntities() {
        return this.entities;
    }

    public void tick() {
        for (Entity e : getEntities()) {
            e.tick();
        }

        for (Tile t : Tile.tiles) {
            if (t == null) {
                break;
            }
            t.tick();
        }
    }

    public void renderTiles(Screen screen, int xOffset, int yOffset) {
        if (xOffset < 0)
            xOffset = 0;
        if (xOffset > ((width << 3) - screen.width))
            xOffset = ((width << 3) - screen.width);
        if (yOffset < 0)
            yOffset = 0;
        if (yOffset > ((height << 3) - screen.height))
            yOffset = ((height << 3) - screen.height);

        screen.setOffset(xOffset, yOffset);

        for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++) {
            for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
                getTile(x, y).render(screen, this, x << 3, y << 3);
            }
        }
    }

    public void renderEntities(Screen screen) {
        for (Entity e : getEntities()) {
            e.render(screen);
        }
    }

    public Tile getTile(int x, int y) {
        if (0 > x || x >= width || 0 > y || y >= height)
            return Tile.VOID;
        return Tile.tiles[tiles[x + y * width]];
    }

    public synchronized void addEntity(Entity entity) {
        this.getEntities().add(entity);
    }

    public synchronized void removePlayerMP(String username) {
        int index = 0;
        for (Entity e : getEntities()) {
            if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) {
                break;
            }
            index++;
        }
        this.getEntities().remove(index);
    }

    // suppose to be private
    public int getPlayerMPIndex(String username) {
        int index = 0;
        for (Entity e : getEntities()) {
            if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) {
                break;
            }
            index++;
        }
        return index;
    }

    public synchronized void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir) {
        int index = getPlayerMPIndex(username);
        PlayerMP player = (PlayerMP) this.getEntities().get(index);
        player.x = x;
        player.y = y;
        player.setMoving(isMoving);
        player.setNumSteps(numSteps);
        player.setMovingDir(movingDir);
    }

    /*
    Descr: My Test Player Collision
    Links: https://java2blog.com/how-to-iterate-over-map-or-hashmap-in/
    Gen: HashMap not thread safe
    Quest: synch HashMap? [Map map=Collections.synchornizedMap(hashmap)

     */
    public synchronized void testPlayerCollision(String username, int x, int y, int numSteps, boolean isMoving, int movingDir) {
        System.out.println("In Test Player Collision...");
        System.out.println("This App User Name is " + username + " void testPlayerCollision ");
     //   Hashtable<Integer, String> h = new Hashtable<>();
        Hashtable<String, String> hstring = new Hashtable<>();
         Physics physics = new Physics();
     //   h.size();

        for(Entity ourself : getEntities()){
            if(ourself instanceof  PlayerMP) {
                if (((PlayerMP) ourself).getUsername().equals(username))
                    for (Entity otherPerson : getEntities()) {
                    boolean bcol = false;
                    double dist = 0;
                    // As long as it is not ourself.
                        String str_whom = ((PlayerMP) otherPerson).getUsername();
                        // Protects us from colliding with ourself.
                        if( !((PlayerMP) ourself).getUsername().equalsIgnoreCase(str_whom)) {
                            // Do some things.
                            //canPlaySound to false. ( If we are using the server)
                            // physics.setCanPlaySounds(this.canPlaySound);
                            dist = physics.SC(new Vector2(ourself.x, ourself.y) , new Vector2(otherPerson.x, otherPerson.y), 5, 5); // our poss, other poss, radius, radius
                             bcol = physics.hasCollision;
                        }
                        // now based on Collision fix Positions. Hard Fix for now
                        // delta = Beta - Alpha
                        if( bcol) {
                            // if we have col then we (ourself) and the otherPerson) should translate accordingly.
                            // Really should test to see who faceing what.

                            // Cheesy
                            int delta = ourself.x - otherPerson.x;
                            if (delta > 0) {
                                ourself.x += 1;
                                otherPerson.x += 1;
                            } else {
                                ourself.x -= 1;
                                otherPerson.x -= 1;
                            }

                            /*
                            String strOurSelf = (((PlayerMP) ourself).getUsername());
                            String strOtherPerson = (((PlayerMP) otherPerson).getUsername());
                            System.out.println("Entity OurSelf: >> " + strOurSelf + " << Entity otherPerson: >> " + strOtherPerson + " <<]");
                            if( !strOurSelf.equals(username));
                            hstring.put(strOurSelf, strOtherPerson);
                            int index = getPlayerMPIndex(strOurSelf);
                            h.put(index, strOtherPerson); // Ok we had a collision lets just store our user.
                            */
                        }
                }
            }
            //////////////////
            System.out.println("***************Duplicate********************");
            Iterator< Map.Entry<String, String >> entryIterator = hstring.entrySet().iterator();
            while (entryIterator.hasNext())
            {
                Map.Entry<String, String> entrystring = entryIterator.next();
                System.out.println("Key e: " + entrystring.getKey() + " value f: " + entrystring.getValue());
            }
        }

    }

}
