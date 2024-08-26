package pda.keywordream.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pda.keywordream.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);
    boolean existsByNickname(String nickname);

}
