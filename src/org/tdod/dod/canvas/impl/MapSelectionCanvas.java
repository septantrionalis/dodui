package org.tdod.dod.canvas.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.tdod.dod.EditableFieldEnum;
import org.tdod.dod.GameContext;
import org.tdod.dod.canvas.AbstractGameCanvas;
import org.tdod.dod.gui.UITextEditor;
import org.tdod.dod.utils.Constants;
import org.tdod.dod.utils.MapSelectionMenuEnum;
import org.tdod.dod.utils.Utilities;

public class MapSelectionCanvas extends AbstractGameCanvas {

    private static final long serialVersionUID = -5630932582284373622L;

    private static final int MAX_FILES_PER_PAGE = 10;
    
    private GameContext context;
    private int page = 0;
    private int selectedItem = 0;
    private MapSelectionMenuEnum selectionState = MapSelectionMenuEnum.NONE;
    private UITextEditor textEditor = new UITextEditor();
    
    private static final Rectangle newMapNameTextArea = new Rectangle(200, 320, 200, 23);
    private static final Rectangle newMapFilenameTextArea = new Rectangle(200, 345, 200, 23);
    private static final Rectangle fileExistsTextArea = new Rectangle(200, 370, 200, 23);
    private static final Rectangle deleteMapTextArea = new Rectangle(250, 370, 200, 23);
    
    private Boolean modifyingListLock = Boolean.FALSE;
    
    List<String> data = new ArrayList<String>();
    
    private String mapName = "";
    private String mapFilename = "";
    
    public MapSelectionCanvas(GameContext context) {
        this.context = context;
    }
    
    @Override
    public void update() {
        updateAll();
    }

    @Override
    public void throttledUpdate() {
    }

