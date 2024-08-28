package org.tdod.dod.map;

import org.tdod.dod.map.impl.Tile;

public interface OverlandMap {

    public static final String DEFAULT_MAP = "limbo";
    
    public void initialize(String filename);
    
    public Tile getMapTile(int x, int y);

    public void setMapTile(int x, int y, Tile tile);

    public void save(String filename);
    
    public int getDefaultTile();
    
    public void setDefaultTile(int defaultTile);

}
