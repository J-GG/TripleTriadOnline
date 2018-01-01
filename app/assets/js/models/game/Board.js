'use strict';

/**
 * A Board.
 * @author Jean-Gabriel Genest
 * @since 17.12.26
 * @version 17.12.27
 */
define([cardGame.gamePath + "js/models/game/Case.js"], function (Case) {
    return class Board {

        /**
         * Create a new board based on the parameters.
         * @param board the board
         * @since 17.12.26
         */
        constructor(board) {
            this.nbRows = board.nbRows;
            this.nbCols = board.nbCols;
            this.cases = [];
            for (let i in board.cases) {
                let cols = [];
                for (let j in board.cases[i]) {
                    cols.push(new Case(board.cases[i][j]));
                }
                this.cases.push(cols);
            }
        }

        /**
         * Get the number of rows.
         * @returns {*} the number of rows
         * @since 17.12.26
         */
        getNbRows() {
            return this.nbRows;
        }

        /**
         * Get the number of columns.
         * @returns {*} the number of columns
         * @since 17.12.26
         */
        getNbCols() {
            return this.nbCols;
        }

        /**
         * Get the array of cases.
         * @returns {Array} the array of cases
         * @since 17.12.27
         */
        getCases() {
            return this.cases;
        }

        /**
         * Get the case.
         * @param row the row of the case
         * @param col the column of the case
         * @returns {Array} the case
         * @since 17.12.27
         */
        getCase(row, col) {
            if (row < 0 || row >= this.nbRows) {
                logger.warning("The row should be between 0 and " + (this.nbRows - 1) + " but [row: " + row + "] found");
                return;
            }
            if (col < 0 || col >= this.nbCols) {
                logger.warning("The column should be between 0 and " + (this.nbCols - 1) + " but [col: " + col + "] found");
                return;
            }

            return this.cases[row][col];
        }
    }
});