package org.launchcode.dogdb.repository;

import org.launchcode.dogdb.domain.Cat;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Cat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {

    @Query("select cat from Cat cat where cat.user.login = ?#{principal.username}")
    List<Cat> findByUserIsCurrentUser();

}
