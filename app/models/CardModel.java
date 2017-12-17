package models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "card")
public class CardModel extends BaseModel {
    private String name;

    private Integer level;

    private Integer upNumber;

    private Integer rightNumber;

    private Integer downNumber;

    private Integer leftNumber;

    @ManyToMany(mappedBy = "cards", targetEntity = MemberModel.class, fetch = FetchType.LAZY)
    @JoinTable(name = "member_owns_card")
    private List<MemberModel> owners;
}
