package main;


import java.util.*;
import java.lang.*;

/* Name of the class has to be "Main" only if the class is public. */
class GameOfLife
{
    public static class Pair{
        int first;
        int second;

        public Pair(int first, int second){
            this.first=first;
            this.second=second;
        }

        @Override
        public boolean equals(Object p){
            return (p instanceof Pair) && this.first == ((Pair)p).first && this.second == ((Pair)p).second;
        }

        public int hashCode() {
            return 31*first + 17*second;
        }

    }



    // The value representing a dead cell
    public final static int DEAD    = 0;

    // The value representing a live cell
    public final static int ALIVE    = 1;


    public static void main(String[] args) {

        // test the game of life implementation
        GameOfLife Gof = new GameOfLife();
        Gof.test(1);
    }


    /**
     * Test the gameoflife implementation, change the array
     * values to test each condition in the game of life.
     *
     * @param nrIterations      the number of times the board should be played
     */
    private void test(int nrIterations) {

        System.out.println("Please enter number of live coordinates");

        Scanner s = new Scanner(System.in);
        int noOfLiveCoordinates = s.nextInt();

        System.out.println("Please enter live cooordiates in separate line");


        int boardRows = Integer.MIN_VALUE;
        int boardCols = Integer.MIN_VALUE;

        Set<Pair> LiveCoordinates = new HashSet<>();
        for(int i=0;i<noOfLiveCoordinates;i++){
            int x = s.nextInt();
            int y = s.nextInt();
            LiveCoordinates.add(new Pair(x, y));
            boardRows = Math.max(boardRows,x);
            boardCols = Math.max(boardCols,y);
        }

        int[][] board = new int[boardRows+2][boardCols+2];

        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[0].length;j++){
                if(LiveCoordinates.contains(new Pair(i, j))){
                    board[i][j] = ALIVE;
                }
                else{
                    board[i][j] = DEAD;
                }
            }
        }

        
        

        for (int i = 0 ; i < nrIterations ; i++) {
            System.out.println();
            board = getNextBoard(board);
            printBoard(board);
        }
    }

    private void printBoard(int[][] board) {
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[0].length;j++){
                if(board[i][j]==ALIVE){
                    System.out.println(i + " " + j);
                }
            }
        }
    }

    /**
     * get the next game board, this will calculate if cells live on or die or new
     * ones should be created by reproduction.
     **/


    public int[][] getNextBoard(int[][] board) {

        // if the board is empty throw exception
        if (board.length == 0 || board[0].length == 0) {
            throw new IllegalArgumentException("Board cannot be empty");
        }

        int Rows = board.length;
        int Cols = board[0].length;

        // output board to store new values
        int[][] output = new int[Rows][Cols];

        for (int row = 0 ; row < Rows ; row++) {

            for (int col = 0 ; col < Cols ; col++) {
                output[row][col] = NewCellState(board[row][col], countLiveNeighbours(row, col, board));
            }
        }
        return output;
    }

    /**
     * Returns the no of live neighbours given the cell position
     *
     * @param cellRow       the column position of the cell
     * @param cellCol       the row position of the cell
     * @return the number of live neighbours given the position in the array
     */
    private int countLiveNeighbours(int cellRow, int cellCol, int[][] board) {

        int liveNeighbours = 0;
        int rowEnd = Math.min(board.length , cellRow + 2);
        int colEnd = Math.min(board[0].length, cellCol + 2);

        for (int row = Math.max(0, cellRow - 1) ; row < rowEnd ; row++) {

            for (int col = Math.max(0, cellCol - 1) ; col < colEnd ; col++) {

                // make sure to exclude the cell itself from calculation
                if ((row != cellRow || col != cellCol) && board[row][col] == ALIVE) {
                    liveNeighbours++;
                }
            }
        }
        return liveNeighbours;
    }


    /**
     * Returns the new state given the curState and count of live neughbours
     *
     * @param curState          The current state of the cell, either DEAD or ALIVE
     * @param liveNeighbours    The number of live neighbours of the given cell
     *
     * @return The new state of the cell given the number of live neighbours
     *         and the current state
     */
    private int NewCellState(int curState, int liveNeighbours) {

        int newState = curState;

        switch (curState) {
            case ALIVE:

                // Any live cell with fewer than two
                // live neighbours dies
                if (liveNeighbours < 2) {
                    newState = DEAD;
                }

                // Any live cell with more than three live neighbours
                // dies, as if by overcrowding.
                if (liveNeighbours > 3) {
                    newState = DEAD;
                }
                break;

            case DEAD:
                // Any dead cell with exactly three live neighbours becomes a
                // live cell, as if by reproduction.
                if (liveNeighbours == 3) {
                    newState = ALIVE;
                }
                break;

            default:
                throw new IllegalArgumentException("State of cell must be either LIVE or DEAD");
        }
        return newState;
    }
}