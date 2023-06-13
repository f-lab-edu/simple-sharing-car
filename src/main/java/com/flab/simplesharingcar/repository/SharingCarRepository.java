package com.flab.simplesharingcar.repository;

import com.flab.simplesharingcar.domain.SharingCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharingCarRepository extends JpaRepository<SharingCar, Long>, SharingCarRepositorySupport {

}
