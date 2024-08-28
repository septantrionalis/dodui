package org.tdod.dod.canvas.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import org.tdod.dod.GameContext;
import org.tdod.dod.canvas.AbstractGameCanvas;
import org.tdod.dod.gui.GameUIComponents;
import org.tdod.dod.map.OverlandMap;
import org.tdod.dod.map.impl.OverlandMapImpl;
import org.tdod.dod.map.impl.Teleport;
import org.tdod.dod.map.impl.Tile;
import org.tdod.dod.player.Player;
import org.tdod.dod.sound.SongEnum;
import org.tdod.dod.sprite.SpriteSheet;
import org.tdod.dod.sprite.impl.Sprite;
import org.tdod.dod.sprite.impl.SpriteSheetImpl;
import org.tdod.dod.utils.CommandPromptEnum;
import org.tdod.dod.utils.Constants;

public class MapCanvas extends AbstractGameCanvas {

    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final long serialVersionUID = 1225279943148258754L;

    private static final int MAX_TEXT_MESSAGES = 6;
    
    /**
     * Needs to be multiples of the sprite size, plus an even number to avoid fractions in the math.
     */
    private static final int TEXT_WINDOW_HEIGHT = (SpriteSheet.Y_SPRITE_SIZE * 4);
    
    private Rectangle border = new Rectangle(0, 0, Constants.X_RESOLUTION, Constants.Y_RESOLUTION-TEXT_WINDOW_HEIGHT);
    
    private static final int PLAYER_X = Constants.X_RESOLUTION / 2 - SpriteSheetImpl.X_SPRITE_SIZE;
    private static final int PLAYER_Y = (Constants.Y_RESOLUTION - TEXT_WINDOW_HEIGHT) / 2 - SpriteSheetImpl.Y_SPRITE_SIZE;
    
    private OverlandMap overlandMap = new OverlandMapImpl();
    private Player player;
    
    private List<String> textMessages = new ArrayList<String>();

    private GameContext context;

    private CommandPromptEnum prompt = CommandPromptEnum.NONE;
    
    private boolean isTeleporting = false;
    
    public MapCanvas(GameContext context) {
        this.context = context;
        this.player = context.getPlayer();
    }

    @Override
    public void initialize() {
        initializeUIComponents();
        
        initializePlayer();
        
        initializeBufferStrategy();
        
        overlandMap.initialize(player.getMap());

        this.requestFocusInWindow();        

        if (context.isPlayMusic()) {
            context.getGameLibraries().startMusic(SongEnum.EXPLORE1);            
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (isTeleporting) {
            return;
        }
        
        // Process commands that require more input.
        if (CommandPromptEnum.OPEN.equals(prompt)) {
            handleOpenDoor(e);
            return;
        }
        
        
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            // Check boundary.
            int yWindowSize = Constants.Y_RESOLUTION / SpriteSheetImpl.Y_SPRITE_SIZE;
            int yStartIndex = -(yWindowSize / 2);
            int minimumYCoord = player.getY() + yStartIndex;
            if (minimumYCoord <= OverlandMapImpl.MIN_Y_SIZE) {
                log.info("North is Blocked");
                return;
            }
            
            if (isBlocked("North" , player.getX(), player.getY() - 1)) {
                return;
            }
            
            int playerCoord = player.getY() - 1;
            player.setY(playerCoord);
            context.getGameLibraries().getSounds().playWalk();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            // Check boundary.
            int xWindowSize = Constants.X_RESOLUTION / SpriteSheetImpl.X_SPRITE_SIZE;
            int xStartIndex = xWindowSize / 2;
            int maximumXCoord = player.getX() + xStartIndex;
            if (maximumXCoord >= (OverlandMapImpl.MAX_X_SIZE - 1)) {
                log.info("East is Blocked");
                return;
            }

            if (isBlocked("East" , player.getX() + 1, player.getY())) {
                return;
            }

            int playerCoord = player.getX() + 1;
            player.setX(playerCoord);
            context.getGameLibraries().getSounds().playWalk();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            // Check boundary.
            int yWindowSize = Constants.Y_RESOLUTION / SpriteSheetImpl.Y_SPRITE_SIZE;
            int yStartIndex = yWindowSize / 2;
            int maximumYCoord = player.getY() + yStartIndex;
            if (maximumYCoord >= (OverlandMapImpl.MAX_X_SIZE - 1)) {
                log.info("South is Blocked");
                return;
            }

            if (isBlocked("South" , player.getX(), player.getY() + 1)) {
                return;
            }

            int playerCoord = player.getY() + 1;
            player.setY(playerCoord);
            context.getGameLibraries().getSounds().playWalk();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            // Check boundary.
            int xWindowSize = Constants.X_RESOLUTION / SpriteSheetImpl.X_SPRITE_SIZE;
            int xStartIndex = -(xWindowSize / 2);
            int minimumXCoord = player.getX() + xStartIndex;
            if (minimumXCoord <= OverlandMapImpl.MIN_X_SIZE) {
                log.info("West is Blocked");
                return;
            }

            if (isBlocked("West" , player.getX() - 1, player.getY())) {
                return;
            }

            int playerCoord = player.getX() - 1;
            player.setX(playerCoord);
            context.getGameLibraries().getSounds().playWalk();
        }
        if (e.getKeyCode() == KeyEvent.VK_O) {
            addTextMessage("Open.  What direction?");
            prompt = CommandPromptEnum.OPEN;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            // TODO
            addTextMessage("Saving game...");
        }

        updateAll();
        
        handleTransport(); // TODO do I want this here?
    }

