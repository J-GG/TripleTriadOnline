package toolbox;

import models.enumeration.RuleEnum;
import models.game.*;
import toolbox.rules.RuleFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AI.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.08
 * @since 18.01.08
 */
public class AI {

    /**
     * The game.
     *
     * @since 18.01.08
     */
    private final GameModel game;

    /**
     * The player with the AI.
     *
     * @since 18.01.08
     */
    private final PlayerModel player;

    /**
     * The case selected by the AI.
     *
     * @since 18.01.08
     */
    private CaseModel selectedCase;

    /**
     * The card selected by the AI.
     *
     * @since 18.01.08
     */
    private CardInDeckModel selectedCard;

    /**
     * Create a new AI.
     *
     * @param game   the game
     * @param player the player with the AI
     * @since 18.01.08
     */
    public AI(final GameModel game, final PlayerModel player) {
        this.game = game;
        this.player = player;
    }

    /**
     * Choose a card and case to play on the bord based on the difficulty of the game.
     *
     * @since 18.01.08
     */
    public void chooseCardAndCase() {
        switch (this.game.getDifficulty()) {
            case EASY:
                chooseCardAndCaseRandomly();
                break;
            case NORMAL:
                chooseCardAndCaseBasedOnRules(new ArrayList<>());
                break;
            case HARD:
                chooseCardAndCaseBasedOnRules(this.game.getEnabledRules());
                break;
        }
    }

    /**
     * Choose a card and a case randomly.
     *
     * @since 18.01.08
     */
    private void chooseCardAndCaseRandomly() {
        final Random randomGenerator = new Random();

        final int selectedCardIndex = randomGenerator.nextInt(this.player.getDeck().size());
        this.selectedCard = this.player.getDeck().get(selectedCardIndex);

        final List<CaseModel> emptyCases = new ArrayList<>();
        for (final CaseModel caseModel : this.game.getBoard().getCases()) {
            if (caseModel.getCardOnCase() == null) {
                emptyCases.add(caseModel);
            }
        }
        final int selectedCaseIndex = randomGenerator.nextInt(emptyCases.size());
        this.selectedCase = emptyCases.get(selectedCaseIndex);
    }

    /**
     * Choose a card and a case randomly.
     *
     * @since 18.01.08
     */
    private void chooseCardAndCaseBasedOnRules(final List<RuleEnum> rules) {
        final List<CaseModel> emptyCases = new ArrayList<>();
        for (final CaseModel caseModel : this.game.getBoard().getCases()) {
            if (caseModel.getCardOnCase() == null) {
                emptyCases.add(caseModel);
            }
        }

        final List<CardInDeckModel> playerCards = this.player.getDeck();

        int maxFlipped = -1;
        for (final CardInDeckModel cardInDeck : playerCards) {
            for (final CaseModel caseModel : emptyCases) {
                caseModel.setCardOnCase(new CardOnCaseModel(cardInDeck));
                final int nbCardFlipped = RuleFactory.testRules(this.game, rules, caseModel);
                caseModel.setCardOnCase(null);
                if (nbCardFlipped > maxFlipped) {
                    maxFlipped = nbCardFlipped;
                    this.selectedCase = caseModel;
                    this.selectedCard = cardInDeck;
                }
            }
        }
    }

    /**
     * Return the selected case.
     *
     * @return the selected case
     * @since 18.01.08
     */
    public CaseModel getSelectedCase() {
        return this.selectedCase;
    }

    /**
     * Return the selected card.
     *
     * @return the selected card
     * @since 18.01.08
     */
    public CardInDeckModel getSelectedCard() {
        return this.selectedCard;
    }
}
