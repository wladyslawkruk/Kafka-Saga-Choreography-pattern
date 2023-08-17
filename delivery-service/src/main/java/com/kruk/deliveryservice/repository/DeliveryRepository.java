package com.kruk.deliveryservice.repository;

import com.kruk.deliveryservice.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}
