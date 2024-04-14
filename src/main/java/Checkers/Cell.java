package Checkers;

public class Cell {

    // The x-coordinate of the cell on the board
    private int x;

    // The y-coordinate of the cell on the board
    private int y;

    // The piece that is currently occupying this cell (null if the cell is empty)
    private Piece piece;

    // Constructor: Initializes a cell with specified x and y coordinates
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Returns the x-coordinate of the cell
    public int getX() {
        return this.x;
    }

    // Returns the y-coordinate of the cell
    public int getY() {
        return this.y;
    }

    // Assigns a piece to this cell and updates the piece's position to this cell
    // If a piece is already present, it is replaced by the new piece
    public void setPiece(Piece p) {
        this.piece = p;

        // If the piece is not null, update the piece's position to this cell
        if (p != null) {
            p.setPosition(this);
        }
    }

    // Returns the piece that is currently occupying the cell, or null if the cell
    // is empty
    public Piece getPiece() {
        return this.piece;
    }

    public void draw(App app) {
        if (this.piece != null)
            this.piece.draw(app);
    }
}