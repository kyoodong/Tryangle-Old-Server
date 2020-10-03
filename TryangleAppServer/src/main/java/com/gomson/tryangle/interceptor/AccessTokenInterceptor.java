package com.gomson.tryangle.interceptor;

import com.gomson.tryangle.api.access_token.AccessTokenService;
import com.gomson.tryangle.dao.AccessTokenDao;
import com.gomson.tryangle.domain.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class AccessTokenInterceptor implements HandlerInterceptor {

    private static final int MAX_ACCESS_COUNT = 1000;

    @Autowired
    private AccessTokenDao accessTokenDao;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter("token");
        if (token == null) {
            throw new AccessDeniedException("토큰정보가 없습니다.");
        }

        AccessToken accessToken = accessTokenDao.selectAccessTokenByToken(token);

        if (accessToken == null) {
            throw new AccessDeniedException("존재하지 않는 토큰입니다.");
        }

        // 만기 시간 지남 or 최대 접속 횟수 초과 or 등록 IP와 다른 IP에서 접속
        if (LocalDateTime.now().isAfter(accessToken.getExpiredAt()) ||
                accessToken.getAccessCount() > MAX_ACCESS_COUNT ||
                !accessToken.getIp().equals(request.getRemoteAddr())) {
            accessTokenDao.deleteAccessToken(accessToken.getId());
            throw new AccessDeniedException("유효하지 않은 토큰입니다.");
        }

        accessTokenDao.addAccessCount(accessToken.getId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion");
    }
}
