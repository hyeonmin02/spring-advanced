package org.example.expert.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderTest {
    // PasswordEncoder는 외부 의존성이 없는데 불필요하게 @InjectMocks를 통해 가짜 객체를 불러와서 제거해 테스트 의도를 명확하게함
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    @Test
    void matches_메서드가_정상적으로_동작한다() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when (PasswordEncoder 클래스 내의 파라미터 순서와 달랐음)
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertTrue(matches);
    }
}
