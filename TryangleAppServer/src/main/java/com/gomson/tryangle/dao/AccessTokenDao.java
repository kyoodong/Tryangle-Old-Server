package com.gomson.tryangle.dao;

import com.gomson.tryangle.domain.AccessToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccessTokenDao {

    void insertToken(AccessToken accessToken);

    AccessToken selectToken(long id);
    AccessToken selectAccessTokenByToken(String token);

    void addAccessCount(long id);

    void deleteAccessToken(long id);
}
