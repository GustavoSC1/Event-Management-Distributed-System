package com.gustavo.eventservice.repositories;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.eventservice.entities.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {
	
	@Query("select obj from Event obj where (upper(obj.name) LIKE '%' || upper(:search) || '%' OR "
			+ "upper(obj.description) LIKE '%' || upper(:search) || '%') AND "
			+ "(upper(obj.place) LIKE '%' || upper(:place) || '%') AND "
			+ "(obj.price BETWEEN :minPrice AND :maxPrice) AND "
			+ "(:date BETWEEN obj.startDateTime AND obj.endDateTime)")
	Page<Event> findByParams(@Param("search") String search, @Param("place") String place,@Param("minPrice") Double minPrice, 
			@Param("maxPrice") Double maxPrice, @Param("date") LocalDateTime date, Pageable pageable);
		
	@Transactional(readOnly = true)
	Page<Event> findByStaffUsersUserId(UUID userId, Pageable pageable);
	
}
