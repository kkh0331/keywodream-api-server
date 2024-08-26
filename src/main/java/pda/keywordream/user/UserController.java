package pda.keywordream.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pda.keywordream.user.dto.NicknameReqDto;
import pda.keywordream.user.dto.NicknameResDto;
import pda.keywordream.user.entity.User;
import pda.keywordream.user.service.UserService;
import pda.keywordream.utils.ApiUtils;
import pda.keywordream.utils.ApiUtils.ApiResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResult<String>> login(@Valid @RequestBody NicknameReqDto reqDto){
        User user = userService.login(reqDto.getNickname());
        String userToken = userService.crateToken(user.getId());
        return ResponseEntity.ok()
                .header("accessToken", userToken)
                .body(ApiUtils.success("로그인 성공"));
    }

    @GetMapping("/nickname")
    public ResponseEntity<ApiResult<NicknameResDto>> getNickname(@RequestHeader("accessToken") String token){
        User user = userService.getUser(token);
        NicknameResDto nicknameResDto = NicknameResDto.builder().nickname(user.getNickname()).build();
        return ResponseEntity.ok(ApiUtils.success(nicknameResDto));
    }

}
