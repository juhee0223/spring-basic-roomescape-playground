package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.LoginMember;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final LoginMemberProvider loginMemberProvider;

    public AdminInterceptor(LoginMemberProvider loginMemberProvider) {
        this.loginMemberProvider = loginMemberProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LoginMember member = loginMemberProvider.getLoginMember(request);

        if (!member.getRole().equals("ADMIN")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

}
