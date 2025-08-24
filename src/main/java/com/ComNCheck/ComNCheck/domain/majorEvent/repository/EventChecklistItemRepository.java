package com.ComNCheck.ComNCheck.domain.majorEvent.repository;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.EventChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventChecklistItemRepository extends JpaRepository<EventChecklistItem, Long> {
}
