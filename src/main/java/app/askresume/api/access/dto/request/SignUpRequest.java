package app.askresume.api.access.dto.request;

import app.askresume.domain.member.constant.MemberType;
import app.askresume.domain.member.constant.Role;
import app.askresume.domain.member.model.Member;
import app.askresume.global.util.SHA256Util;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record SignUpRequest(
    @Schema(description = "이메일", example = "member@domain.co.kr", required = true)
    @Email
    @NotBlank
    String email,

    @Schema(description = "비밀번호", example = "!Password123!", required = true)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "비밀번호는 대문자, 소문자, 숫자, 특수문자 중 하나 이상을 포함해야 합니다.")
    @Length(min = 8)
    @NotBlank
    String password,

    @Schema(description = "비밀번호 확인", example = "!Password123!", required = true)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "비밀번호는 대문자, 소문자, 숫자, 특수문자 중 하나 이상을 포함해야 합니다.")
    @Length(min = 8)
    @NotBlank
    String passwordCheck,

    @Schema(description = "회원 이름", example = "홍길동", required = true)
    @NotBlank
    @Size(min = 2, max = 20)
    String username,

    @URL(message = "올바른 URL 형식이 아닙니다.")
    @Schema(description = "프로필 이미지 경로", example = "https://domain.com/img_110x110.jpg")
    String profile,

    @NotBlank
    @Schema(description = "언어", example = "en")
    String locale

) {
    public SignUpRequest(String email, String password, String passwordCheck, String username, String profile, String locale) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
        this.profile = profile;
        this.locale = locale;
    }

    public Member toMemberEntity(MemberType memberType, Role role) {
        return Member.builder()
                .email(this.email)
                .password(SHA256Util.encrypt(this.password))
                .username( this.username)
                .profile(this.profile)
                .memberType(memberType)
                .locale(locale)
                .role(role)
                .build();
    }
}
