package com.ComNCheck.ComNCheck.domain.majorEvent.repository;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.EventTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTipRepository extends JpaRepository<EventTip, Long> {
}
