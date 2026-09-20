package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.member.LoginMember;
import roomescape.member.MemberService;

@Component
public class LoginMemberProvider {
    private final CookieTokenExtractor cookieTokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginMemberProvider(CookieTokenExtractor cookieTokenExtractor, JwtTokenProvider jwtTokenProvider,
                               MemberService memberService) {
        this.cookieTokenExtractor = cookieTokenExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public LoginMember getLoginMember(HttpServletRequest request) {
        String token = cookieTokenExtractor.extract(request.getCookies());
        String email = jwtTokenProvider.getPayload(token);
        return memberService.findLoginMemberByEmail(email);
    }
}
