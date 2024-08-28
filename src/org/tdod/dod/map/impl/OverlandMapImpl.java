package org.tdod.dod.map.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import org.tdod.dod.map.OverlandMap;
import org.tdod.dod.utils.Constants;
import org.tdod.dod.utils.Utilities;

public class OverlandMapImpl implements OverlandMap {

    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static final int MIN_X_SIZE = 0;
    public static final int MIN_Y_SIZE = 0;
    public static final int MAX_X_SIZE = 100;
    public static final int MAX_Y_SIZE = 100;
    
    private static final String DEFAULT_TILE_KEYWORD = "DefaultTile";
    
    private Map<String, Tile> area = new HashMap<String, Tile>(); 
    private int defaultTile = 4;
    
    public OverlandMapImpl() {
    }

    @Override
    /**
     * 0 = x
     * 1 = y
     * 2 = sprite number
     * 3 = isImpassable(0,1)
     * 4 = isNpcImpassable(0,1)
     * 5 = door(sprite number or -1)
     * 6 = teleport map (null for none)
     * 7 = teleport x
     * 8 = teleport y
     */
    public void initialize(String filename) {
        filename = getCompleteFilename(filename);
        log.info("Loading " + filename);

        area.clear();
        
        try {
            Scanner scanner = new Scanner(new File(filename));
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(DEFAULT_TILE_KEYWORD)) {
                    String[] split = line.split("=");
                    defaultTile = Integer.parseInt(split[1]);
                    // Initialize all the default tiles first
                    for (int y = MIN_Y_SIZE; y < MAX_Y_SIZE; y++) {
                        for (int x = MIN_X_SIZE; x < MAX_X_SIZE; x++) {
                            Tile tile = new Tile();
                            tile.setSpriteValue(defaultTile);
                            area.put(getStringCoord(x,y), tile);
                        }
                    }
                    
                } else {
                    // Initialize empty tiles incase the default param wasn't specified.
                    initializeEmptyTiles();
                    String[] split = line.split(",");
                    Tile tile = new Tile();
                    int x = Integer.parseInt(split[0]);
                    int y = Integer.parseInt(split[1]);
                    tile.setSpriteValue(Integer.parseInt(split[2]));
                    tile.setImpassable(Utilities.booleanFromString((split[3])));
                    tile.setNpcImpassable(Utilities.booleanFromString((split[4])));
                    
                    tile.setOpenDoorValue(Integer.parseInt(split[5]));
                    String teleportMap = split[6];
                    if (!Teleport.NULL_KEYWORD.equals(teleportMap)) {
                        Teleport t = new Teleport();
                        t.setTeleportMap(teleportMap);
                        t.setTeleportX(Integer.parseInt(split[7]));
                        t.setTeleportY(Integer.parseInt(split[8]));
                        tile.setTeleport(t);
                    }
                    area.put(getStringCoord(x,y), tile);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        // Initialize empty tiles for an empty file.
        initializeEmptyTiles();
    }
    
    @Override
    public Tile getMapTile(int x, int y) {
        return area.get(getStringCoord(x,y));
    }

    @Override
    public void setMapTile(int x, int y, Tile tile) {
        area.put(getStringCoord(x,y), tile);
    }

    @Override
    public void save(String filename) {
        String completeFilename = getCompleteFilename(filename);
        log.info("Saving " + completeFilename);

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(completeFilename));
            writer.write(DEFAULT_TILE_KEYWORD + "=" + defaultTile + "\n");
            for (Map.Entry<String, Tile> entry : area.entrySet()) {
                String key = entry.getKey();
                Tile tile = entry.getValue();
                if (tile.getSpriteValue() != defaultTile || tile.hasPropertiesSet()) {
                    String[] coords = key.split(",");
                    String x = coords[0];
                    String y = coords[1];
                    int spriteValue = tile.getSpriteValue();
                    int isImpassable = tile.isImpassable() ? 1 : 0;
                    int isNpcImpassable = tile.isNpcImpassable() ? 1 : 0;
                    int door = tile.getOpenDoorValue();
                    String teleportMap;
                    int teleportX;
                    int teleportY;
                    if (null != tile.getTeleport()) {
                        teleportMap = tile.getTeleport().getTeleportMap();
                        teleportX = tile.getTeleport().getTeleportX();
                        teleportY = tile.getTeleport().getTeleportY();
                    } else {
                        teleportMap = Teleport.NULL_KEYWORD;
                        teleportX = -1;
                        teleportY = -1;      
                    }
                    
                    writer.write(x + "," + y + "," + spriteValue + "," + isImpassable + "," + isNpcImpassable + "," + door + "," + teleportMap + "," + teleportX + "," + teleportY + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    
    @Override
    public int getDefaultTile() {
        return defaultTile;
    }
    
    @Override
    public void setDefaultTile(int newDefaultTile) {
        for (int y = MIN_Y_SIZE; y < MAX_Y_SIZE; y++) {
            for (int x = MIN_X_SIZE; x < MAX_X_SIZE; x++) {
                Tile oldTile = area.get(getStringCoord(x,y));
                if (oldTile.getSpriteValue() == defaultTile) {
                    Tile newTile = new Tile();
                    newTile.setSpriteValue(newDefaultTile);
                    if (oldTile.hasPropertiesSet()) {
                        newTile.copyProperties(oldTile);
                    }
                    String coord = getStringCoord(x,y);
                    area.remove(coord);
                    area.put(coord, newTile);                    
                }
            }
        }

        this.defaultTile = newDefaultTile;

    }
    
    private String getStringCoord(int x, int y) {
        return x +"," + y;
    }
    
    private String getCompleteFilename(String filename) {
        return Constants.MAP_DIRECTORY + filename + Constants.MAP_EXTENSION;
    }
    
    private void initializeEmptyTiles() {
        if (area.size() == 0) {
            for (int y = MIN_Y_SIZE; y < MAX_Y_SIZE; y++) {
                for (int x = MIN_X_SIZE; x < MAX_X_SIZE; x++) {
                    Tile tile = new Tile();
                    tile.setSpriteValue(defaultTile);
                    area.put(getStringCoord(x,y), tile);
                }
            }            
        }

    }
}
