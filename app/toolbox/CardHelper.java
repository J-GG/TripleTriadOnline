package toolbox;

import models.game.CardModel;
import play.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * CardHelper.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.23
 * @since 17.12.23
 */
public class CardHelper {

    /**
     * Get an array of cards based on a weighted random selection. The weight depends on the level of the cards.
     *
     * @param numberOfCards The number of returned cards.
     * @return a list of cards randomly selected
     * @since 17.12.23
     */
    public static List<CardModel> getRandomCards(final int numberOfCards) {
        if (numberOfCards < 0) {
            play.Logger.debug("The number of generated cards should be greater than 0");
        }

        final List<CardModel> allCards = CardModel.find.all();
        int sum = 0;
        for (final CardModel card : allCards) {
            sum += CardModel.MAX_LEVEL + 1 - card.getLevel();
        }

        final Random randomGenerator = new Random();
        final List<CardModel> cards = new ArrayList<>();
        draw_card:
        for (int i = 0; i < numberOfCards; i++) {
            final int randomNumber = randomGenerator.nextInt(sum + 1);
            int seek = 0;
            for (final CardModel card : allCards) {
                if (randomNumber < seek + CardModel.MAX_LEVEL + 1 - card.getLevel()) {
                    cards.add(card);
                    continue draw_card;
                }

                seek += CardModel.MAX_LEVEL + 1 - card.getLevel();
            }
        }

        Logger.info("{} cards have been randomly picked", numberOfCards);

        return cards;
    }
}
