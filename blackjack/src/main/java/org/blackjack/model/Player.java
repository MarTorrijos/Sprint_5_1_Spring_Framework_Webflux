package org.blackjack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Table("players")
public class Player {

    @Id
    private String id;
    private String name;  // 100 char, TODO validate this
    private int gamesWon;

}