package models.game;

import models.BaseModel;

import javax.persistence.*;

/**
 * CaseModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.23
 * @since 17.12.23
 */
@Entity
@Table(name = "\"case\"")
public class CaseModel extends BaseModel {

    /**
     * The card placed on the case.
     *
     * @since 17.12.23
     */
    @OneToOne(cascade = CascadeType.ALL)
    private CardOnCaseModel cardOnCase;

    /**
     * The board on which the case is.
     *
     * @since 17.12.23
     */
    @ManyToOne(targetEntity = BoardModel.class)
    @JoinColumn(nullable = false)
    private BoardModel board;

    /**
     * The row of the case.
     *
     * @since 17.12.24
     */
    @Column(nullable = false)
    private Integer row;

    /**
     * The column of the case.
     *
     * @since 17.12.24
     */
    @Column(nullable = false)
    private Integer col;

    /**
     * Create a new case a the given coordinates.
     *
     * @param row the row of the case
     * @param col the column of the case
     * @since 17.12.24
     */
    public CaseModel(final Integer row, final Integer col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Return the card placed on the case. Null if there is no card.
     *
     * @return the card placed on the case
     * @since 17.12.23
     */
    public CardOnCaseModel getCardOnCase() {
        return this.cardOnCase;
    }

    /**
     * Set the card on the case.
     *
     * @param cardOnCase the card to place on the case.
     * @since 17.12.23
     */
    public void setCardOnCase(final CardOnCaseModel cardOnCase) {
        this.cardOnCase = cardOnCase;
    }

    /**
     * Return the board on which the case is.
     *
     * @return the board on which the case is
     * @since 17.12.24
     */
    public BoardModel getBoard() {
        return this.board;
    }

    /**
     * Set the board on which the case is.
     *
     * @param board the board on which the case is
     * @since 17.12.24
     */
    public void setBoard(final BoardModel board) {
        this.board = board;
    }

    /**
     * Return the row of the case.
     *
     * @return the row of the case
     * @since 17.12.24
     */
    public Integer getRow() {
        return this.row;
    }

    /**
     * Set the row of the case.
     *
     * @param row the row of the case
     * @since 17.12.24
     */
    public void setRow(final Integer row) {
        this.row = row;
    }

    /**
     * Return the column of the case.
     *
     * @return the column of the case
     * @since 17.12.24
     */
    public Integer getCol() {
        return this.col;
    }

    /**
     * Set the column of the case.
     *
     * @param col the column of the case
     * @since 17.12.24
     */
    public void setCol(final Integer col) {
        this.col = col;
    }
}
