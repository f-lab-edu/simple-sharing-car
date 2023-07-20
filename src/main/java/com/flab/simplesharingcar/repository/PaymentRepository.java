package com.flab.simplesharingcar.repository;

import com.flab.simplesharingcar.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
