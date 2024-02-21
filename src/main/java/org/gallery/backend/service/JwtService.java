package org.gallery.backend.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
	String getToken(String key, Object value);
	
	Claims getClaims(String token);
	
	boolean isValid(String token);
	
	int getid(String token);
}
