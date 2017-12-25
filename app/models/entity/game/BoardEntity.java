package models.entity.game;

import models.game.BoardModel;

/**
 * BoardEntity.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.24
 * @since 17.12.24
 */
public class BoardEntity {

    /**
     * The number of rows.
     *
     * @since 17.12.24
     */
    private final int nbRows;

    /**
     * The number of columns.
     *
     * @since 17.12.24
     */
    private final int nbCols;

    /**
     * The array of cases making the board.
     *
     * @since 17.12.24
     */
    private final CaseEntity[][] cases;

    /**
     * Create a new BoardEntity based on a BoardModel.
     *
     * @param boardModel the model of the entity
     * @since 17.12.24
     */
    public BoardEntity(final BoardModel boardModel) {
        this.nbRows = boardModel.getNbRows();
        this.nbCols = boardModel.getNbCols();
        this.cases = new CaseEntity[boardModel.getNbRows()][boardModel.getNbCols()];
        for (int row = 1; row < boardModel.getNbRows(); row++) {
            for (int col = 1; col < boardModel.getNbCols(); col++) {
                this.cases[row][col] = new CaseEntity(boardModel.getCase(row, col));
            }
        }
    }

    /**
     * Return the number of rows.
     *
     * @return the number of rows
     * @since 17.12.24
     */
    public int getNbRows() {
        return this.nbRows;
    }

    /**
     * Return the number of columns.
     *
     * @return the number of columns
     * @since 17.12.24
     */
    public int getNbCols() {
        return this.nbCols;
    }

    /**
     * Return the array of cases making the board.
     *
     * @return the array of cases making the board
     * @since 17.12.24
     */
    public CaseEntity[][] getCases() {
        return this.cases;
    }
}
