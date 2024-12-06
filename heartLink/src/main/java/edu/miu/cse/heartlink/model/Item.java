package edu.miu.cse.heartlink.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "items")
@NoArgsConstructor
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;//donar

    public Item(int i) {
        this.id = i;
    }
}
