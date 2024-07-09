package com.nttdata.db_demo.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service //to manage bean
public class JWTService {
	
	private static final String SECRET_KEY= "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b"; 

	public String extractUsername(String token) {
		//Now to extract details we need to install 3 dependencies of jwts -
		//1.jjwt-api
		//2.jjwt-impl
		//3.jjwt-jackson
		//what is jwt token ?
		//jwt stands for json web token , its a compact url safe representation of data/claims in 
		//that is to be transferred between two parties,
		//they are encoded as Json objects that are digitally signed using json web signature
		//has three parts
		//1.header - has two parts : type of token eg.jwt and signature algorithm
		//2.payload - data : contains claims , statements about entity typically user data and extra things
		//3.verify signature - used to verify the sender of the token , it is who it claims to be and the token was not altered
		return extractClaim(token,Claims::getSubject);
	}
	
	//to extract all the claims and one single claim
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);//Now this method can be called in getUsername to extract name from the subject / claims
		
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.setSigningKey(getSignInKey()) // a signing key is used to digitally sign a jwt token,creates the third part of jwt i.e the signature, we need a minim of 256 bits secret key
				.build()
				.parseClaimsJws(token)
				.getBody();
				
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	//generating token without extra claims
	public String generateToken(UserDetails userDetails) {
		return generateToken(userDetails,new HashMap<>());
	}
	
	//Now other methods jwt service class like token expiration date , testing if token is expired, generating the token etc
	
	//generate token method
	//will generate a jwt token using UserDetails - provided by spring security
	//need to set all the three parts data of jwt
	public String generateToken(UserDetails userDetails,Map<String,Object> extraClaims) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
				.signWith(getSignInKey(),SignatureAlgorithm.HS256)
				.compact();
	}
	
	//to check token validity
	public boolean isTokenValid(String token,UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()))&& !isTokenExpired(token);
	}

	//check token expiration
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	//to get the expiration date
	private Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
	}

}
