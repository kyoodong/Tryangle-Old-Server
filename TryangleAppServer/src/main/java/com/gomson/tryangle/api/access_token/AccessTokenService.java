package com.gomson.tryangle.api.access_token;

import com.gomson.tryangle.dao.AccessTokenDao;
import com.gomson.tryangle.domain.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccessTokenService {

    private static final int TOKEN_EXPIRED_HOUR = 3;

    @Autowired
    private AccessTokenDao accessTokenDao;


    AccessToken issueToken(String ip) {
        AccessToken accessToken = new AccessToken(0, "",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(TOKEN_EXPIRED_HOUR),
                0, ip);

        accessTokenDao.insertToken(accessToken);

        AccessToken newAccessToken = accessTokenDao.selectToken(accessToken.getId());

        if (newAccessToken.getId() == accessToken.getId())
            return newAccessToken;

        return null;
    }
}
