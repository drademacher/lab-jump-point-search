package map.shortestpath;

import util.Coordinate;

/**
 * Created by paloka on 01.08.16.
 */
public class BoundingBox {
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    BoundingBox(int minX, int maxX, int minY, int maxY){
        this.minX   = minX;
        this.maxX   = maxX;
        this.minY   = minY;
        this.maxY   = maxY;
    }

    BoundingBox add(BoundingBox summand){
        this.minX   = Math.min(this.minX,summand.getMinX());
        this.maxX   = Math.max(this.maxX,summand.getMaxX());
        this.minY   = Math.min(this.minY,summand.getMinY());
        this.maxY   = Math.max(this.maxY,summand.getMaxY());
        return this;
    }

    BoundingBox add(Coordinate summand){
        this.minX   = Math.min(this.minX,summand.getX());
        this.maxX   = Math.max(this.maxX,summand.getX());
        this.minY   = Math.min(this.minY,summand.getY());
        this.maxY   = Math.max(this.maxY,summand.getY());
        return this;
    }

    boolean isInBoundingBox(Coordinate testPoint){
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
}
