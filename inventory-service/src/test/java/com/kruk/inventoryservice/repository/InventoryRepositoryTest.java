package com.kruk.inventoryservice.repository;

import com.kruk.inventoryservice.domain.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class InventoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InventoryRepository inventoryRepositoryJpa;

    @Test
    public void whenGetByName_thenReturnItem(){
        Item it = new Item(
              "beer",
                50L,
                0L
        );
        it.setStock(10L);
        entityManager.persist(it);
        entityManager.flush();

        String name = it.getName();
        Item gotItem = inventoryRepositoryJpa.findByName(name).get();

        assertThat(gotItem.getId())
                .isEqualTo(it.getId());

        assertThat(gotItem.getStock())
                .isEqualTo(it.getStock());
    }
}