    @Override
    public void update() {
        updateAll();
    }
    
    @Override
    public void throttledUpdate() {
    }
    
    private void updateAll() {
        synchronized (this) {
            BufferStrategy bufferStrategy = this.getBufferStrategy();
            Graphics graphics = bufferStrategy.getDrawGraphics();
            
            drawOverlandMap(graphics);
            drawPlayerSprite(graphics);
            drawTopBorder(graphics);
            drawTextArea(graphics);
            
            // graphics.dispose();
            bufferStrategy.show();
        }

    }
    
    private void initializePlayer() {
        player.setX(50);
        player.setY(50);
    }
    
    protected void drawSprite(Graphics graphics) {
        // Sprite sprite = sheet.getSprite(0);
    }

    private void drawPlayerSprite(Graphics graphics) {
        Sprite sprite = context.getGameLibraries().getSpriteSheet().getSprite(SpriteSheetImpl.PLAYER_TILE_INDEX);
        graphics.drawImage(sprite.getImage(), getPlayerXCoord(), getPlayerYCoord(), null);
    }

    private void drawOverlandMap(Graphics graphics) {
        int xWindowSize = Constants.X_RESOLUTION / SpriteSheetImpl.X_SPRITE_SIZE;
        int yWindowSize = (Constants.Y_RESOLUTION - TEXT_WINDOW_HEIGHT) / SpriteSheetImpl.Y_SPRITE_SIZE;
        int xStartIndex = -(xWindowSize / 2);
        int yStartIndex = -(yWindowSize / 2);

        for (int y = yStartIndex; y <= (yStartIndex + yWindowSize); y++) {
            for (int x = xStartIndex; x <= (xStartIndex + xWindowSize); x++) {
                Tile tile = overlandMap.getMapTile(player.getX() + x, player.getY() + y);
                
                int spriteValue = tile.getSpriteValue();
                if (tile.isDoor()) {
                    if (!tile.isClosed()) {
                        spriteValue = tile.getOpenDoorValue();
                    }
                }
                Sprite sprite = context.getGameLibraries().getSpriteSheet().getSprite(spriteValue);
                int xPixelCoord = (x + (xWindowSize / 2)) * SpriteSheetImpl.X_SPRITE_SIZE - SpriteSheetImpl.X_SPRITE_SIZE;
                int yPixelCoord = (y + (yWindowSize / 2)) * SpriteSheetImpl.Y_SPRITE_SIZE - SpriteSheetImpl.Y_SPRITE_SIZE;

                if (xPixelCoord != getPlayerXCoord() || yPixelCoord != getPlayerYCoord()) {
                    graphics.drawImage(sprite.getImage(), xPixelCoord, yPixelCoord, null);
                }
            }
        }
    }

