package org.theGo.game;

import java.util.*;

/**
 * Class representing a tile on the Go board.
 */
public class GoTile {
    /**
     * Color of the stone on the tile, null if empty.
     */
    private Color stoneColor = null;

    /**
     * Array of neighbors of the tile.
     * 0 - north
     * 1 - south
     * 2 - east
     * 3 - west
     */
    private GoTile[] neighbors = new GoTile[4];

    /**
     * Set of tiles that are breaths of the stone on this tile.
     */
    private final Set<GoTile> breathTiles = new HashSet<>();

    /**
     * Counter for captured stones.
     */
    private final GameCounter counter;

    /**
     * Creates a new tile with given counter.
     *
     * @param counter counter for captured stones
     */
    public GoTile(GameCounter counter) {
        this.counter = counter;
    }

    /**
     * Sets the neighbors for the tile.
     *
     * @param neighbors array of neighbors
     */
    public void setNeighbors(GoTile[] neighbors) {
        this.neighbors = neighbors;
    }

    /**
     * Resets the tile to empty.
     */
    private void resetTile() {
        stoneColor = null;
        breathTiles.clear();
    }

    /**
     * Returns the color of the stone on the tile.
     *
     * @return color of the stone
     */
    public Color getStoneColor() {
        return stoneColor;
    }

