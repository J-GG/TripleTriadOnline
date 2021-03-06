package models.entity.game;

import models.enumeration.RuleEnum;
import models.game.GameModel;
import models.game.PlayerModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * GameEntity.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.14
 * @since 17.12.24
 */
public class GameEntity {

    /**
     * The identifier of the game.
     *
     * @since 17.12.24
     */
    private final UUID gameRef;

    /**
     * The date the game was started.
     *
     * @since 18.01.14
     */
    private final Date createdAt;

    /**
     * The identifier of the player to whom it's the turn.
     *
     * @since 17.12.24
     */
    private final UUID playerTurnRef;

    /**
     * The list of players.
     *
     * @since 17.12.24
     */
    private final List<PlayerEntity> players;

    /**
     * The board.
     *
     * @since 17.12.24
     */
    private final BoardEntity board;

    /**
     * The list of enabled rules.
     *
     * @since 17.12.26
     */
    private final List<RuleEnum> enabledRules;

    /**
     * Whether the game is over or not.
     *
     * @since 17.12.31
     */
    private final boolean gameOver;


    /**
     * The list of winners' identifiers.
     *
     * @since 17.12.26
     */
    private final List<UUID> winnersRef;

    /**
     * Create a new GameEntity based on a GameModel.
     *
     * @param gameModel the model of the entity
     * @since 17.12.24
     */
    public GameEntity(final GameModel gameModel) {
        this.gameRef = gameModel.getUid();
        this.createdAt = gameModel.getCreatedAt();
        this.playerTurnRef = gameModel.getPlayerTurn().getUid();
        this.players = new ArrayList<>();
        for (final PlayerModel player : gameModel.getPlayers()) {
            this.players.add(new PlayerEntity(player));
        }
        this.board = new BoardEntity(gameModel.getBoard());
        this.enabledRules = gameModel.getEnabledRules();
        this.gameOver = gameModel.isGameOver();
        this.winnersRef = gameModel.getWinners().stream().map(PlayerModel::getUid).collect(Collectors.toList());
    }

    /**
     * Return the unique identifier of the game.
     *
     * @return the unique identifier of the game
     * @since 17.12.26
     */
    public UUID getGameRef() {
        return this.gameRef;
    }

    /**
     * Return the date of creation.
     *
     * @return the date of creation
     * @since 18.01.14
     */
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Return the identifier of the player to whom it's the turn.
     *
     * @return the identifier of the player to whom it's the turn.
     * @since 17.12.24
     */
    public UUID getPlayerTurnRef() {
        return this.playerTurnRef;
    }

    /**
     * Return the list of players
     *
     * @return the list of players
     * @since 17.12.24
     */
    public List<PlayerEntity> getPlayers() {
        return this.players;
    }

    /**
     * Return the board.
     *
     * @return the board
     * @since 17.12.24
     */
    public BoardEntity getBoard() {
        return this.board;
    }

    /**
     * Return the list of enabled rules.
     *
     * @return the list of enabled rules
     * @since 17.12.26
     */
    public List<RuleEnum> getEnabledRules() {
        return this.enabledRules;
    }

    /**
     * Return whether is the game is over or not.
     *
     * @return true if the game is over
     * @since 18.01.01
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * Return the list of winners' identifiers.
     *
     * @return the list of winners' identifiers. An empty list if the game is not over
     * @since 18.01.01
     */
    public List<UUID> getWinnersRef() {
        return this.winnersRef;
    }
}
