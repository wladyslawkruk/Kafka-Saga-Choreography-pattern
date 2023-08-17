package com.kruk.inventoryservice.repository;

import com.kruk.inventoryservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByName(String name);
}