    /**
     * Returns the neighbors of the tile matching given color.
     * If color is null, returns all empty neighbors.
     *
     * @param color color of the neighbors
     * @return list of neighbors matching given color
     */
    public List<Integer> getNeighbors(Color color) {
        List<Integer> matchingNeighbors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (neighbors[i] != null && neighbors[i].getStoneColor() == color) {
                matchingNeighbors.add(i);
            }
        }
        return matchingNeighbors;
    }

    /**
     * Adds breaths to the stone on the tile.
     *
     * @param tiles set of tiles to add as breaths
     */
    public void inheritBreath(Set<GoTile> tiles) {
        breathTiles.addAll(tiles);
    }

    /**
     * Adds a breath to the stone on the tile.
     *
     * @param tile tile to add as a breath
     */
    public void inheritBreath(GoTile tile) {
        breathTiles.add(tile);
    }

    /**
     * Makes the stone on the tile loose a breath given as a parameter.
     * It executes recursively on all neighbors of the same color.
     *
     * @param breath  tile to remove as a breath
     * @param already set of tiles that are already checked
     */
    public void looseBreath(GoTile breath, Set<GoTile> already) {
        already.add(this);
        breathTiles.remove(breath);

//      Jeśli używamy checkNeighborsV2() to musi być ten warunek
//      if (stoneColor == null) {
//         return;
//      }

        for (int i = 0; i < 4; i++) {
            GoTile neighbor = neighbors[i];
            if (neighbor != null && neighbor.stoneColor == stoneColor) {
                if (neighbor != breath && !already.contains(neighbor)) {
                    neighbor.looseBreath(breath, already);
                }
            }
        }

        if (breathTiles.isEmpty()) {
            killStone();
        }
    }

    /**
     * Kills the stone on the tile.
     */
    private void killStone() {
        counter.addCapturedStone(stoneColor);

        //oddaje oddech sąsiadom przeciwnego koloru
        for (Integer dir : getNeighbors(stoneColor.opposite())) {
            neighbors[dir].inheritBreath(this);
        }

        resetTile();
    }

    /**
     * Sets the stone with given color on the tile.
     * Checks if the move is legal.
     *
     * @param color color of the stone
     * @return true if the move is legal
     */
    public boolean placeStone(Color color) {
        if (stoneColor != null) {
            return false;
        } else if (getNeighbors(null).isEmpty()) {
            //Jeśli miejsce otoczone jest kamieniami

            for (Integer direction : getNeighbors(color)) {
                if (!neighbors[direction].onlyBreath(this)) {
                    //sprawdza, czy można odziedziczyć oddechy od sąsiadów tego samego koloru
                    setupStone(color);
                    return true;
                }
            }

            for (Integer dir : getNeighbors(color.opposite())) {
                if (neighbors[dir].onlyBreath(this)) {
                    //sprawdza, czy można zabić kamienie przeciwnika
                    setupStone(color);
                    return true;
                }
            }

            //Jeśli nie można odziedziczyć oddechów ani zabić kamieni przeciwnika
            //ruch spowodowałby samobójstwo, więc nie można go wykonać
            return false;
        } else {
            setupStone(color);
            return true;
        }
    }

    /**
     * Sets the stone with given color on the tile, and updates breaths and neighbors.
     * @param color color of the stone
     */
    private void setupStone(Color color) {
        stoneColor = color;
        checkNeighborsV3();
    }


    /**
     * Updates breaths and neighbors.
     */
    @Deprecated
    private void checkNeighborsV2() {
        //zabiera oddechy wszystkim sąsiadom przeciwnego koloru
        for (Integer direction : getNeighbors(stoneColor.opposite())) {
            neighbors[direction].looseBreath(this, Set.of(this));
        }

        //ustawia sobie oddechy od pustych sąsiadów
        for (Integer direction : getNeighbors(null)) {
            this.inheritBreath(neighbors[direction]);
        }

        //zabiera oddechy od sąsiadów tego samego koloru
        List<Integer> sameColorNeighbors = getNeighbors(stoneColor);
        if (!sameColorNeighbors.isEmpty()) {

            for (Integer direction : sameColorNeighbors) {
                this.inheritBreath(neighbors[direction].breathTiles);
            }

            breathTiles.remove(this);

            for (Integer direction : sameColorNeighbors) {
                neighbors[direction].inheritBreath(this.breathTiles);
                neighbors[direction].looseBreath(this, Set.of(this));
            }
        }
    }


    /**
     * Updates breaths and neighbors.
     */
    private void checkNeighborsV3() {
        for (int i = 0; i < 4; i++) {
            GoTile neighbor = neighbors[i];
            if (neighbor != null) {
                if (neighbor.stoneColor == stoneColor.opposite()) {
                    //zabiera oddechy od sąsiadów tego samego koloru
                    neighbor.looseBreath(this, new HashSet<>(Set.of(this)));
                } else if (neighbor.stoneColor == null) {
                    //ustawia sobie oddechy od pustych sąsiadów
                    this.inheritBreath(neighbor);
                }
            }
        }


        if (!getNeighbors(stoneColor).isEmpty()) {
            //zabiera oddechy od sąsiadów tego samego koloru
            for (int i = 0; i < 4; i++) {
                GoTile neighbor = neighbors[i];
                if (neighbor != null) {
                    if (neighbor.stoneColor == stoneColor) {
                        this.inheritBreath(neighbor.breathTiles);
                    }
                }
            }

            // usuwa siebie z oddechów, ponieważ dziedziczenie oddechów od sąsiadów nas tu dodało
//         this.looseBreath(this, this, this);
            breathTiles.remove(this);
            //dodaje oddechy do sąsiadów tego samego koloru
            for (int i = 0; i < 4; i++) {
                GoTile neighbor = neighbors[i];
                if (neighbor != null) {
                    if (neighbor.stoneColor == stoneColor) {
                        neighbor.inheritBreath(this.breathTiles);
                        neighbor.looseBreath(this, new HashSet<>(Set.of(this)));
                    }
                }
            }
        }
    }

    /**
     * Checks if the tile is the only breath of the stone on it.
     *
     * @param tile tile to check
     * @return true if the tile is the only breath of the stone on it
     */
    private boolean onlyBreath(GoTile tile) {
        return breathTiles.contains(tile) && breathTiles.size() == 1;
    }

    /**
     * Counts the territory of given color.
     *
     * @param color     color of the player
     * @param territory set of tiles that are already counted
     * @return true if Set territory is indeed a territory
     */
    public boolean countTerritory(Color color, Set<GoTile> territory) {
        territory.add(this);
        boolean isTerritory = true;
        for (int i = 0; i < 4; i++) {
            if (neighbors[i] != null) {
                if (neighbors[i].getStoneColor() == null && !territory.contains(neighbors[i])) {
                    isTerritory = isTerritory && neighbors[i].countTerritory(color, territory);
                } else if (neighbors[i].getStoneColor() == color.opposite()) {
                    return false;
                }
            }
        }
        return isTerritory;
    }
}
