package edu.miu.cse.heartlink.config;

import edu.miu.cse.heartlink.auth.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import static io.jsonwebtoken.Jwts.*;

@Service
@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${jjt.secret-key}")
    public String SECRET;

    private final TokenBlacklistService tokenBlacklistService;

    public String generateToken(UserDetails userDetails) {
        String token = builder()
                .issuedAt(new Date())
                .issuer("edu.miu")
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .subject(userDetails.getUsername())//identity
                .claim("authorities", populateAuthorities(userDetails))
                .signWith(signInKey())
                .compact();
        return token;
    }

    private SecretKey signInKey() {
        //Hash based message authentication code
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }

    public String populateAuthorities(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
//                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));
    }

    public Claims getClaims(String token) {
        return parser().verifyWith(signInKey()).build().parseSignedClaims(token).getPayload();
    }

    private Claims extractClaims(String token) {
        try {

            return Jwts.parser().setSigningKey(SECRET).build().parseSignedClaims(token).getBody();

        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    boolean isNotExpired = !isTokenExpired(token);
    boolean isNotBlacklisted = !tokenBlacklistService.isTokenBlacklisted(token); // Check blacklist
    return (username.equals(userDetails.getUsername()) && isNotExpired && isNotBlacklisted);
}


    private String extractUsername(String token) {
        return extractClaims(token).getSubject(); // "sub" is used for username in JWT claims
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

}
