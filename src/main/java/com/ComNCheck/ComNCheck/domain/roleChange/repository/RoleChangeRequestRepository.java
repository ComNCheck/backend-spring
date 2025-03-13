package com.ComNCheck.ComNCheck.domain.roleChange.repository;

import com.ComNCheck.ComNCheck.domain.roleChange.model.entity.RoleChange;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleChangeRequestRepository extends JpaRepository<RoleChange, Long> {

    @Query("SELECT r FROM RoleChange r ORDER BY r.requestId DESC")
    List<RoleChange> findAllOrderByIdDesc();
}
