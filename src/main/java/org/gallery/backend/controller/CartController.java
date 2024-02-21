package org.gallery.backend.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.gallery.backend.entity.Cart;
import org.gallery.backend.entity.Item;
import org.gallery.backend.repository.CartRepository;
import org.gallery.backend.repository.ItemRepository;
import org.gallery.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CartController {
	@Autowired
	JwtService jwtService;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@GetMapping("/api/cart/items")
	public ResponseEntity getCartItems(@CookieValue(value = "token", required = false) String token) {
		if(!jwtService.isValid(token)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		int memberId = jwtService.getid(token);
		List<Cart> carts = cartRepository.findByMemberId(memberId);
		List<Integer> itemIds = carts.stream().map(Cart::getItemId).collect(Collectors.toList());
		List<Item> items = itemRepository.findByIdIn(itemIds);
		
		
		return new ResponseEntity(items, HttpStatus.OK);
	}
	
	@PostMapping("/api/cart/items/{itemId}")
	public ResponseEntity pushCartItem(
			@PathVariable int itemId, 
			@CookieValue(value = "token", required = false) String token){
		
		if(!jwtService.isValid(token)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		int memberId = jwtService.getid(token);
		Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);
		
		if(cart == null) {
			Cart newCart = new Cart();
			newCart.setMemberId(memberId);
			newCart.setItemId(itemId);
			cartRepository.save(newCart);
		}
		
		return new ResponseEntity(HttpStatus.OK);
	}

}
