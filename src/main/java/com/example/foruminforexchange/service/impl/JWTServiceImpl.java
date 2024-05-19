package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {

    private static final String secretKey = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";

    private static final String JWT_BLACKLIST_PREFIX = "jwt_blacklist:";

    private final StringRedisTemplate redisTemplate;

    public JWTServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 240))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, Objects> extraClaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generatePasswordResetToken(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 180 * 1000)) //3 phut
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Lay username tu token
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigninKey(){
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) );
    }

    public boolean isTokenResetPasswordValid(String token){
        return (!isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public void blacklistToken(String token) {
        String jti = extractClaim(token, Claims::getId);
        long expirationSec = extractClaim(token, Claims::getExpiration).getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set(JWT_BLACKLIST_PREFIX + jti, "blacklisted", expirationSec, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        String jti = extractClaim(token, Claims::getId);
        return redisTemplate.hasKey(JWT_BLACKLIST_PREFIX + jti);
    }


}
