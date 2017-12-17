package models;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * PlayerModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
@Entity
@Table(name = "player")
public class PlayerModel extends BaseModel {

    /**
     * The member who is the player.
     *
     * @since 17.12.17
     */
    private MemberModel member;
}
