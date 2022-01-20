package javaFiles;

import java.util.Arrays;
import java.util.Random;

public class Game {
    private static final Random rn = new Random();
    private final int N, M;
    private boolean [][] grid, future;

    public Game(int col, int row) {
        M = col;
        N = row;
        // Initializing the grid.
        randomBooleans();
    }

    // Function to print next generation
    public void nextGeneration() {
        future = new boolean[M][N];
        centerHandler();
        edgeCornerHandler();
        edgeLineHandler();
        grid = Arrays.stream(future).map(boolean[]::clone).toArray(boolean[][]::new);
    }

    private void randomBooleans() {
        grid = new boolean[M][N];
        for(int col = 0; col < M; col++)
            for (int row = 0; row < N; row++) grid[col][row] = rn.nextBoolean();
    }


    private void centerHandler(){
        // Loop through every cell
        int aliveNeighbours;
        for (int l = 1; l < M - 1; l++) {
            for (int k = 1; k < N - 1; k++) {
                // finding no Of Neighbours that are alive
                aliveNeighbours = 0;
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        if (grid[l + i][k + j])
                            aliveNeighbours += 1;
                // The cell needs to be subtracted from
                // its neighbours as it was counted before
                if (grid[l][k])
                    aliveNeighbours -= 1;
                // Implementing the Rules of Life
                // Cell is lonely and dies
                if ((grid[l][k]) && (aliveNeighbours < 2)) future[l][k] = false;
                // Cell dies due to over population
                else if ((grid[l][k]) && (aliveNeighbours > 3)) future[l][k] = false;
                // A new cell is born
                else if (aliveNeighbours == 3) future[l][k] = true;
                // Remains the same
                else future[l][k] = grid[l][k];
            }
        }
    }

    private void edgeCornerHandler(){
        // Edges Birth
        if ((grid[1][1]) && (grid[0][1]) && (grid[1][0])) future[0][0] = true;
        if ((grid[M-2][N-2]) && (grid[M-1][N-2]) && (grid[M-2][N-1])) future[M-1][N-1] = true;
        if ((grid[1][N-2]) && (grid[1][N-1]) && (grid[0][N-2])) future[0][N-1] = true;
        if ((grid[M-2][1]) && (grid[M-1][1]) && (grid[M-2][0])) future[M-1][0] = true;

        // Edges Death
        if (grid[0][0]) if( ((grid[1][1]) ? 1 : 0)+ ((grid[0][1]) ? 1 : 0) + ((grid[1][0]) ? 1 : 0) < 2) future[0][0] = false;
        if (grid[M-1][N-1]) if( ((grid[M-2][N-2]) ? 1 : 0)+ ((grid[M-1][N-2]) ? 1 : 0) + ((grid[M-2][N-1]) ? 1 : 0) < 2) future[M-1][N-1] = false;
        if (grid[0][N-1]) if( ((grid[1][N-1]) ? 1 : 0)+ ((grid[0][N-2]) ? 1 : 0) + ((grid[0][N-1]) ? 1 : 0) < 2) future[0][N-1] = false;
        if (grid[M-1][0]) if( ((grid[M-2][1]) ? 1 : 0)+ ((grid[M-1][1]) ? 1 : 0) + ((grid[M-2][0]) ? 1 : 0) < 2) future[M-1][0] = false;
    }

    private void edgeLineHandler(){
        int aliveNeighbours;
        // first Axis
        for (int l = 1; l < M - 1; l++) {
            aliveNeighbours = ((grid[l - 1][0]) ? 1 : 0) + ((grid[l - 1][1]) ? 1 : 0) + ((grid[l][1]) ? 1 : 0) + ((grid[l + 1][0]) ? 1 : 0) + ((grid[l + 1][1]) ? 1 : 0);
            if (aliveNeighbours == 3) future[l][0] = true;
            else future[l][0] = grid[l][0] && (aliveNeighbours == 2);

            aliveNeighbours = ((grid[l - 1][N-1]) ? 1 : 0) + ((grid[l - 1][N-2]) ? 1 : 0) + ((grid[l][N-2]) ? 1 : 0) + ((grid[l + 1][N-1]) ? 1 : 0) + ((grid[l + 1][N-2]) ? 1 : 0);
            if (aliveNeighbours == 3) future[l][N-1] = true;
            else future[l][N-1] = grid[l][N - 1] && (aliveNeighbours == 2);
        }
        for (int l = 1; l < N - 1; l++) {
            aliveNeighbours = ((grid[0][l - 1]) ? 1 : 0) + ((grid[1][l - 1]) ? 1 : 0) + ((grid[1][l]) ? 1 : 0) + ((grid[0][l + 1]) ? 1 : 0) + ((grid[1][l + 1]) ? 1 : 0);
            if (aliveNeighbours == 3) future[0][l] = true;
            else future[0][l] = grid[0][l] && (aliveNeighbours == 2);

            aliveNeighbours = ((grid[M - 1][l - 1]) ? 1 : 0) + ((grid[M - 2][l - 1]) ? 1 : 0) + ((grid[M-2][l]) ? 1 : 0) + ((grid[M - 1][l + 1]) ? 1 : 0) + ((grid[M - 2][l + 1]) ? 1 : 0);
            if (aliveNeighbours == 3) future[M-1][l] = true;
            else future[M-1][l] = grid[M - 1][l] && (aliveNeighbours == 2);
        }
    }
    public void setGrid(boolean[][] otherGrid){grid = Arrays.stream(otherGrid).map(boolean[]::clone).toArray(boolean[][]::new);}
    
    public boolean[][] getGrid(){return grid;}

    public Game clone(){
        Game cloned = new Game(M, N);
        cloned.setGrid(this.getGrid());
        return cloned;
    }
}
