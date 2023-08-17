package com.kruk.paymentservice.repository;

import com.kruk.paymentservice.domain.UserBalance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class PaymentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepositoryJpa;

    @Test
    public void whenGetByName_thenReturnBalance(){
        UserBalance ub = new UserBalance(
                1L,
                "alex"
        );
        ub.setBalance(1000l);
        entityManager.persist(ub);
        entityManager.flush();

        String user = ub.getName();
        UserBalance gotUser = paymentRepositoryJpa.findByName(user).get();

        assertThat(gotUser.getId())
                .isEqualTo(ub.getId());

        assertThat(gotUser.getBalance())
                .isEqualTo(ub.getBalance());
    }
}
