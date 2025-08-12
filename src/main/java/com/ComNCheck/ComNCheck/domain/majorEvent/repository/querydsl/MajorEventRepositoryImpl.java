package com.ComNCheck.ComNCheck.domain.majorEvent.repository.querydsl;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.MajorEvent;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.QMajorEvent;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums.EventType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MajorEventRepositoryImpl implements MajorEventRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final QMajorEvent majorEvent = QMajorEvent.majorEvent;

    @Override
    public List<MajorEvent> search(Integer year, List<EventType> eventTypes) {
        return queryFactory
                .selectFrom(majorEvent)
                .where(
                        yearEq(year),
                        eventTypeIn(eventTypes)
                )
                .fetch();
    }

    private BooleanExpression yearEq(Integer year) {
        return year != null ? majorEvent.year.eq(year) : null;
    }

    private BooleanExpression eventTypeIn(List<EventType> eventTypes) {
        return (eventTypes != null && !eventTypes.isEmpty()) ? majorEvent.category.in(eventTypes) : null;
    }
}