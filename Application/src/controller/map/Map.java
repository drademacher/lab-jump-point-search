package controller.map;

/**
 * Created by paloka on 25.05.16.
 */
public class Map {

    private Field[][]       grid;
    private PathCoordinate  startPoint;
    private Coordinate      goalPoint;
    // TODO: paloka macht isSetStartPoint und isSetGoalPoint methoden

    public Map(int xDim, int yDim){
        //Todo: x und y >0;
        this.grid   = new Field[xDim][yDim];
        for(int x=0;x<xDim;x++){
            for(int y=0;y<yDim;y++){
                grid[x][y]  = new GridPointField(x,y);
            }
        }
    }

    public Field getField(int x, int y) throws NotAFieldException {
        isInGrid(x,y);
        return grid[x][y];
    }

    public void setField(int x, int y) throws NotAFieldException {
        isInGrid(x,y);
        if(grid[x][y].getFieldType()!=FieldType.OBSTACLE_POINT) return;
        grid[x][y] = new GridPointField(x,y);
    }

    public void setObstacle(int x, int y) throws NotAFieldException {
        isInGrid(x,y);
        if(grid[x][y].getFieldType()!=FieldType.GRID_POINT) return;
        grid[x][y] = new ObstaclePointField(x,y);
    }

    public void setStartPoint(int x, int y) throws NotAFieldException {
        isInGrid(x,y);
        if(grid[x][y].getFieldType()!=FieldType.GRID_POINT) return;
        removeStartPoint();
        grid[x][y]  = new StartPointField(x,y);
        startPoint  = grid[x][y];
    }

    public PathCoordinate getStartPoint(){
        return startPoint;
    }

    public void removeStartPoint(){
        if(startPoint==null)    return;
        grid[startPoint.getX()][startPoint.getY()]  = new GridPointField(startPoint.getX(), startPoint.getY());
        startPoint  = null;
    }

    public void setGoalPoint(int x,int y) throws NotAFieldException {
        isInGrid(x,y);
        if(grid[x][y].getFieldType()!=FieldType.GRID_POINT) return;
        removeGoalPoint();
        grid[x][y]  = new GoalPointField(x,y);
        goalPoint  = grid[x][y];
    }

    public Coordinate getGoalPoint(){
        return goalPoint;
    }

    public void removeGoalPoint(){
        if(goalPoint==null)    return;
        grid[goalPoint.getX()][goalPoint.getY()]  = new GridPointField(goalPoint.getX(), goalPoint.getY());
        goalPoint   = null;
    }

    private boolean isInGrid(int x, int y) throws NotAFieldException {
        if(grid==null || x<0 || y<0 || x>=grid.length || y>=grid[0].length) throw new NotAFieldException(x,y);
        return true;
    }
}