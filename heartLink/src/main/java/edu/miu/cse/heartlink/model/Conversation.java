package edu.miu.cse.heartlink.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name="conversations")
@NoArgsConstructor
@Data
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String title;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDateTime;//default dateTime value in DB

    @ManyToOne
    @JoinColumn(name="sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    private  User receiver;

    @ManyToOne
    @JoinColumn(name="item_id")
    Item item;

}
