package grid;


import view.Global;

public class Map {
    private Global global = Global.getInstance();

    private int n;
    private int m;
    private Type[][] grid;

    public Map(int n, int m) {
        this.n = n;
        this.m = m;
        grid = new Type[n][m];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                    grid[x][y] = Type.FLOOR;
            }
        }
    }

    public Type getCell(int x, int y) {
        if (x < 0 || x >= n || y < 0 || y >= m) {
            return Type.ERROR;
        }

        return grid[x][y];
    }

    public void setCell(int x, int y, Type type) {
        if (x < 0 || x >= n || y < 0 || y >= m) {
            return;
        }

        grid[x][y] = type;
    }

    public void setEmpty() {
        for (int x = 0; x < getN(); x++) {
            for (int y = 0; y < m; y++) {
                grid[x][y] = Type.FLOOR;
            }
        }
    }

    public void setObstacle() {
        for (int x = 0; x < getN(); x++) {
            for (int y = 0; y < m; y++) {
                grid[x][y] = Type.OBSTACLE;
            }
        }
    }

    public void setExample() {
        // TODO: fill in some example map for tool presentation
    }

    public void setRnd(double prob) {
        for (int x = 0; x < getN(); x++) {
            for (int y = 0; y < m; y++) {
                if (global.rnd.nextDouble() > prob) {
                    grid[x][y] = Type.FLOOR;
                } else {
                    grid[x][y] = Type.OBSTACLE;
                }

            }
        }
    }

    public void setGenerate() {
        // TODO: import and adjust map graph generator from spoon
    }

    public void setLoadFromFile() {
        // TODO: write parser
    }

    public int getN() {
        return n;
    }


    public int getM() {
        return m;
    }
}
