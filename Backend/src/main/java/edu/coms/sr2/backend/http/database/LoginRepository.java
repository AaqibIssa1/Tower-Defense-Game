package edu.coms.sr2.backend.http.database;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

/**
 * Interface for Spring/Hibernate to provide database functionality of our "profile_logins" table
 * @author Nathan
 *
 */

@Repository
public interface LoginRepository extends CrudRepository<ProfileLogin, String>
{
	Optional<ProfileLogin> findByEmail(String email);
}
