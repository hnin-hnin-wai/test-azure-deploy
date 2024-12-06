package edu.miu.cse.heartlink.model;
import edu.miu.cse.heartlink.constant.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="itemrequests")
@NoArgsConstructor
@Getter
@Setter
public class ItemClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name="item_id")
    Item item;

    @ManyToOne
    @JoinColumn(name="donar_id")
    private User donar;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    private User receiver;

}
