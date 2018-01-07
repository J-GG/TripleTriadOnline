package models.game;

import models.BaseModel;
import play.Logger;

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
        for (int row = 0; row < this.NB_ROWS; row++) {
            for (int col = 0; col < this.NB_COLS; col++) {
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
        if (row < 0 || row > this.NB_ROWS - 1) {
            Logger.debug("The given row ({}) is out of range [0-{}]", row, this.NB_ROWS - 1);
            return null;
        }
        if (col < 0 || col > this.NB_COLS - 1) {
            Logger.debug("The given column ({}) is out of range [0-{}]", row, this.NB_COLS - 1);
            return null;
        }

        for (final CaseModel aCase : this.cases) {
            if (aCase.getRow() == row && aCase.getCol() == col) {
                return aCase;
            }
        }

        Logger.warn("No case could be found at the coordinates ({}, {})", row, this.NB_COLS - 1);

        return null;
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

    /**
     * Unflip all the cards on the board.
     *
     * @since 18.01.06
     */
    public void unflipCards() {
        for (final CaseModel caseModel : this.cases) {
            if (caseModel.getCardOnCase() != null) {
                caseModel.getCardOnCase().unflip();
            }
        }
    }
}
