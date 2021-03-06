package mekura.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mekura.api.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    /*@Query("select m.id from Misc m where m.name= :name")
       Misc findMiscByName(@Param("name")String name);*/
    Optional<User> findByUsername(String username);
}