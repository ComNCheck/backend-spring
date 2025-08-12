package com.ComNCheck.ComNCheck.domain.majorEvent.repository.querydsl;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.EventType;

import java.util.List;

public interface MajorEventRepositoryCustom {
    List<MajorEvent> search(Integer year, List<EventType> eventTypes);
}