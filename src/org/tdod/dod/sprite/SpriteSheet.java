package org.tdod.dod.sprite;

import java.util.List;

import org.tdod.dod.sprite.impl.Sprite;

public interface SpriteSheet {

    public static final int ADMIN_TILE_INDEX = 512;
    public static final int PLAYER_TILE_INDEX = 332;
    public static final int X_SPRITE_SIZE = 32;
    public static final int Y_SPRITE_SIZE = 32;
    
    /**
     * Initializes the SpriteSheet.
     */
    public void initialize();

    /**
     * This returns a "random" sprite in the animated sprite sequence.
     * I.E., if the series is 1,2,3,4 and 1 is passed in, 1,2,3 or 4 will return.
     * The first in the series should always be passed in.
     * 
     * @param index The index of the sprite.  If the sprite is animated, the first in the series should be returned.
     * @return A sprite.
     */
    public Sprite getSprite(int index);

    /**
     * This will return an actual sprite, without leveraging the animation data.
     * 
     * @param index The index of the sprite.
     * @return A sprite.
     */
    public Sprite getActualSprite(int index);

    /**
     * The editor has an interface to select which sprites to draw on the screen.
     * Each row has a number of sprites.  This will return all the sprite numbers in that
     * particular row.
     * 
     * @param index The row index.
     * @return A list of sprite numbers in that row.
     */
    public List<Integer> getBuilderList(int index);

    /**
     * Returns the number of rows in the builder list.
     * 
     * @return the number of rows in the builder list.
     */
    public int getBuilderListRows();
    
    /**
     * This engine has a larger number of sprites defined than what can be displayed due to
     * animated sprites.  This call returns the total number of displayable sprites, taking
     * into account the animated sprites.
     * 
     * For example, if there are two sprites, the first one has one frame and the second one
     * has six frames, this call will return two.
     * 
     * @return the total number of displayable sprites.
     */
    public int getDisplayableSpriteCount();


}
