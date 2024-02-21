package org.gallery.backend.repository;

import java.util.List;

import org.gallery.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer>{

	List<Item> findByIdIn(List<Integer> itemIds);
}
