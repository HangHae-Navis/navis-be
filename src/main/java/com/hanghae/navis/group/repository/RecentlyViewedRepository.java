package com.hanghae.navis.group.repository;

import com.hanghae.navis.group.entity.RecentlyViewed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewed, Long> {

}
