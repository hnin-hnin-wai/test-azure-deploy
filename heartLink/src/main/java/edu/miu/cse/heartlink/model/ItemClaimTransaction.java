package edu.miu.cse.heartlink.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="itemrequestransactions")
@NoArgsConstructor
@Getter
@Setter
public class ItemClaimTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToOne
    @JoinColumn(name="itemrequest_id")
    private ItemClaim itemRequest;

}
