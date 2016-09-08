package core.map.movingrule;

import core.map.MapController;

/**
 * MovingRuleStrategy provides the opportunity to memorise a specific MovingRule implementation to give it back whenever needed.
 *
 * @author Patrick Loka
 * @version 1.0
 * @see MapController
 * @see MovingRule
 * @see AllNeighborMovingRule
 * @see UncutNeighborMovingRule
 * @see OrthogonalNeighborMovingRule
 * @since 1.0
 */
public class MovingRuleStrategy {

    private MovingRule movingRule;
    
    
    /* ------- MovingRule Getter & Setter ------- */

    /**
     * Returns the memorised MovingRule implementation.
     *
     * @return The memorised MovingRule implementation. Returns null if no implementation was set.
     * @see MapController
     * @see MovingRule
     * @since 1.0
     */
    public MovingRule getMovingRule(){
        return this.movingRule;
    }

    /**
     * Memorises the AllNeighborMovingRule implementation.
     *
     * @see MapController
     * @see AllNeighborMovingRule
     * @see MovingRule
     * @since 1.0
     */
    public void setAllNeighborMovingRule(){
        this.movingRule = new AllNeighborMovingRule();
    }

    /**
     * Memorises the UncutNeighborMovingRule implementation.
     *
     * @see MapController
     * @see UncutNeighborMovingRule
     * @see MovingRule
     * @since 1.0
     */
    public void setUncutNeighborMovingRule(){
        this.movingRule = new UncutNeighborMovingRule();
    }

    /**
     * Memorises the OrthogonalNeighborMovingRule implementation.
     *
     * @see MapController
     * @see OrthogonalNeighborMovingRule
     * @see MovingRule
     * @since 1.0
     */
    public void setOrthogonalNeighborMovingRule(){
        this.movingRule = new OrthogonalNeighborMovingRule();
    }
}