package models;


import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

/**
 * BaseModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
@MappedSuperclass
public abstract class BaseModel extends Model {
    /**
     * The unique identifier.
     *
     * @since 17.12.17
     */
    @Id
    private UUID uid;

    /**
     * The creation date
     *
     * @since 17.12.17
     */
    @Column(nullable = false)
    private final Date createdAt;

    /**
     * Constructor setting the date of creation at now.
     *
     * @since 17.12.17
     */
    public BaseModel() {
        this.createdAt = new Date();
    }

    /**
     * Return the unique identifier.
     *
     * @return the unique identifier
     * @since 17.12.17
     */
    public UUID getUid() {
        return this.uid;
    }
}
