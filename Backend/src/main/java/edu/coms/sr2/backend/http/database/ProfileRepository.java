package edu.coms.sr2.backend.http.database;

import org.springframework.stereotype.Repository;

import org.springframework.data.repository.CrudRepository;

/**
 * Interface for Spring/Hibernate to provide database functionality for our "profiles" table
 * @author Nathan
 *
 */
@Repository
public interface ProfileRepository extends CrudRepository<Profile, Integer>
{
}
