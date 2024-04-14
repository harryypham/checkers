package Checkers;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 0;
    public static final int BOARD_WIDTH = 8;
    public static final int[] BLACK_RGB = { 181, 136, 99 };
    public static final int[] WHITE_RGB = { 240, 217, 181 };
    public static final float[][][] coloursRGB = new float[][][] {
            // default - white & black
            {
                    { WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2] },
                    { BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2] }
            },
            // green
            {
                    { 105, 138, 76 }, // when on white cell
                    { 105, 138, 76 } // when on black cell
            },
            // blue
            {
                    { 196, 224, 232 },
                    { 170, 210, 221 }
            }
    };

    public static int WIDTH = CELLSIZE * BOARD_WIDTH + SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH * CELLSIZE;

    public static final int FPS = 60;

    private Cell[][] board;
    private Piece currentSelected;
    private HashSet<Cell> possibleMove;
    // private HashMap<Character, HashSet<Piece>> piecesInPlay = new HashMap<>();
    private char currentPlayer = 'w';
    private int numWhites;
    private int numBlacks;

    public App() {

    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        frameRate(FPS);

        // Set up the data structures used for storing data in the game
        this.board = new Cell[BOARD_WIDTH][BOARD_WIDTH];
        HashSet<Piece> w = new HashSet<>();
        HashSet<Piece> b = new HashSet<>();
        numWhites = 12;
        numBlacks = 12;
        // piecesInPlay.put('w', w);
        // piecesInPlay.put('b', b);

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                board[row][col] = new Cell(col, row);
                if ((col + row) % 2 == 1) {
                    if (row < 3) {
                        board[row][col].setPiece(new Piece('w'));
                        w.add(board[row][col].getPiece());
                    } else if (row >= 5) {
                        board[row][col].setPiece(new Piece('b'));
                        b.add(board[row][col].getPiece());
                    }
                }
            }
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed() {

    }

    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased() {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Check if the user clicked on a piece which is theirs - make sure only
        // whoever's current turn it is, can click on pieces

        // TODO: Check if user clicked on an available move - move the selected piece
        // there.
        // TODO: Remove captured pieces from the board
        // TODO: Check if piece should be promoted and promote it
        // TODO: Then it's the other player's turn.
        int x = e.getX();
        int y = e.getY();
        if (x < 0 || x >= App.WIDTH || y < 0 || y >= App.HEIGHT)
            return;

        Cell clicked = board[y / App.CELLSIZE][x / App.CELLSIZE];
        if (clicked.getPiece() != null && Character.toLowerCase(clicked.getPiece().getColour()) == currentPlayer) {
            if (clicked.getPiece() == currentSelected) {
                currentSelected = null;
                possibleMove = null;
            } else {
                currentSelected = clicked.getPiece();
            }

            if (currentSelected != null) {

                possibleMove = new HashSet<Cell>();
                findAllPossibleMoves(y / App.CELLSIZE, x / App.CELLSIZE, currentSelected.getColour());

            }
        }
        if (currentSelected != null && clicked.getPiece() == null && possibleMove.contains(clicked)) {
            possibleMove = null;
            int oldx = currentSelected.getPosition().getX();
            int oldy = currentSelected.getPosition().getY();
            if (currentSelected.getColour() == 'w' && clicked.getY() == 7) {
                currentSelected.promote();
            }
            if (currentSelected.getColour() == 'b' && clicked.getY() == 0) {
                currentSelected.promote();
            }
            if (Math.abs(clicked.getX() - oldx) == 2 && board[(clicked.getY() + oldy) / 2][(clicked.getX() + oldx) / 2]
                    .getPiece().getColour() != currentSelected.getColour()) {
                if (Character.toLowerCase(board[(clicked.getY() + oldy) / 2][(clicked.getX() + oldx) / 2].getPiece()
                        .getColour()) == 'w') {
                    numWhites -= 1;
                } else {
                    numBlacks -= 1;
                }
                board[(clicked.getY() + oldy) / 2][(clicked.getX() + oldx) / 2].setPiece(null);
            }
            board[y / App.CELLSIZE][x / App.CELLSIZE].setPiece(currentSelected);
            board[oldy][oldx].setPiece(null);
            currentSelected = null;
            if (currentPlayer == 'w') {
                currentPlayer = 'b';
            } else {
                currentPlayer = 'w';
            }
        }
    }

    private void findAllPossibleMoves(int row, int col, char color) {
        int[][] moves = { { 1, -1 }, { 1, 1 }, { -1, -1 }, { -1, 1 }, { 2, -2 }, { 2, 2 }, { -2, -2 }, { -2, 2 } };
        if (color == 'w' || color == 'W') {
            for (int i = 0; i < moves.length; i++) {
                int x = moves[i][0];
                int y = moves[i][1];
                if (color == 'w' && x < 0) {
                    continue;
                }
                if (row + x > 7 || row + x < 0 || col + y > 7 || col + y < 0) {
                    continue;
                }
                if (board[row + x][col + y].getPiece() == null) {
                    if (x == 1 || x == -1) {
                        possibleMove.add(board[row + x][col + y]);
                    } else if (board[row + (x / 2)][col + (y / 2)].getPiece() != null) {
                        possibleMove.add(board[row + x][col + y]);

                    }
                }
            }
        } else {
            for (int i = 0; i < moves.length; i++) {
                int x = moves[i][0];
                int y = moves[i][1];
                if (color == 'b' && x > 0) {
                    continue;
                }
                if (row + x > 7 || row + x < 0 || col + y > 7 || col + y < 0) {
                    continue;
                }
                if (board[row + x][col + y].getPiece() == null) {
                    if (x == 1 || x == -1) {
                        possibleMove.add(board[row + x][col + y]);
                    } else if (board[row + (x / 2)][col + (y / 2)].getPiece() != null) {
                        possibleMove.add(board[row + x][col + y]);

                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    private void checkEndgame() {
        if (numWhites == 0 || numBlacks == 0) {
            fill(255);
            stroke(0);
            strokeWeight(4.0f);
            rect(App.WIDTH * 0.25f, App.HEIGHT * 0.375f, App.WIDTH * 0.5f, App.HEIGHT * 0.25f); // Draw a rectangle for
                                                                                                // the text background.
            fill(0, 0, 0);
            textSize(24.0f);
            if (numWhites == 0) {
                text("Black wins!", App.WIDTH * 0.35f, App.HEIGHT * 0.5f);
            } else if (numBlacks == 0) {
                text("White wins!", App.WIDTH * 0.35f, App.HEIGHT * 0.5f);
            }
        }
    }

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        this.noStroke();
        background(WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]);

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (currentSelected != null && board[row][col].getPiece() == currentSelected) {
                    // highlighted cells
                    this.setFill(1, (col + row) % 2);
                    this.rect(col * App.CELLSIZE, row * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                } else if ((col + row) % 2 == 1) {
                    // normal cells
                    this.fill(BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]);
                    this.rect(col * App.CELLSIZE, row * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                }
                if (possibleMove != null && possibleMove.contains(board[row][col])) {
                    this.setFill(2, (col + row) % 2);
                    this.rect(col * App.CELLSIZE, row * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                }
                board[row][col].draw(this);
            }
        }

        // check if the any player has no more pieces. The winner is the player who
        // still has pieces remaining
        checkEndgame();
        // Draw the board and the pieces.

    }

    /**
     * Set fill colour for cell background
     * 
     * @param colourCode   The colour to set
     * @param blackOrWhite Depending on if 0 (white) or 1 (black) then the cell may
     *                     have different shades
     */
    public void setFill(int colourCode, int blackOrWhite) {
        this.fill(coloursRGB[colourCode][blackOrWhite][0], coloursRGB[colourCode][blackOrWhite][1],
                coloursRGB[colourCode][blackOrWhite][2]);
    }

    public static void main(String[] args) {
        PApplet.main("Checkers.App");
    }

}
