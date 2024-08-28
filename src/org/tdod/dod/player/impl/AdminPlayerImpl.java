package org.tdod.dod.player.impl;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tdod.dod.player.AdminPlayer;

public class AdminPlayerImpl extends PlayerImpl implements AdminPlayer {

    private static final long serialVersionUID = -8018271478288300811L;
    private Integer selectedInterfaceTile = null;
    private int interfacePage = 0;
    private Map<Integer, Rectangle> interfaceIcons = new ConcurrentHashMap<Integer, Rectangle>();
    private Point selectedTileCoords = null;
    
    @Override
    public Integer getSelectedInterfaceTile() {
        return selectedInterfaceTile;
    }

    @Override
    public void setSelectedInterfaceTile(Integer selectedInterfaceTile) {
        this.selectedInterfaceTile = selectedInterfaceTile;
    }

    @Override
    public int getInterfacePage() {
        return interfacePage;
    }

    @Override
    public void setInterfacePage(int interfacePage) {
        this.interfacePage = interfacePage;
    }

    @Override
    public Map<Integer, Rectangle> getInterfaceIcons() {
        return interfaceIcons;
    }

    @Override
    public void setInterfaceIcons(Map<Integer, Rectangle> interfaceIcons) {
        this.interfaceIcons = interfaceIcons;
    }

    @Override
    public Point getSelectedTileCoords() {
        return selectedTileCoords;
    }
    
    @Override
    public void setSelectedTileCoords(Point selectedTileCoords) {
        this.selectedTileCoords = selectedTileCoords;
    }

}
