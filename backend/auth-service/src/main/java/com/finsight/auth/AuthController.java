package com.finsight.auth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
 private final String jwtSecret;
 private final long expirationSeconds;
 public AuthController(@Value("${finsight.jwt.secret}") String jwtSecret,
                       @Value("${finsight.jwt.expiration-seconds:3600}") long expirationSeconds){
  this.jwtSecret=jwtSecret; this.expirationSeconds=expirationSeconds;
 }
 @PostMapping("/login")
 public LoginResponse login(@RequestBody LoginRequest request){
  if(!"ops.officer".equals(request.username()) || !"Demo@123".equals(request.password()))
   throw new UnauthorizedException("Invalid officer credentials");
  Instant now=Instant.now();
  String token=Jwts.builder().subject(request.username()).claim("role","BANK_OFFICER")
   .issuedAt(java.util.Date.from(now))
   .expiration(java.util.Date.from(now.plusSeconds(expirationSeconds)))
   .signWith(signingKey()).compact();
  return new LoginResponse(token,"Bearer",expirationSeconds);
 }
 private SecretKey signingKey(){return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));}
 public record LoginRequest(String username,String password){}
 public record LoginResponse(String accessToken,String tokenType,long expiresInSeconds){}
 @ResponseStatus(HttpStatus.UNAUTHORIZED)
 public static class UnauthorizedException extends RuntimeException{
  public UnauthorizedException(String message){super(message);}
 }
}