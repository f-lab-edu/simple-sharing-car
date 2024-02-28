package com.flab.simplesharingcar.config;

import com.flab.simplesharingcar.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static com.flab.simplesharingcar.constants.SessionKey.LOGIN_USER;

public class SessionLoginAuditor implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        Long loginUserId = loginUser.getId();
        return Optional.of(loginUserId);
    }

}
