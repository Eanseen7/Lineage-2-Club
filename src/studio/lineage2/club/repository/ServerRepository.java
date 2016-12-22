package studio.lineage2.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studio.lineage2.club.model.Server;

/**
 Obi-Wan
 23.07.2016
 */
@Repository public interface ServerRepository extends JpaRepository<Server, Long>
{}