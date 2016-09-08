package core.util;

/**
 * BoundingBox represents an axes parallel rectangle in the 2 dimension space around all vectors, which ever where included to it.
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
public class BoundingBox {
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    /**
     * Init a Bounding Box with the first point given as input.
     *
     * @param vector first vector in the new bounding box.
     * @since 1.0
     */
    public BoundingBox(Vector vector){
        this.minX   = vector.getX();
        this.maxX   = vector.getX();
        this.minY   = vector.getY();
        this.maxY   = vector.getY();
    }

    /**
     * Copies another bounding box.
     *
     * @param bb bounding box which should be copied.
     * @since 1.0
     */
    public BoundingBox(BoundingBox bb){
        this.minX   = bb.getMinX();
        this.maxX   = bb.getMaxX();
        this.minY   = bb.getMinY();
        this.maxY   = bb.getMaxY();
    }

    /**
     * Includes the given vector to this bounding box
     *
     * @param vector given vector to include to the bounding box.
     * @since 1.0
     */
    public void add(Vector vector){
        this.minX   = Math.min(vector.getX(),this.getMinX());
        this.maxX   = Math.max(vector.getX(),this.getMaxX());
        this.minY   = Math.min(vector.getY(),this.getMinY());
        this.maxY   = Math.max(vector.getY(),this.getMaxY());
    }

    /**
     * Includes all points of the give bounding box to this one.
     *
     * @param bb bounding box to union to this bounding box.
     * @since 1.0
     */
    public void union(BoundingBox bb){
        this.minX   = Math.min(bb.getMinX(),this.getMinX());
        this.maxX   = Math.max(bb.getMaxX(),this.getMaxX());
        this.minY   = Math.min(bb.getMinY(),this.getMinY());
        this.maxY   = Math.max(bb.getMaxY(),this.getMaxY());
    }

    /**
     * Tests, if a given vector is inside the bounding box.
     *
     * @param testPoint point, which should be tested
     * @return true, if testPoint lies in the bounding box, else false
     * @since 1.0
     */
    public boolean isInBoundingBox(Vector testPoint){
        return  this.minX<=testPoint.getX() && testPoint.getX()<=this.maxX &&
                this.minY<=testPoint.getY() && testPoint.getY()<=this.maxY;
    }

    /**
     * Returns the upper bound of the bonding box in x direction.
     *
     * @return maxX
     * @since 1.0
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * Returns the upper bound of the bonding box in y direction.
     *
     * @return maxY
     * @since 1.0
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * Returns the lower bound of the bonding box in x direction.
     *
     * @return minX
     * @since 1.0
     */
    public int getMinX() {
        return minX;
    }

    /**
     * Returns the lower bound of the bonding box in y direction.
     *
     * @return minY
     * @since 1.0
     */
    public int getMinY() {
        return minY;
    }

    @Override
    public String toString(){
        return "("+minX+"-"+maxX+","+minY+"-"+maxY+")";
    }
}