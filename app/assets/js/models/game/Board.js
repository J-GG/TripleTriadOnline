'use strict';

/**
 * A Board.
 * @author Jean-Gabriel Genest
 * @since 17.12.26
 * @version 17.12.26
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
        getNbColums() {
            return this.nbCols;
        }
    }
});