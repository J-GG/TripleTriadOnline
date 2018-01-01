package models.entity.game;

import models.game.CaseModel;

/**
 * CaseEntity.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.24
 * @since 17.12.24
 */
public class CaseEntity {

    /**
     * The card.
     *
     * @since 17.12.24
     */
    private CardOnCaseEntity cardOnCase;

    /**
     * Create a new CaseEntity based on a CaseModel.
     *
     * @param caseModel the model of the entity
     * @since 17.12.24
     */
    public CaseEntity(final CaseModel caseModel) {
        if (caseModel.getCardOnCase() != null) {
            this.cardOnCase = new CardOnCaseEntity(caseModel.getCardOnCase());
        }
    }

    /**
     * Return the card on the case.
     *
     * @return the card on the case
     * @since 17.12.24
     */
    public CardOnCaseEntity getCardOnCase() {
        return this.cardOnCase;
    }
}
