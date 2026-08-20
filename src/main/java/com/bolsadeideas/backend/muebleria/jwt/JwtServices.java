package com.bolsadeideas.backend.muebleria.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtServices {
	
	private final String secretKey;
	private final long jwtExpiration;
	
	public JwtServices(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") long jwtExpiration) {
		this.secretKey = secretKey;
		this.jwtExpiration = jwtExpiration;
	}
	
	public String getToken(UserDetails user) {
		return getToken(new HashMap<>(), user);
	}

	//Metodo que genera el token JWT
	private String getToken(Map<String, Object> extraClaim, UserDetails user) {
		return Jwts.builder()
				.setClaims(extraClaim)
				.setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	//decodifica y devuelve la clave
	private Key getKey() {
		//Decodificamos base 64 nuestra llave secreta
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		
		//creamos nueva instancia de nuestra secret Key
		return Keys.hmacShaKeyFor(keyBytes);
		//return SECRET_KEY;
	}

	//Extrae el nombre de usuario del token
	public String getUsernameFromToken(String token) {
		
		return getClaim(token,Claims::getSubject);
	
	}

	//verifica si el token es valido y coincide con el nombre de usuario
	public boolean isTokenValid(String token, UserDetails userDetails) {
		
		final String username= getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
		
	}
	
	//extrae una reclamacion espesifica
	private Claims getAllClaims(String token) {
		
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		
	}
	
	//Desglosa el token para optener todas las reclamaciones(claims)
	public <T> T getClaim(String token, Function<Claims,T> claimResolver) {
		
		final Claims claims = getAllClaims(token);
		return claimResolver.apply(claims);
		
	}
	
	//evaluar si el token es valido con la fecha de expiracion
	private Date getExpiration(String token) {
		
		return getClaim(token, Claims::getExpiration);
		
	}
	
	private boolean isTokenExpired(String token) {
		
		return getExpiration(token).before(new Date());
	}
	
}
