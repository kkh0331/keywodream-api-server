package pda.keywordream.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NicknameReqDto {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

}
