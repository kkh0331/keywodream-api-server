package pda.keywordream.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pda.keywordream.user.entity.User;
import pda.keywordream.user.repository.UserRepository;
import pda.keywordream.utils.jwt.JWTUtil;

import java.util.NoSuchElementException;
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

    public User getUser(String token) {
        String cleanedToken = token.replace("Bearer ", "");
        Long userId = jwtUtil.getId(cleanedToken);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 요청에 해당하는 사용자가 존재하지 않습니다."));
    }
}
