package com.kruk.paymentservice.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "balance")
public class UserBalance {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private Long balance;

    public UserBalance(Long id, String name) {
        this.id = id;
        this.name = name;
        this.balance = 0l;
    }
}
