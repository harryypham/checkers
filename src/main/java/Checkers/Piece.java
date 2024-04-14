package Checkers;

public class Piece {

    // The color of the checkers piece ('w' for white, 'b' for black)
    private char colour;

    // The current position of the piece on the board
    private Cell position;

    // Constructor: Initializes a new piece with a given color
    public Piece(char c) {
        this.colour = c;
    }

    // Returns the color of the piece
    public char getColour() {
        return this.colour;
    }

    // Sets the position of the piece to a given cell
    public void setPosition(Cell p) {
        this.position = p;
    }

    // Returns the current position of the piece
    public Cell getPosition() {
        return this.position;
    }

    public void capture() {
        // capture this piece
    }

    public void promote() {
        // promote this piece
        this.colour = Character.toUpperCase(this.colour);
    }

    public void draw(App app) {
        // Set the stroke weight for the outline of the piece
        app.strokeWeight(5.0f);

        if (Character.toLowerCase(colour) == 'w') {
            // White piece
            app.fill(255); // white fill
            app.stroke(0); // black stroke
        } else if (Character.toLowerCase(colour) == 'b') {
            // Black piece
            app.fill(0); // black fill
            app.stroke(255);// white stroke
        }

        app.ellipse(position.getX() * App.CELLSIZE + App.CELLSIZE / 2,
                position.getY() * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
        if (colour == 'W' || colour == 'B') {
            app.ellipse(position.getX() * App.CELLSIZE + App.CELLSIZE / 2,
                    position.getY() * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.4f, App.CELLSIZE * 0.4f);
        }
        // Disable the stroke for subsequent drawings
        app.noStroke();
    }
}