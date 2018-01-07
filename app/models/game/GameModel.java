package models.game;

import io.ebean.Finder;
import io.ebean.annotation.DbJson;
import models.BaseModel;
import models.enumeration.GameDifficultyEnum;
import models.enumeration.RuleEnum;
import play.Logger;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GameModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.23
 * @since 17.12.23
 */
@Entity
@Table(name = "game")
public class GameModel extends BaseModel {

    /**
     * A Finder to simplify querying.
     *
     * @since 17.12.23
     */
    public static final Finder<Long, GameModel> find = new Finder<>(GameModel.class);

    /**
     * The list of players.
     *
     * @since 17.12.23
     */
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private final List<PlayerModel> players;

    /**
     * The board.
     *
     * @since 17.12.23
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, unique = true)
    private final BoardModel board;

    /**
     * The player to whom it's the turn to play.
     *
     * @since 17.12.23
     */
    @ManyToOne(targetEntity = PlayerModel.class)
    @Column(nullable = false)
    private PlayerModel playerTurn;

    /**
     * The difficulty of the game.
     *
     * @since 17.12.23
     */
    @Column(nullable = false)
    private GameDifficultyEnum difficulty;

    /**
     * The list of enabled rules.
     *
     * @since 17.12.23
     */
    @DbJson
    @Column(nullable = false)
    private List<RuleEnum> enabledRules;

    /**
     * Whether the game is over or not.
     *
     * @since 17.12.31
     */
    @Column(nullable = false)
    private boolean gameOver;

    /**
     * Create a new Game.
     *
     * @since 17.12.23
     */
    public GameModel() {
        this.board = new BoardModel();
        this.players = new ArrayList<>();
        this.gameOver = false;
    }

    /**
     * Return the board.
     *
     * @return the board
     * @since 17.12.23
     */
    public BoardModel getBoard() {
        return this.board;
    }

    /**
     * Return the player to whom it's the turn to play.
     *
     * @return the player to whom it's the turn to play.
     * @since 17.12.23
     */
    public PlayerModel getPlayerTurn() {
        return this.playerTurn;
    }

    /**
     * Set the player to whom it's the turn to play.
     *
     * @param playerTurn the player to whom it's the turn to play.
     * @since 17.12.23
     */
    public void setPlayerTurn(final PlayerModel playerTurn) {
        this.playerTurn = playerTurn;
    }

    /**
     * Return the difficulty of the game.
     *
     * @return the difficulty of the game
     * @since 17.12.23
     */
    public GameDifficultyEnum getDifficulty() {
        return this.difficulty;
    }

    /**
     * Set the difficulty of the game.
     *
     * @param difficulty the difficulty of the game
     * @since 17.12.23
     */
    public void setDifficulty(final GameDifficultyEnum difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Return the list of enabled rules.
     *
     * @return the list of enabled rules.
     * @since 17.12.23
     */
    public List<RuleEnum> getEnabledRules() {
        return this.enabledRules;
    }

    /**
     * Set the list of enabled rules
     *
     * @param enabledRules the list of enabled rules
     * @since 17.12.23
     */
    public void setEnabledRules(final List<RuleEnum> enabledRules) {
        this.enabledRules = enabledRules == null ? new ArrayList<>() : enabledRules;
    }

    /**
     * Return the list of players.
     *
     * @return the list of players
     * @since 17.12.23
     */
    public List<PlayerModel> getPlayers() {
        return this.players;
    }


    /**
     * Return the player at the given index.
     *
     * @return the player
     * @since 17.12.27
     */
    public PlayerModel getPlayer(final int index) {
        return this.players.get(index);
    }


    /**
     * Return the next player in the list.
     *
     * @param player the player preceding the one which is returned.
     * @return a player
     * @since 17.12.31
     */
    public PlayerModel getNextPlayer(final PlayerModel player) {
        int index = this.players.indexOf(player);
        if (index == -1) {
            Logger.debug("The player [{}] couldn't be found in the game [{}]", player.getUid(), this.uid);
            return null;
        }

        index = index + 1 < this.players.size() ? index + 1 : 0;

        return this.players.get(index);
    }

    /**
     * Add a player to the game.
     *
     * @param player the player to add to the game
     * @since 17.12.23
     */
    public void addPlayer(final PlayerModel player) {
        this.players.add(player);
    }

    /**
     * Set whether the game is over or not.
     *
     * @param gameOver true if the game is over
     * @since 17.12.31
     */
    public void setGameOver(final boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Return whether the game is over or not.
     *
     * @return true if the game is over
     * @since 17.12.31
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * Return the list of winners if the game is over.
     *
     * @return the list of winners. An empty list if the game is not over
     * @since 18.01.01
     */
    public List<PlayerModel> getWinners() {
        List<PlayerModel> winners = new ArrayList<>();
        if (this.gameOver) {
            final Integer maxScore = this.players.stream().map(PlayerModel::getScore).max(Integer::compare).orElse(0);
            winners = this.players.stream().filter(playerModel -> playerModel.getScore() == maxScore).collect(Collectors.toList());
        }
        return winners;
    }
}
