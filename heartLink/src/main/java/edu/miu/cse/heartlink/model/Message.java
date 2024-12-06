package edu.miu.cse.heartlink.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name="messages")
@NoArgsConstructor
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="message_id")
    private Integer messageId;
    private String content;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDateTime;

    @ManyToOne
    @JoinColumn(name="conversation_id")
    Conversation conversation;

    @OneToOne
    @JoinColumn(name="sender_Id")
    User sender;

    @OneToOne
    @JoinColumn(name="receiver_Id")
    User receiver;

}
