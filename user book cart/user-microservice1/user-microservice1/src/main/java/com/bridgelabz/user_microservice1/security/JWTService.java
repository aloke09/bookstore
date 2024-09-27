package com.bridgelabz.user_microservice1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService
{
    private String secretKey="";

    public JWTService()
    {
        try
        {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey= Base64.getEncoder().encodeToString(sk.getEncoded());
            System.out.println("inside jwt service constructor key generated through HMACSHA256 and encoded");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String name)
    {
        System.out.println("inside jwt service generate token method");
        Map<String,Object> claim =new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claim)
                .subject(name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+6000000))
                .and()
                .signWith(getKey())
                .compact();

//        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImNoYW5kdSIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNTE2MjM5MTIyfQ.8wyDrP8X1DFu7BBRjFTkg9-6Fix4aREw28m4Q-0oO5Y";
    }

    private SecretKey getKey()
    {

        byte[] decode = Decoders.BASE64.decode(secretKey);
        System.out.println("inside jwt service getkey method");
        return Keys.hmacShaKeyFor(decode);

    }


    public String extractUserName(String token)
    {
        System.out.println("inside jwt service extract username method");
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails)
    {
        System.out.println("inside jwt service validate token method");
        final String userName = extractUserName(token);
        System.out.println(userName);

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
