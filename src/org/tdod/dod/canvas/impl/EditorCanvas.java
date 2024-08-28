package org.tdod.dod.canvas.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.tdod.dod.EditableFieldEnum;
import org.tdod.dod.GameContext;
import org.tdod.dod.canvas.AbstractGameCanvas;
import org.tdod.dod.gui.GameUIComponents;
import org.tdod.dod.gui.UITextEditor;
import org.tdod.dod.map.OverlandMap;
import org.tdod.dod.map.impl.OverlandMapImpl;
import org.tdod.dod.map.impl.Teleport;
import org.tdod.dod.map.impl.Tile;
import org.tdod.dod.player.AdminPlayer;
import org.tdod.dod.sprite.SpriteSheet;
import org.tdod.dod.sprite.impl.Sprite;
import org.tdod.dod.utils.Constants;
import org.tdod.dod.utils.Utilities;

public class EditorCanvas extends AbstractGameCanvas {

    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final long serialVersionUID = 1225279943148258754L;

    public static final int SPRITE_INTERFACE_COLUMNS = 16;
    
    private static final int SPRITE_INTERFACE_ROWS = 2;    
    private static final Rectangle PROPERTY_RECTANGLE = new Rectangle(5, 5, 206, 500);
    private static final Rectangle IMPASSABLE_CHECKBOX_AREA = new Rectangle(PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 101, GameUIComponents.getComponent(GameUIComponents.UNCHECKED_CHECKBOX).getWidth(), GameUIComponents.getComponent(GameUIComponents.UNCHECKED_CHECKBOX).getHeight());
    private static final Rectangle NPC_IMPASSABLE_CHECKBOX_AREA = new Rectangle(PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 116, GameUIComponents.getComponent(GameUIComponents.UNCHECKED_CHECKBOX).getWidth(), GameUIComponents.getComponent(GameUIComponents.UNCHECKED_CHECKBOX).getHeight());
    private static final Rectangle DEFAULT_TILE_AREA_BUTTON = new Rectangle((int)(PROPERTY_RECTANGLE.x + SpriteSheet.X_SPRITE_SIZE - 5), PROPERTY_RECTANGLE.y + 23, SpriteSheet.X_SPRITE_SIZE, SpriteSheet.Y_SPRITE_SIZE);
    private static final Rectangle DEFAULT_TILE_AREA_LEFT = new Rectangle(DEFAULT_TILE_AREA_BUTTON.x - 20, PROPERTY_RECTANGLE.y + 24, GameUIComponents.getComponent(GameUIComponents.LEFT_ARROW).getWidth(), GameUIComponents.getComponent(GameUIComponents.LEFT_ARROW).getHeight());
    private static final Rectangle DEFAULT_TILE_AREA_RIGHT = new Rectangle(DEFAULT_TILE_AREA_BUTTON.x + SpriteSheet.X_SPRITE_SIZE + 4, PROPERTY_RECTANGLE.y + 24, GameUIComponents.getComponent(GameUIComponents.RIGHT_ARROW).getWidth(), GameUIComponents.getComponent(GameUIComponents.RIGHT_ARROW).getHeight());;
    private static final Rectangle DOOR_BUTTON_AREA = new Rectangle(PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 131, GameUIComponents.getComponent(GameUIComponents.UNCHECKED_CHECKBOX).getWidth(), GameUIComponents.getComponent(GameUIComponents.UNCHECKED_CHECKBOX).getHeight());
    private static final Rectangle TELEPORT_CHECKBOX_AREA = new Rectangle(PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 146, GameUIComponents.getComponent(GameUIComponents.UNCHECKED_CHECKBOX).getWidth(), GameUIComponents.getComponent(GameUIComponents.UNCHECKED_CHECKBOX).getHeight());
    private static final Rectangle TELEPORT_COORD_BUTTON_AREA = new Rectangle(PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 161, GameUIComponents.getComponent(GameUIComponents.SMALL_EDIT_BUTTON).getWidth(), GameUIComponents.getComponent(GameUIComponents.SMALL_EDIT_BUTTON).getHeight());
    private static final Rectangle TELEPORT_MAP_BUTTON_AREA = new Rectangle(PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 176, GameUIComponents.getComponent(GameUIComponents.SMALL_EDIT_BUTTON).getWidth(), GameUIComponents.getComponent(GameUIComponents.SMALL_EDIT_BUTTON).getHeight());

