package grid;


import view.Context;

public class Grid {
    private Context context = Context.getInstance();

    private int n;
    private int m;
    private Type[][] grid;

    public Grid(int n, int m) {
        this.n = n;
        this.m = m;
        grid = new Type[n][m];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                if (x <= 2 && y > 4) {
                    grid[x][y] = Type.OBSTACLE;
                } else {
                    grid[x][y] = Type.NODE;
                }

            }
        }
    }

    public Type getCell(int x, int y) {
        return grid[x][y];
    }

    public void setCell(int x, int y, Type type) {
        grid[x][y] = type;
    }

    public void setEmpty() {
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                grid[x][y] = Type.NODE;
            }
        }
    }

    public void setExample() {
        // TODO: fill in some example map for tool presentation
    }

    public void setRnd(double prob) {
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                if (context.rnd.nextDouble() > prob) {
                    grid[x][y] = Type.NODE;
                } else {
                    grid[x][y] = Type.OBSTACLE;
                }

            }
        }
    }

    public void setGenerate() {
        // TODO: import and adjust grid graph generator from spoon
    }

    public void setLoadFromFile() {
        // TODO: write parser
    }
}
