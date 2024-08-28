package org.tdod.dod.player;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;

public interface AdminPlayer extends Player {

    public Integer getSelectedInterfaceTile();
    
    public void setSelectedInterfaceTile(Integer selectedInterfaceTile);

    public int getInterfacePage();
    
    public void setInterfacePage(int interfacePage);
 
    public Map<Integer, Rectangle> getInterfaceIcons();

    public void setInterfaceIcons(Map<Integer, Rectangle> interfaceIcons);

    public Point getSelectedTileCoords();
    
    public void setSelectedTileCoords(Point selectedTileCoords);
}