    private static final int PLAYER_X = Constants.X_RESOLUTION / 2 - SpriteSheet.X_SPRITE_SIZE;
    private static final int PLAYER_Y = Constants.Y_RESOLUTION / 2 - SpriteSheet.Y_SPRITE_SIZE;
    
    private OverlandMap overlandMap = new OverlandMapImpl();

    private AdminPlayer player;

    private Boolean spriteSelectionLock = Boolean.FALSE;
    private Boolean propertyRectangleLock = Boolean.FALSE;
    
    private Rectangle propertyRectangle = null;
    private Rectangle interfaceRectangle = null;
    
    private Map<Point, Rectangle> mapGrid = new HashMap<Point, Rectangle>();
    
    private int displayedDefaultTileIndex;

    private UITextEditor textEditor = new UITextEditor();

    private GameContext context;
    
    public EditorCanvas(GameContext context) {
        this.context = context;
        player = (AdminPlayer)context.getPlayer(); // TODO another way to do this without type casting?
    }

    @Override
    public void initialize() {
        initializeUIComponents();

        initializePlayer();
        
        initializeBufferStrategy();
        
        overlandMap.initialize(player.getMap());
        displayedDefaultTileIndex = overlandMap.getDefaultTile();

        initializeMapGrid();
        
        this.requestFocusInWindow();        
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (textEditor.isEditing()) {
            textEditor.appendText(e.getKeyChar());
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                handleEditableFieldUpdate();
            }
            return;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            // Check boundary.
            int yWindowSize = Constants.Y_RESOLUTION / SpriteSheet.Y_SPRITE_SIZE;
            int yStartIndex = -(yWindowSize / 2);
            int minimumYCoord = player.getY() + yStartIndex;
            if (minimumYCoord <= OverlandMapImpl.MIN_Y_SIZE) {
                log.info("North is Blocked");
                return;
            }
            
            int playerCoord = player.getY() - 1;
            player.setY(playerCoord);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            // Check boundary.
            int xWindowSize = Constants.X_RESOLUTION / SpriteSheet.X_SPRITE_SIZE;
            int xStartIndex = xWindowSize / 2;
            int maximumXCoord = player.getX() + xStartIndex;
            if (maximumXCoord >= (OverlandMapImpl.MAX_X_SIZE - 1)) {
                log.info("East is Blocked");
                return;
            }

            int playerCoord = player.getX() + 1;
            player.setX(playerCoord);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            // Check boundary.
            int yWindowSize = Constants.Y_RESOLUTION / SpriteSheet.Y_SPRITE_SIZE;
            int yStartIndex = yWindowSize / 2;
            int maximumYCoord = player.getY() + yStartIndex;
            if (maximumYCoord >= (OverlandMapImpl.MAX_X_SIZE - 1)) {
                log.info("South is Blocked");
                return;
            }

            int playerCoord = player.getY() + 1;
            player.setY(playerCoord);

        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            // Check boundary.
            int xWindowSize = Constants.X_RESOLUTION / SpriteSheet.X_SPRITE_SIZE;
            int xStartIndex = -(xWindowSize / 2);
            int minimumXCoord = player.getX() + xStartIndex;
            if (minimumXCoord <= OverlandMapImpl.MIN_X_SIZE) {
                log.info("West is Blocked");
                return;
            }

            int playerCoord = player.getX() - 1;
            player.setX(playerCoord);
        }
        if (e.getKeyCode() == KeyEvent.VK_N) {
            synchronized(spriteSelectionLock) {
                int maxPage = context.getGameLibraries().getSpriteSheet().getBuilderListRows() - SPRITE_INTERFACE_ROWS - 1;
                if (player.getInterfacePage() < maxPage) {
                    player.setInterfacePage(player.getInterfacePage() + 1);
                    player.setSelectedInterfaceTile(null);
                }                                
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            synchronized(spriteSelectionLock) {
                if (player.getInterfacePage() > 0) {
                    player.setInterfacePage(player.getInterfacePage() - 1);
                    player.setSelectedInterfaceTile(null);
                }                                
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
        	overlandMap.save(player.getMap());
        }

        if (e.getKeyCode() == KeyEvent.VK_Q) {
            setNextCanvas(new MapSelectionCanvas(context));            
        }
        
        
        updateAll();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (textEditor.isEditing()) {
            return;
        }

        if (e.getButton() == MouseEvent.BUTTON1) {
        	handleLeftMousePressed(e);
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
        	handleRightMousePressed(e);
        }
    }

    @Override
    public void update() {
        updateAll();
    }
    
    @Override
    public void throttledUpdate() {
    }
    
    @Override
    public void addListeners() {
        this.addKeyListener(this);
        this.addMouseListener(this);
    }
    
    @Override
    public void removeListeners() {
        this.removeKeyListener(this);
        this.removeMouseListener(this);
    }

    private void updateAll() {
        synchronized (this) {
            BufferStrategy bufferStrategy = this.getBufferStrategy();
            Graphics graphics = bufferStrategy.getDrawGraphics();

            // Order will determine what graphics is displayed on top.
            drawOverlandMap(graphics);
            selectMapIcon(graphics);
            drawPlayerSprite(graphics);
            drawInterface(graphics);
            selectInterfaceIcon(graphics);
            drawPropertyWindow(graphics);
            drawTextEditor(graphics);
            
            bufferStrategy.show();        
        }

    }
    
    private void initializePlayer() {
        player.setX(50);
        player.setY(50);
    }
    
    private void drawPlayerSprite(Graphics graphics) {
        Sprite sprite = context.getGameLibraries().getSpriteSheet().getSprite(SpriteSheet.ADMIN_TILE_INDEX);
        graphics.drawImage(sprite.getImage(), getPlayerXCoord(), getPlayerYCoord(), null);
    }

    private void drawOverlandMap(Graphics graphics) {
        int xWindowSize = Constants.X_RESOLUTION / SpriteSheet.X_SPRITE_SIZE;
        int yWindowSize = Constants.Y_RESOLUTION / SpriteSheet.Y_SPRITE_SIZE;
        int xStartIndex = -(xWindowSize / 2);
        int yStartIndex = -(yWindowSize / 2);
        
        for (int y = yStartIndex; y <= (yStartIndex + yWindowSize); y++) {
            for (int x = xStartIndex; x <= (xStartIndex + xWindowSize); x++) {
                Tile tile = overlandMap.getMapTile(player.getX() + x, player.getY() + y);
                Sprite sprite = context.getGameLibraries().getSpriteSheet().getSprite(tile.getSpriteValue());
                int xPixelCoord = (x + (xWindowSize / 2)) * SpriteSheet.X_SPRITE_SIZE - SpriteSheet.X_SPRITE_SIZE;
                int yPixelCoord = (y + (yWindowSize / 2)) * SpriteSheet.Y_SPRITE_SIZE - SpriteSheet.Y_SPRITE_SIZE;

                if (xPixelCoord != getPlayerXCoord() || yPixelCoord != getPlayerYCoord()) {
                    graphics.drawImage(sprite.getImage(), xPixelCoord, yPixelCoord, null);                                        
                }
            }
        }
    }

    private void drawInterface(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(5));
        int width = (SpriteSheet.X_SPRITE_SIZE + 2) * 16;
        int x = (Constants.X_RESOLUTION - width) / 2;
        int y = Constants.Y_RESOLUTION - (SpriteSheet.Y_SPRITE_SIZE * 2) - 5;
        if (interfaceRectangle == null) {
            interfaceRectangle = new Rectangle(x - 2, y - 2, width + 2, (SpriteSheet.Y_SPRITE_SIZE * 2) + 6);
        }
        g2.fillRect(interfaceRectangle.x, interfaceRectangle.y, interfaceRectangle.width, interfaceRectangle.height);

        synchronized(spriteSelectionLock) {
            int selectedInterfaceIndex = player.getInterfacePage() * SPRITE_INTERFACE_COLUMNS;
            player.getInterfaceIcons().clear();
            
            for (int row = 0; row < SPRITE_INTERFACE_ROWS; row++) {
                int column = 0;
                List<Integer> spriteList = context.getGameLibraries().getSpriteSheet().getBuilderList(player.getInterfacePage() + row);
                for (Integer spriteIndex : spriteList) {
                    Sprite sprite = context.getGameLibraries().getSpriteSheet().getSprite(spriteIndex);
                    int xPos = x + (column * (SpriteSheet.X_SPRITE_SIZE + 2));
                    int yPos = y + (row * (SpriteSheet.Y_SPRITE_SIZE + 2));
                    graphics.drawImage(sprite.getImage(), xPos, yPos, null);
                    
                    Rectangle rect = new Rectangle(xPos, yPos, SpriteSheet.X_SPRITE_SIZE, SpriteSheet.Y_SPRITE_SIZE);
                    player.getInterfaceIcons().put(Integer.valueOf(sprite.getStartIndex()), rect);

                    if (null == player.getSelectedInterfaceTile()) {
                        player.setSelectedInterfaceTile(spriteList.get(0));
                    }
                    
                    selectedInterfaceIndex++;
                    if (sprite.getSize() > 0) {
                        selectedInterfaceIndex = selectedInterfaceIndex + sprite.getSize() - 1;
                    }
                    if (selectedInterfaceIndex >= context.getGameLibraries().getSpriteSheet().getDisplayableSpriteCount()) {
                        selectedInterfaceIndex = selectedInterfaceIndex - 1;
                    }
                    
                    column++;
                }
            }
        }
    }
    
