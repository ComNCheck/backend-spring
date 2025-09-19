package com.ComNCheck.ComNCheck.domain.majorEvent.repository;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.EventLike;
import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.SuggestedEvent;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
    Optional<EventLike> findByMemberAndSuggestedEvent(Member member, SuggestedEvent event);
    boolean existsByMemberAndSuggestedEvent(Member member, SuggestedEvent event);


    @Query("SELECT se.id FROM EventLike el JOIN el.suggestedEvent se WHERE el.member = :member AND se.id IN :eventIds")
    Set<Long> findLikedEventIdsByMemberAndEventIds(@Param("member") Member member, @Param("eventIds") List<Long> eventIds);
}
