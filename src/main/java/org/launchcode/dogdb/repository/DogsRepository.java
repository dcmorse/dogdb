package org.launchcode.dogdb.repository;

import org.launchcode.dogdb.domain.Dogs;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Dogs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DogsRepository extends JpaRepository<Dogs, Long> {

}
