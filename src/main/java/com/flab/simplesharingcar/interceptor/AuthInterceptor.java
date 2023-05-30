package com.flab.simplesharingcar.interceptor;

import com.flab.simplesharingcar.constants.SessionKey;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.web.exception.NotLoginException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        HttpSession session = request.getSession();
        User loginUser = null;
        if (session != null) {
            loginUser = (User) session.getAttribute(SessionKey.LOGIN_USER);
        }

        if (loginUser == null) {
            handlingNotLogin(response, handler);
            return false;
        }
        return true;
    }

    private void handlingNotLogin(HttpServletResponse response, Object handler)
        throws Exception {
        //...?
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Class<?> returnType = method.getReturnType();

        if (returnType.equals(ResponseEntity.class)) {
            throw new NotLoginException("로그인이 필요 합니다.");
        }
        response.sendRedirect("/users/login");
    }

}
