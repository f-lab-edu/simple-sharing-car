package com.flab.simplesharingcar.entity;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.domain.Payment;
import com.flab.simplesharingcar.domain.Reservation;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.repository.PaymentRepository;
import com.flab.simplesharingcar.repository.ReservationRepository;
import com.flab.simplesharingcar.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.flab.simplesharingcar.constants.SessionKey.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BaseEntityTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @PersistenceContext
    EntityManager em;

    MockHttpServletRequest servletRequest;
    MockHttpSession session;
    User loginUser;

    @BeforeEach
    void setUp() {
        loginUser = new User("auditing@test.com", "1234", "TestUser");
        userRepository.save(loginUser);

        session = new MockHttpSession();
        session.setAttribute(LOGIN_USER, loginUser);

        servletRequest = new MockHttpServletRequest();
        servletRequest.setSession(session);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

    }

    @Test
    @Transactional
    void auditingTest() {
        // given
        Payment payment = new Payment(1);
        Reservation reservation = Reservation.builder()
                .status(CarReservationStatus.RESERVED)
                .payment(payment)
                .build();
        // when
        paymentRepository.save(payment);
        reservationRepository.save(reservation); // insert
        reservation.cancel(); // update
        em.flush();
        em.clear();
        // then
        Reservation findReservation = reservationRepository.findById(reservation.getId()).get();

        LocalDateTime createdDate = findReservation.getCreatedDate().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime updatedDate = findReservation.getUpdatedDate().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        Long createdBy = findReservation.getCreatedBy();
        Long updatedBy = findReservation.getUpdatedBy();
        Long loginId = loginUser.getId();

        assertThat(createdDate).isEqualTo(now);
        assertThat(updatedDate).isEqualTo(now);

        assertThat(createdBy).isEqualTo(loginId);
        assertThat(updatedBy).isEqualTo(loginId);

    }

}