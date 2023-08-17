package com.kruk.inventoryservice.domain;

import com.kruk.inventoryservice.dto.ItemDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Data
@Table(name = "inventory")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "price")
    private Long price;

    @Column(name = "left_in_stock")
    private Long stock;

    public Item(String description, Long price,Long amount) {

        this.name = description;
        this.price = price;
        this.stock =0l;


    }

    public static Item toItem(ItemDto itemDto){
        return new Item(itemDto.getItemName(),
                itemDto.getPrice(), itemDto.getAmount());
    }

}
