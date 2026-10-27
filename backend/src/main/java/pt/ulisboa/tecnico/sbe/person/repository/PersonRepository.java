package pt.ulisboa.tecnico.sbe.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.sbe.person.domain.Person;

// Database access. JpaRepository already gives findAll, findById, save, delete, ...
// Extra queries can be declared by name (Spring generates them) or with @Query.
@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person, Integer> {
    boolean existsByEmail(String email);
}
