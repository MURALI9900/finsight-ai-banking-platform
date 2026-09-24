package com.finsight.gateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
@Component
public class JwtGatewayFilter extends AbstractGatewayFilterFactory<JwtGatewayFilter.Config>{
 private final SecretKey signingKey;
 public JwtGatewayFilter(@Value("${finsight.jwt.secret}") String jwtSecret){
  super(Config.class); signingKey=Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
 }
 @Override public GatewayFilter apply(Config config){
  return (exchange,chain)->{
   String path=exchange.getRequest().getURI().getPath();
   if(path.startsWith("/api/v1/auth/")) return chain.filter(exchange);
   String header=exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
   if(header==null || !header.startsWith("Bearer ")) return unauthorized(exchange);
   try{
    Claims claims=Jwts.parser().verifyWith(signingKey).build()
     .parseSignedClaims(header.substring(7)).getPayload();
    String role=claims.get("role",String.class);
    if(!"BANK_OFFICER".equals(role)) return unauthorized(exchange);
    ServerWebExchange authenticated=exchange.mutate().request(exchange.getRequest().mutate()
     .header("X-FinSight-Officer",claims.getSubject()).header("X-FinSight-Role",role).build()).build();
    return chain.filter(authenticated);
   }catch(Exception ex){return unauthorized(exchange);}
  };
 }
 private Mono<Void> unauthorized(ServerWebExchange exchange){
  exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); return exchange.getResponse().setComplete();
 }
 public static class Config{}
}