package pda.keywordream.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pda.keywordream.user.entity.User;
import pda.keywordream.user.repository.UserRepository;
import pda.keywordream.utils.jwt.JWTUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public User login(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        if(user.isEmpty()){
            User newUser = User.builder().nickname(nickname).build();
            return userRepository.save(newUser);
        }
        return user.get();
    }

    public String crateToken(Long userId) {
        String userToken = jwtUtil.createJwt(userId, 1000*60*60*24L);
        return "Bearer " + userToken;
    }
}
