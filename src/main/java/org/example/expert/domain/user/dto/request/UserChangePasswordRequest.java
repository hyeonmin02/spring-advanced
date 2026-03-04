package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    // oldPassword는 이미 검증완료된 비밀번호이니 Size와 Pattern이 불필요하다고 판단
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8,message = "비밀번호는 8자 이상이어야합니다.")
    @Pattern(regexp = "(?=.*\\d)(?=.*[A-Z]).*", message = "비밀번호는 숫자와 대문자를 포함해야 합니다.")
    private String newPassword;
}
