package core.map.heuristic;

import core.map.MapController;

/**
 * HeuristicStrategy provides the opportunity to memorise a specific Heuristic implementation to give it back whenever needed.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see MapController
 * @see Heuristic
 * @see ZeroHeuristic
 * @see ManhattanHeuristic
 * @see GridHeuristic
 * @see EuclideanHeuristic
 * @since 1.0
 */
public class HeuristicStrategy {

    private Heuristic heuristic;


    /* ------- Heuristic Getter & Setter ------- */

    /**
     * Returns the memorised Heuristic implementation.
     *
     * @return The memorised Heuristic implementation. Returns null if no implementation was set.
     * @see MapController
     * @see Heuristic
     * @since 1.0
     */
    public Heuristic getHeuristic() {
        return this.heuristic;
    }

    /**
     * Memorises the ZeroHeuristic implementation.
     *
     * @see MapController
     * @see ZeroHeuristic
     * @see Heuristic
     * @since 1.0
     */
    public void setZeroHeuristic() {
        this.heuristic = new ZeroHeuristic();
    }

    /**
     * Memorises the ManhattanHeuristic implementation.
     *
     * @see MapController
     * @see ManhattanHeuristic
     * @see Heuristic
     * @since 1.0
     */
    public void setManhattanHeuristic() {
        this.heuristic = new ManhattanHeuristic();
    }

    /**
     * Memorises the GridHeuristic implementation.
     *
     * @see MapController
     * @see GridHeuristic
     * @see Heuristic
     * @since 1.0
     */
    public void setGridHeuristic() {
        this.heuristic = new GridHeuristic();
    }

    /**
     * Memorises the EuclideanHeuristic implementation.
     *
     * @see MapController
     * @see EuclideanHeuristic
     * @see Heuristic
     * @since 1.0
     */
    public void setEuclideanHeuristic() {
        this.heuristic = new EuclideanHeuristic();
    }
}
