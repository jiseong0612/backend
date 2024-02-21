package org.gallery.backend.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.gallery.backend.entity.Member;
import org.gallery.backend.repository.MemberRepository;
import org.gallery.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;

@RestController
public class AccountController {
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	JwtService jwtService;

	@PostMapping("/api/account/login")
	public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res) {
		Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));

		if (member != null) {
			int id = member.getId();
			String token = jwtService.getToken("id", id); // 회원의 인덱스 넘버

			Cookie cookie = new Cookie("token", token);
			cookie.setHttpOnly(true);
			cookie.setPath("/");

			res.addCookie(cookie);

			return new ResponseEntity<>(id, HttpStatus.OK);
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}
	
    @PostMapping("/api/account/logout")
    public ResponseEntity logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        res.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

	@GetMapping("/api/account/check")
	public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
		Claims claims = jwtService.getClaims(token);

		if (claims != null) {
			int id = Integer.parseInt(claims.get("id").toString());
			return new ResponseEntity<>(id, HttpStatus.OK);
		}

		return new ResponseEntity<>(null, HttpStatus.OK);
		
	}

}
