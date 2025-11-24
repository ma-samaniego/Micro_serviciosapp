package Iniciosecion.Iniciosecion.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {

    @Value("${jwt.secret:changeit12345}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:3600000}")
    private long jwtExpirationMs;

    public String generateToken(String username, Long rolId) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        return JWT.create()
                .withSubject(username)
                .withClaim("rol_id", rolId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        return JWT.require(algorithm).build().verify(token);
    }

}
