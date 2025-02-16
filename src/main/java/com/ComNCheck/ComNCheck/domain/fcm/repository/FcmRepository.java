package com.ComNCheck.ComNCheck.domain.fcm.repository;

import com.ComNCheck.ComNCheck.domain.fcm.model.entity.FcmToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByToken(String token);
}
