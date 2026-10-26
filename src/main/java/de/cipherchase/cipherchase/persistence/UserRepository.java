package de.cipherchase.cipherchase.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @version 1.0 - 2026-10-25
 * @author Manuel
 * 
 * 
*/
@Repository 
public interface UserRepository extends JpaRepository<User, Long> {

    
}