    private void drawTopBorder(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        GameUIComponents.drawDialogBox(g2, border.x, border.y, border.width, border.height, false, false);
    }

    private void drawTextArea(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        GameUIComponents.drawDialogBox(g2, 0, Constants.Y_RESOLUTION - TEXT_WINDOW_HEIGHT - 15, Constants.X_RESOLUTION, TEXT_WINDOW_HEIGHT + 15, true, false);
        
        g2.setFont(getGameFont());
        g2.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        int startY = Constants.Y_RESOLUTION - TEXT_WINDOW_HEIGHT + 20;
        for (int index = 0; index < textMessages.size(); index++) {
            g2.drawString(textMessages.get(index), 30, startY);            
            startY = startY + 17;
        }
    }
    
    private int getPlayerXCoord() {
        return PLAYER_X;
    }

    private int getPlayerYCoord() {
        return PLAYER_Y;
    }
    
    private boolean isBlocked(String direction, int xPlayerCoord, int yPlayerCoord) {
        Tile tile = overlandMap.getMapTile(xPlayerCoord, yPlayerCoord);
        if (tile.isImpassable()) {
            context.getGameLibraries().getSounds().playBlocked();
            addTextMessage(direction + " is blocked.");
            return true;
        }        
        
        if (tile.isDoor() && tile.isClosed()) {
            context.getGameLibraries().getSounds().playBlocked();
            addTextMessage(direction + " door is closed.");
            return true;            
        }
        
        return false;
    }
    
    private void addTextMessage(String message) {
        textMessages.add(message);
        if (textMessages.size() > MAX_TEXT_MESSAGES) {
            textMessages.remove(0);
        }
    }
    
    private void handleOpenDoor(KeyEvent e) {
        int doorX = -1;
        int doorY = -1;
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            doorX = player.getX();
            doorY = player.getY() - 1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {            
            doorX = player.getX();
            doorY = player.getY() + 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            doorX = player.getX() - 1;
            doorY = player.getY();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {            
            doorX = player.getX() + 1;
            doorY = player.getY();
        }

        Tile tile = overlandMap.getMapTile(doorX, doorY);
        
        if (null != tile) {
            if (tile.isDoor()) {
                if (tile.isClosed()) {
                    addTextMessage("Opening...");
                    tile.setClosed(false);
                    prompt = CommandPromptEnum.NONE;            
                    return;
                } else {
                    addTextMessage("Closing...");
                    tile.setClosed(true);
                    prompt = CommandPromptEnum.NONE;            
                    return;                    
                }
            }
        }

        addTextMessage("You can't open that.");
        prompt = CommandPromptEnum.NONE;            
    }
    
    private void handleTransport() {
        synchronized (this) {
            Tile tile = overlandMap.getMapTile(player.getX(), player.getY());
            
            Teleport teleport = tile.getTeleport();
            if (null != teleport) {
                isTeleporting = true;
                try {
                    log.info("Teleporting to " + teleport.getTeleportMap() + " at " + teleport.getTeleportX() + "," + teleport.getTeleportY());
                    addTextMessage("Entering " + getMapName(teleport.getTeleportMap()));
                    player.setMap(teleport.getTeleportMap());
                    player.setX(teleport.getTeleportX());
                    player.setY(teleport.getTeleportY());    
                    
                    if (context.isPlayMusic()) {
                        context.getGameLibraries().stopMusic();            
                    }

                    overlandMap.initialize(player.getMap());
                    restartMusic();                    
                } finally {
                    isTeleporting = false;                    
                }
            }            
        }
    }
    
    private String getMapName(String filename) {
        String mapIndexFile = Constants.getMapIndexFile();
        try {
            Scanner scanner = new Scanner(new File(mapIndexFile));
            try {
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains(filename)) {
                        String split[] = line.split("=");
                        return split[0];
                    }
                }                
            } finally {
                scanner.close();                
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private void restartMusic() {
        // TODO play random music here.
        if (context.isPlayMusic()) {
            context.getGameLibraries().startMusic(SongEnum.EXPLORE1);            
        }
    }
}