    private int getPlayerXCoord() {
        return PLAYER_X;
    }

    private int getPlayerYCoord() {
        return PLAYER_Y;
    }
    
    private void selectInterfaceIcon(Graphics graphics) {
        if (null == player.getSelectedInterfaceTile()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(5));
        Rectangle rect = player.getInterfaceIcons().get(player.getSelectedInterfaceTile());
        if (null == rect) {
            log.info("Rectangle not found for " + player.getSelectedInterfaceTile());
            return;
        }
        g2.drawRect((int)rect.getX(), (int)rect.getY(), SpriteSheet.X_SPRITE_SIZE, SpriteSheet.Y_SPRITE_SIZE);            
    }

    private void selectMapIcon(Graphics graphics) {
        Point selectedMapTileCoords = player.getSelectedTileCoords();
        
        if (null == selectedMapTileCoords) {
            return;
        }
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(5));
        
        int offsetXCoord = player.getSelectedTileCoords().x - player.getX();
        int offsetYCoord = player.getSelectedTileCoords().y - player.getY();
        Rectangle r = mapGrid.get(new Point(offsetXCoord, offsetYCoord));
        if (null != r) {
            g2.drawRect(r.x, r.y, SpriteSheet.X_SPRITE_SIZE, SpriteSheet.Y_SPRITE_SIZE);                       
        }
    }

    private void handleLeftMousePressed(MouseEvent e) {
        // Selected the tile interface area.
        if (interfaceRectangle != null && interfaceRectangle.contains(e.getX(), e.getY())) {
            for (Map.Entry<Integer, Rectangle> entry : player.getInterfaceIcons().entrySet()) {
                Integer key = entry.getKey();
                Rectangle value = entry.getValue();
                if (value.contains(e.getX(), e.getY())) {
                    player.setSelectedInterfaceTile(key);
                    log.info("Selected tile #" + player.getSelectedInterfaceTile());
                    return;
                }
            }
        }
        
        // Selected the property area.
        if (propertyRectangle != null && propertyRectangle.contains(e.getX(), e.getY())) {
            if (IMPASSABLE_CHECKBOX_AREA.contains(e.getX(), e.getY())) {
                Tile tile = overlandMap.getMapTile(player.getSelectedTileCoords().x, player.getSelectedTileCoords().y);
                tile.setImpassable(!tile.isImpassable());
            } else if (NPC_IMPASSABLE_CHECKBOX_AREA.contains(e.getX(), e.getY())) {
                Tile tile = overlandMap.getMapTile(player.getSelectedTileCoords().x, player.getSelectedTileCoords().y);
                tile.setNpcImpassable(!tile.isNpcImpassable());
            } else if (DOOR_BUTTON_AREA.contains(e.getX(), e.getY())) {
                Tile tile = overlandMap.getMapTile(player.getSelectedTileCoords().x, player.getSelectedTileCoords().y);
                if (tile.isDoor()) {
                    tile.removeDoor();
                } else {
                    tile.setDefaultDoor();
                    textEditor.setEditing(true, 3, new Rectangle(DOOR_BUTTON_AREA.x + 90,DOOR_BUTTON_AREA.y + 14, 35, 14), Color.BLACK, EditableFieldEnum.DOOR, Utilities.NUMBER_REGEX);
                }
            } else if (TELEPORT_CHECKBOX_AREA.contains(e.getX(), e.getY())) {
                Tile tile = overlandMap.getMapTile(player.getSelectedTileCoords().x, player.getSelectedTileCoords().y);
                if (tile.isTeleporter()) {
                    tile.removeTeleporter();
                } else {
                    tile.setDefaultTeleporter();
                }                
            } else if (DEFAULT_TILE_AREA_BUTTON.contains(e.getX(), e.getY())) {
                overlandMap.setDefaultTile(displayedDefaultTileIndex);
            } else if (DEFAULT_TILE_AREA_LEFT.contains(e.getX(), e.getY())) {
                if (displayedDefaultTileIndex > 0) {
                    displayedDefaultTileIndex--;
                    Sprite sprite = context.getGameLibraries().getSpriteSheet().getActualSprite(displayedDefaultTileIndex);
                    if (sprite.getSize() > 0) {
                        displayedDefaultTileIndex = displayedDefaultTileIndex - sprite.getSize() + 1;
                    }                    
                }
            } else if (DEFAULT_TILE_AREA_RIGHT.contains(e.getX(), e.getY())) {
                    Sprite sprite = context.getGameLibraries().getSpriteSheet().getActualSprite(displayedDefaultTileIndex);
                    int originalIndex = displayedDefaultTileIndex;
                    displayedDefaultTileIndex++;
                    if (sprite.getSize() > 0) {
                        displayedDefaultTileIndex = displayedDefaultTileIndex + sprite.getSize() - 1;
                    }
                    if (null == context.getGameLibraries().getSpriteSheet().getActualSprite(displayedDefaultTileIndex)) {
                        displayedDefaultTileIndex = originalIndex;
                    }
            } else if (TELEPORT_COORD_BUTTON_AREA.contains(e.getX(), e.getY())) {
                Tile tile = overlandMap.getMapTile(player.getSelectedTileCoords().x, player.getSelectedTileCoords().y);
                String teleportCoordText = "N/A";
                if (null != tile.getTeleport()) {
                    teleportCoordText = tile.getTeleport().getTeleportX() + "," + tile.getTeleport().getTeleportY();
                }
                textEditor.setText(teleportCoordText);
                textEditor.setEditing(true, 7, new Rectangle(TELEPORT_COORD_BUTTON_AREA.x + 100,TELEPORT_COORD_BUTTON_AREA.y + 13, 73, 14), Color.BLACK, EditableFieldEnum.TELEPORT_COORD, Utilities.NUMBER_COMMA_REGEX);
            } else if (TELEPORT_MAP_BUTTON_AREA.contains(e.getX(), e.getY())) {
                Tile tile = overlandMap.getMapTile(player.getSelectedTileCoords().x, player.getSelectedTileCoords().y);
                String teleportMapText = "N/A";
                if (null != tile.getTeleport()) {
                    teleportMapText = tile.getTeleport().getTeleportMap();
                }
                textEditor.setText(teleportMapText);
                textEditor.setEditing(true, Constants.MAX_FILENAME_SIZE, new Rectangle(TELEPORT_MAP_BUTTON_AREA.x + 78,TELEPORT_MAP_BUTTON_AREA.y + 13, 124, 14), Color.BLACK, EditableFieldEnum.TELEPORT_MAP, Utilities.ALPHANUMERIC_REGEX);
            }
            
            return;
        }
        
        // Place the tile on the map.
        Tile tile = new Tile();
        tile.setImpassable(false);
        tile.setSpriteValue(player.getSelectedInterfaceTile().intValue());
        Point coords = getTileCoordinatesFromPixelPoint(e.getX(), e.getY());
        
        overlandMap.setMapTile(coords.x, coords.y, tile);
    }
    
    private void handleRightMousePressed(MouseEvent e) {
        // Dont open the properties dialog if the player clicks on the tile in the interface.
        // Selected the interface area.
        if (interfaceRectangle != null && interfaceRectangle.contains(e.getX(), e.getY())) {
            for (Map.Entry<Integer, Rectangle> entry : player.getInterfaceIcons().entrySet()) {
                Rectangle value = entry.getValue();
                if (value.contains(e.getX(), e.getY())) {
                    return;
                }
            }
            
            return;
        }
        
        // Selected the property area.
        synchronized(propertyRectangleLock) {
            if (propertyRectangle != null && propertyRectangle.contains(e.getX(), e.getY())) {
                log.info("right clicked properties");
                return;
            }

            if (propertyRectangle == null) {
                // Property window was closed.  Open it.
                propertyRectangle = PROPERTY_RECTANGLE;
                player.setSelectedTileCoords(getTileCoordinatesFromPixelPoint(e.getX(), e.getY()));
            } else {
                Point selectedSquare = getTileCoordinatesFromPixelPoint(e.getX(), e.getY());
                if (selectedSquare.getX() == player.getSelectedTileCoords().getX() &&
                    selectedSquare.getY() == player.getSelectedTileCoords().getY()) {
                    // The same tile was right clicked, so close the property window.
                    propertyRectangle = null;
                    player.setSelectedTileCoords(null);                    
                } else {
                    // Property window is already open.  Just move the selection to the new tile.
                    player.setSelectedTileCoords(getTileCoordinatesFromPixelPoint(e.getX(), e.getY()));                    
                }
            }
        }
            
    }

    private void drawPropertyWindow(Graphics graphics) {
        synchronized(propertyRectangleLock) {
            if (propertyRectangle != null) {
                Graphics2D g2 = (Graphics2D) graphics;
                g2.setColor(Color.GRAY);

                // The window itself
                g2.fillRect(PROPERTY_RECTANGLE.x,PROPERTY_RECTANGLE.y,PROPERTY_RECTANGLE.width,PROPERTY_RECTANGLE.height);

                // Initialize the basic font
                g2.setFont(getGameFont());
                g2.setColor(Color.WHITE);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

                g2.setStroke(new BasicStroke(2));

                // The default map tile label
                g2.drawString("Default Map Tile", PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 14);
                
                // The horizontal line
                g2.setColor(Color.BLACK);
                g2.drawLine(PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 17, PROPERTY_RECTANGLE.width - 5, PROPERTY_RECTANGLE.y + 17);
                g2.setColor(Color.WHITE);

                // The default tile icon plus the buttons.
                Sprite defaultSprite = context.getGameLibraries().getSpriteSheet().getSprite(displayedDefaultTileIndex);
                g2.drawImage(defaultSprite.getImage(), DEFAULT_TILE_AREA_BUTTON.x, DEFAULT_TILE_AREA_BUTTON.y, null);
                BufferedImage leftArrow = GameUIComponents.getComponent(GameUIComponents.LEFT_ARROW);
                BufferedImage rightArrow = GameUIComponents.getComponent(GameUIComponents.RIGHT_ARROW);
                g2.drawImage(leftArrow, DEFAULT_TILE_AREA_BUTTON.x - 20, PROPERTY_RECTANGLE.y + 24, null);
                g2.drawImage(rightArrow, DEFAULT_TILE_AREA_BUTTON.x + SpriteSheet.X_SPRITE_SIZE + 4, PROPERTY_RECTANGLE.y + 24, null);
                
                // The selected tile coordinates.
                g2.drawString("Selected: " + player.getSelectedTileCoords().x + ", " + player.getSelectedTileCoords().y, PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 80);
                
                // The tile attributes label.
                g2.drawString("Tile Attributes", PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 95);

                // The horizontal line
                g2.setColor(Color.BLACK);
                g2.drawLine(PROPERTY_RECTANGLE.x + 5, PROPERTY_RECTANGLE.y + 98, PROPERTY_RECTANGLE.width - 5, PROPERTY_RECTANGLE.y + 98);
                g2.setColor(Color.WHITE);

                // The impassable label.
                g2.drawString("Impassable", PROPERTY_RECTANGLE.x + 25, PROPERTY_RECTANGLE.y + 113);
                
                // The impassable checkbox.
                Tile tile = overlandMap.getMapTile(player.getSelectedTileCoords().x, player.getSelectedTileCoords().y);
                BufferedImage impassableCheckbox = GameUIComponents.getCheckbox(tile.isImpassable());
                g2.drawImage(impassableCheckbox, IMPASSABLE_CHECKBOX_AREA.x, IMPASSABLE_CHECKBOX_AREA.y, null);
                
                g2.drawString("NPC Impassable", PROPERTY_RECTANGLE.x + 25, PROPERTY_RECTANGLE.y + 128);
                BufferedImage npcImpassableCheckbox = GameUIComponents.getCheckbox(tile.isNpcImpassable());
                g2.drawImage(npcImpassableCheckbox, NPC_IMPASSABLE_CHECKBOX_AREA.x, NPC_IMPASSABLE_CHECKBOX_AREA.y, null);
                
                // The door label.
                String door = "N/A";
                if (tile.isDoor()) {
                    door = "" + tile.getOpenDoorValue();
                }
                g2.drawString("Door: " + door, PROPERTY_RECTANGLE.x + 25, PROPERTY_RECTANGLE.y + 143);
                BufferedImage doorCheckbox = GameUIComponents.getCheckbox(tile.isDoor());
                g2.drawImage(doorCheckbox, DOOR_BUTTON_AREA.x, DOOR_BUTTON_AREA.y, null);
                
                // The teleport label.
                g2.drawString("Teleport:", PROPERTY_RECTANGLE.x + 25, PROPERTY_RECTANGLE.y + 158);
                BufferedImage teleportCheckbox = GameUIComponents.getCheckbox(tile.isTeleporter());
                g2.drawImage(teleportCheckbox, TELEPORT_CHECKBOX_AREA.x, TELEPORT_CHECKBOX_AREA.y, null);
                
                // The teleport coord label.
                String teleportCoord = "N/A";
                String teleportMap = "N/A";
                if (tile.isTeleporter()) {
                    teleportCoord = tile.getTeleport().getTeleportX() + "," + tile.getTeleport().getTeleportY();
                    teleportMap = tile.getTeleport().getTeleportMap();
                }
                g2.drawString("Coord: " + teleportCoord, PROPERTY_RECTANGLE.x + 25, PROPERTY_RECTANGLE.y + 173);
                BufferedImage teleportEditButton = GameUIComponents.getComponent(GameUIComponents.SMALL_EDIT_BUTTON);
                g2.drawImage(teleportEditButton, TELEPORT_COORD_BUTTON_AREA.x, TELEPORT_COORD_BUTTON_AREA.y, null);
                
                // The map label.
                g2.drawString("Map: " + teleportMap, PROPERTY_RECTANGLE.x + 25, PROPERTY_RECTANGLE.y + 188);
                BufferedImage teleportMapButton = GameUIComponents.getComponent(GameUIComponents.SMALL_EDIT_BUTTON);
                g2.drawImage(teleportMapButton, TELEPORT_MAP_BUTTON_AREA.x, TELEPORT_MAP_BUTTON_AREA.y, null);

            }
        }
    }
    
    private Point getTileCoordinatesFromPixelPoint(int x, int y) {
        int xCoord = x / SpriteSheet.X_SPRITE_SIZE + player.getX() - ((Constants.X_RESOLUTION / SpriteSheet.X_SPRITE_SIZE) / 2 - 1);
        int yCoord = y / SpriteSheet.Y_SPRITE_SIZE + player.getY() - ((Constants.Y_RESOLUTION / SpriteSheet.Y_SPRITE_SIZE) / 2 - 1);;
        return new Point(xCoord, yCoord);        
    }
    
    private void initializeMapGrid() {
        int xWindowSize = Constants.X_RESOLUTION / SpriteSheet.X_SPRITE_SIZE;
        int yWindowSize = Constants.Y_RESOLUTION / SpriteSheet.Y_SPRITE_SIZE;
        int xStartIndex = -(xWindowSize / 2);
        int yStartIndex = -(yWindowSize / 2);
        
        for (int y = yStartIndex; y <= (yStartIndex + yWindowSize); y++) {
            for (int x = xStartIndex; x <= (xStartIndex + xWindowSize); x++) {
                int xPixelCoord = (x + (xWindowSize / 2)) * SpriteSheet.X_SPRITE_SIZE - SpriteSheet.X_SPRITE_SIZE;
                int yPixelCoord = (y + (yWindowSize / 2)) * SpriteSheet.Y_SPRITE_SIZE - SpriteSheet.Y_SPRITE_SIZE;

                mapGrid.put(new Point(x,y), new Rectangle(xPixelCoord, yPixelCoord, SpriteSheet.X_SPRITE_SIZE, SpriteSheet.X_SPRITE_SIZE));
            }
        }

    }
    
    private void drawTextEditor(Graphics graphics) {
        if (textEditor.isEditing()) {
            Graphics2D g2 = (Graphics2D) graphics;
            g2.setColor(textEditor.getBackgroundColor());
            g2.fillRect(textEditor.getLocation().x - 15, textEditor.getLocation().y - 14, textEditor.getLocation().width, textEditor.getLocation().height);
            textEditor.setFont(graphics);
            graphics.drawString(textEditor.getText(), textEditor.getLocation().x - 12, textEditor.getLocation().y - 1);
        }
    }
    
    private void handleEditableFieldUpdate() {
        if (null != textEditor) {
            Tile tile = overlandMap.getMapTile(player.getSelectedTileCoords().x, player.getSelectedTileCoords().y);

            switch (textEditor.getField()) {
            case TELEPORT_COORD: {
                Teleport teleport = tile.getTeleport();
                if (null == teleport) {
                    teleport = new Teleport();
                }
                int teleportX = teleport.getTeleportX();
                int teleportY = teleport.getTeleportY();
                try {
                    if (textEditor.getText() != null && textEditor.getText().matches(Utilities.COORDINATE_REGEX)) {
                        String[] split = textEditor.getText().split(",");
                        teleportX = Integer.parseInt(split[0]);
                        teleportY = Integer.parseInt(split[1]);                        
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                teleport.setTeleportX(teleportX);
                teleport.setTeleportY(teleportY);
                if (null == teleport.getTeleportMap()) {
                    teleport.setTeleportMap(OverlandMap.DEFAULT_MAP);
                }
                tile.setTeleport(teleport);
            }
            break;
            case TELEPORT_MAP: {
                Teleport teleport = tile.getTeleport();
                if (null == teleport) {
                    teleport = new Teleport();
                }
                teleport.setTeleportMap(textEditor.getText());
                if (null == teleport.getTeleportMap()) {
                    teleport.setTeleportMap(OverlandMap.DEFAULT_MAP);
                }
                tile.setTeleport(teleport);
            }
            break;
            case DOOR: {
                if (textEditor.getText() != null && textEditor.getText().matches(Utilities.NUMBER_REGEX)) {
                    tile.setOpenDoorValue(Integer.parseInt(textEditor.getText()));
                }
            }
            break;
            case NONE:
            default:
                break;
            }
            
            textEditor.setText("");
        }
    }
}