    @Override
    public void initialize() {
        initializeUIComponents();
        initializeBufferStrategy();
        initializeData();
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

        if (MapSelectionMenuEnum.DELETE_MAP.equals(selectionState)) {
            if (e.getKeyCode() == KeyEvent.VK_Y) {
                handleDeleteMap();
            }

            selectionState = MapSelectionMenuEnum.NONE;
            
            return;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            page++;
            selectedItem = 0;
            int maxPages = (data.size() - 1) / MAX_FILES_PER_PAGE;
            if (page > maxPages) {
                page = maxPages;
            }
            context.getGameLibraries().getSounds().playSelect2();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {            
            page--;
            selectedItem = 0;
            if (page < 0) {
                page = 0;
            }
            context.getGameLibraries().getSounds().playSelect2();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {            
            selectedItem--;
            if (selectedItem < 0) {
                selectedItem = 0;
            }
            context.getGameLibraries().getSounds().playSelect1();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {            
            selectedItem++;
            if (selectedItem >= MAX_FILES_PER_PAGE) {
                selectedItem = MAX_FILES_PER_PAGE - 1;
            } else if (selectedItem >= data.size() - (page * MAX_FILES_PER_PAGE)) {
                selectedItem = data.size() - (page * MAX_FILES_PER_PAGE) - 1;
            }
            context.getGameLibraries().getSounds().playSelect1();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            int selection = (page * MAX_FILES_PER_PAGE) + selectedItem;
            String str = data.get(selection);
            String[] split = str.split("=");
            context.getPlayer().setMap(split[1]);
            setNextCanvas(new EditorCanvas(context));            
        } else if (e.getKeyCode() == KeyEvent.VK_N) {
            selectionState = MapSelectionMenuEnum.NEW_MAP_NAME;
            textEditor.setText("");
            textEditor.setEditing(true, Constants.MAX_MAPNAME_SIZE, newMapNameTextArea, Color.BLACK, EditableFieldEnum.NEW_MAP_NAME, Utilities.ALPHANUMERIC_UNDERSCORE_REGEX);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            selectionState = MapSelectionMenuEnum.DELETE_MAP;
        }

    }

    private void updateAll() {
        synchronized (this) {
            BufferStrategy bufferStrategy = this.getBufferStrategy();
            Graphics graphics = bufferStrategy.getDrawGraphics();

            drawBackdrop(graphics);

            if (MapSelectionMenuEnum.NONE.equals(selectionState)) {
                drawText(graphics);                                
            } else if (MapSelectionMenuEnum.NEW_MAP_NAME.equals(selectionState) ||
                       MapSelectionMenuEnum.NEW_MAP_FILENAME.equals(selectionState) ||
                       MapSelectionMenuEnum.NEW_MAP_FILENAME_EXISTS.equals(selectionState)) {
                drawCreateNewMapWindow(graphics);                
            } else if (MapSelectionMenuEnum.DELETE_MAP.equals(selectionState)) {
                drawDeleteSelectedMap(graphics);                
            }
            
            drawTextEditor(graphics);
            
            bufferStrategy.show();        
        }
    }

    private void drawBackdrop(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, Constants.X_RESOLUTION, Constants.Y_RESOLUTION);
    }
    
    private void drawText(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        
        g2.setFont(getIntroGameFont());
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        int index = 0 + (MAX_FILES_PER_PAGE * page);
        for (int count = 0; count < MAX_FILES_PER_PAGE; count++) {
            if (index < data.size()) {
                String str = data.get(index);
                String[] split = str.split("=");
                g2.drawString(split[0], 350, 300 + (count * 20));                
            } else {
                break;
            }

            index++;
        }
        
        g2.drawString("-->", 300, 300 + (selectedItem * 20));
    }

    private void drawCreateNewMapWindow(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        
        g2.setFont(getIntroGameFont());
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        if (MapSelectionMenuEnum.NEW_MAP_NAME.equals(selectionState)) {
            g2.drawString("Map Name:", newMapNameTextArea.x, newMapNameTextArea.y);            
        } else if (MapSelectionMenuEnum.NEW_MAP_FILENAME.equals(selectionState)) {
            g2.drawString("Map Name:" + mapName, newMapNameTextArea.x, newMapNameTextArea.y);
            g2.drawString("Map Filename:", newMapFilenameTextArea.x, newMapFilenameTextArea.y);
        } else if (MapSelectionMenuEnum.NEW_MAP_FILENAME_EXISTS.equals(selectionState)) {
            g2.drawString("Map Name:" + mapName, newMapNameTextArea.x, newMapNameTextArea.y);
            g2.drawString("Map Filename:" + mapFilename, newMapFilenameTextArea.x, newMapFilenameTextArea.y);
            g2.setColor(Color.RED);
            g2.drawString("FILE EXISTS!  Try again.", fileExistsTextArea.x, fileExistsTextArea.y);
            g2.setColor(Color.WHITE);

        }
    }

    private void drawDeleteSelectedMap(Graphics graphics) {
        synchronized (modifyingListLock) {
            Graphics2D g2 = (Graphics2D) graphics;
            
            g2.setFont(getIntroGameFont());
            g2.setColor(Color.WHITE);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

            String line = data.get(selectedItem);
            String[] split = line.split("=");
            String filename = split[1] + Constants.MAP_EXTENSION;
            
            g2.drawString("Delete map " + filename + "(Y/N) ?", deleteMapTextArea.x, deleteMapTextArea.y);            
        }
    }
    
    private void handleEditableFieldUpdate() {
        if (MapSelectionMenuEnum.NEW_MAP_NAME.equals(selectionState)) {
            mapName = textEditor.getText();
            if (!mapName.isEmpty()) {
                textEditor.setText("");
                selectionState = MapSelectionMenuEnum.NEW_MAP_FILENAME;
                textEditor.setEditing(true, Constants.MAX_FILENAME_SIZE, newMapFilenameTextArea, Color.BLACK, EditableFieldEnum.NEW_MAP_FILENAME, Utilities.ALPHANUMERIC_REGEX);                
            } else {
                // Abort if map name is empty.
                selectionState = MapSelectionMenuEnum.NONE;
            }
        } else if (MapSelectionMenuEnum.NEW_MAP_FILENAME.equals(selectionState)) {
            String filename = textEditor.getText();
            if (!mapName.isEmpty()) {
                File file = new File(Constants.getFullMapFile(filename));
                mapFilename = textEditor.getText();
                if (file.exists() && !file.isDirectory()) { 
                    selectionState = MapSelectionMenuEnum.NEW_MAP_FILENAME_EXISTS;
                    textEditor.setEditing(true);            

                } else {
                    data.add(mapName + "=" + mapFilename);
                    selectionState = MapSelectionMenuEnum.NONE;
                    createMap();
                }                
            } else {
                // Abort if map filename is empty.
                selectionState = MapSelectionMenuEnum.NONE;
            }
        } else if (MapSelectionMenuEnum.NEW_MAP_FILENAME_EXISTS.equals(selectionState)) {
            textEditor.setEditing(true, Constants.MAX_FILENAME_SIZE, newMapFilenameTextArea, Color.BLACK, EditableFieldEnum.NEW_MAP_FILENAME, Utilities.ALPHANUMERIC_REGEX);            
            selectionState = MapSelectionMenuEnum.NEW_MAP_FILENAME;  
        }
        
    }
    
    private void drawTextEditor(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        textEditor.setFont(graphics);
        if (MapSelectionMenuEnum.NEW_MAP_NAME.equals(selectionState)) {
            g2.setColor(textEditor.getBackgroundColor());
            g2.fillRect(textEditor.getLocation().x + 125, textEditor.getLocation().y - 20, textEditor.getLocation().width, textEditor.getLocation().height);
            textEditor.setFont(graphics);
            graphics.drawString(textEditor.getText(), textEditor.getLocation().x + 123, textEditor.getLocation().y);
        } else if (MapSelectionMenuEnum.NEW_MAP_FILENAME.equals(selectionState)) {
            graphics.drawString(textEditor.getText(), textEditor.getLocation().x + 173, textEditor.getLocation().y);
        }
    }

    private void createMap() {
        try {
            Writer areaIndexFile;
            areaIndexFile = new BufferedWriter(new FileWriter(Constants.getMapIndexFile(), true));
            areaIndexFile.append(mapName + "=" + mapFilename + "\n");
            areaIndexFile.close();
            
            File mapFile = new File(Constants.getFullMapFile(mapFilename));
            mapFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private void initializeData() {
        try {
            Scanner scanner = new Scanner(new File(Constants.MAP_DIRECTORY + Constants.MAP_LIST_FILENAME));
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("#")) {
                    data.add(line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteMap() {
        synchronized (modifyingListLock) {
            String line = data.remove(selectedItem);
            
            String[] split = line.split("=");
            String fileToDelete = Constants.getFullMapFile(split[1]);
            new File(fileToDelete).delete();
            
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(Constants.getMapIndexFile()));
                for (String string : data) {
                    writer.write(string + "\n");                
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

            selectedItem = 0;
        }
        
    }
}
