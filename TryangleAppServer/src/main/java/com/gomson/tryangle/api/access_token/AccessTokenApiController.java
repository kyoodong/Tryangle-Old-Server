package com.gomson.tryangle.api.access_token;

import com.gomson.tryangle.domain.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/access-token")
public class AccessTokenApiController {

    @Autowired
    private AccessTokenService accessTokenService;

    @GetMapping
    private AccessToken issueToken(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return accessTokenService.issueToken(ip);
    }

}
