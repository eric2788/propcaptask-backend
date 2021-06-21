package com.ericlam.propcaptask.service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;


@Service
public class JWTServiceImpl implements JWTService {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private final Set<String> whiteListTokens = new HashSet<>();

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws JwtException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws JwtException {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            if (expiration.before(new Date())) {
                this.whiteListTokens.remove(token);
            }
            return !this.whiteListTokens.contains(token);
        } catch (ExpiredJwtException e) {
            this.whiteListTokens.remove(token);
            return true;
        }
    }

    @Override
    public String generateToken(String username) {
        return generateToken(userDetailsService.loadUserByUsername(username));
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        this.whiteListTokens.removeIf(t -> {
            try{
                return userDetails.getUsername().equals(getUsernameFromToken(t));
            }catch (ExpiredJwtException e){
                return true;
            }
        });
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }


    private String doGenerateToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        this.whiteListTokens.add(token);
        return token;
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public boolean validateToken(String token, String username) {
        return validateToken(token, userDetailsService.loadUserByUsername(username));
    }

    @Override
    public void invalidateToken(String token) {
        this.whiteListTokens.remove(token);
    }
}
