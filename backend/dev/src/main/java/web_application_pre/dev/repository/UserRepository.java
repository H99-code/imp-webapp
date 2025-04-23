package web_application_pre.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web_application_pre.dev.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByPassword(String password);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.items WHERE u.id = :userId")
    Optional<User> findByIdWithItems(@Param("userId") String userId);
}
