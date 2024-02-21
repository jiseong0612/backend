package org.gallery.backend.repository;

import org.gallery.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	
	Member findByEmailAndPassword(String email, String password);
}