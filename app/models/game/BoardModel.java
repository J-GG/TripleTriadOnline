package models.game;

import models.BaseModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BoardModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.23
 * @since 17.12.23
 */
@Entity
@Table(name = "board")
public class BoardModel extends BaseModel {

    /**
     * The game to which the board belongs to.
     *
     * @since 17.12.23
     */
    @OneToOne(targetEntity = GameModel.class, mappedBy = "board")
    private GameModel game;

    /**
     * The array of cases making the board.
     *
     * @since 17.12.23
     */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private final List<CaseModel> cases;

    /**
     * The number of rows on the cases.
     *
     * @since 17.12.23
     */
    @Transient
    private final Integer NB_ROWS = 3;

    /**
     * The number of columns on the cases.
     *
     * @since 17.12.23
     */
    @Transient
    private final Integer NB_COLS = 3;

    /**
     * Create a new cases.
     *
     * @since 17.12.23
     */
    public BoardModel() {
        this.cases = new ArrayList<>();
        for (int row = 1; row <= this.NB_ROWS; row++) {
            for (int col = 1; col <= this.NB_COLS; col++) {
                this.cases.add(new CaseModel(row, col));
            }
        }
    }

    /**
     * Return the array of cases making the board.
     *
     * @return the array of cases making the board
     * @since 17.12.23
     */
    public List<CaseModel> getCases() {
        return this.cases;
    }

    /**
     * Return the case at the given coordinates.
     *
     * @param row the row of the case
     * @param col the col of the case
     * @return the case
     * @since 17.12.24
     */
    public CaseModel getCase(final int row, final int col) {
        if (row < 1 || row > this.NB_ROWS) {
            play.Logger.debug("The given row ({}) is out of range [1-{}]", row, this.NB_ROWS);
            return null;
        }
        if (col < 1 || col > this.NB_COLS) {
            play.Logger.debug("The given column ({}) is out of range [1-{}]", row, this.NB_COLS);
            return null;
        }

        return this.cases.get((row - 1) * this.NB_ROWS + col - 1);
    }

    /**
     * Return the number of rows on the cases.
     *
     * @return the number of rows on the cases
     * @since 17.12.23
     */
    public Integer getNbRows() {
        return this.NB_ROWS;
    }

    /**
     * Return the number of columns on the cases.
     *
     * @return the number of columns on the cases
     * @since 17.12.23
     */
    public Integer getNbCols() {
        return this.NB_COLS;
    }
}
