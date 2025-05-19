package com.ComNCheck.ComNCheck.domain.anotherEvent.repository;

import com.ComNCheck.ComNCheck.domain.anotherEvent.model.entity.AnotherEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnotherEventRepository extends JpaRepository<AnotherEvent, Long> {
}
