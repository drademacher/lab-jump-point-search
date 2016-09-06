package map.shortestpath;

import util.Vector;

/**
 * Created by paloka on 01.08.16.
 */
class BoundingBox {
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    protected BoundingBox(Vector vector){
        this.minX   = vector.getX();
        this.maxX   = vector.getX();
        this.minY   = vector.getY();
        this.maxY   = vector.getY();
    }

    protected BoundingBox(BoundingBox bb){
        this.minX   = bb.getMinX();
        this.maxX   = bb.getMaxX();
        this.minY   = bb.getMinY();
        this.maxY   = bb.getMaxY();
    }

    protected void add(Vector vector){
        this.minX   = Math.min(vector.getX(),this.getMinX());
        this.maxX   = Math.max(vector.getX(),this.getMaxX());
        this.minY   = Math.min(vector.getY(),this.getMinY());
        this.maxY   = Math.max(vector.getY(),this.getMaxY());
    }

    protected void union(BoundingBox bb){
        this.minX   = Math.min(bb.getMinX(),this.getMinX());
        this.maxX   = Math.max(bb.getMaxX(),this.getMaxX());
        this.minY   = Math.min(bb.getMinY(),this.getMinY());
        this.maxY   = Math.max(bb.getMaxY(),this.getMaxY());
    }

    boolean isInBoundingBox(Vector testPoint){
        return  this.minX<=testPoint.getX() && testPoint.getX()<=this.maxX &&
                this.minY<=testPoint.getY() && testPoint.getY()<=this.maxY;
    }

    int getMaxX() {
        return maxX;
    }

    int getMaxY() {
        return maxY;
    }

    int getMinX() {
        return minX;
    }

    int getMinY() {
        return minY;
    }

    @Override
    public String toString(){
        return "("+minX+"-"+maxX+","+minY+"-"+maxY+")";
    }
}
