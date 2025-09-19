package com.ComNCheck.ComNCheck.domain.majorEvent.repository;

import com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.SuggestedEvent;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestedEventRepository extends JpaRepository<SuggestedEvent, Long> {
    List<SuggestedEvent> findTop5ByOrderByLikeCountDesc();

    Page<SuggestedEvent> findByProposer(Member proposer, Pageable pageable);

}
