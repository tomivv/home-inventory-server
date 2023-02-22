package com.github.tomivv.homeinventoryserver.item;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ItemController {
    
    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/items")
    public ResponseEntity<?> getItems() {
        try {
            return new ResponseEntity<>(itemRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error getting items: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<?> getItemById(@PathVariable String id) {
        Optional<Item> item = itemRepository.findById(id);
        if(!item.isPresent()) {
            return new ResponseEntity<>("No item with id: " + id, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        }
    }

    @PostMapping("/item")
    public ResponseEntity<?> createItem(@RequestBody Item item) {
    try {
        Item savedItem = itemRepository.save(item);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>("Error creating item: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @DeleteMapping("/item/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable String id) {
        Optional<Item> item = itemRepository.findById(id);
        if(!item.isPresent()) {
            return new ResponseEntity<>("No item with id: " + id, HttpStatus.NOT_FOUND);
        }
        try {
            itemRepository.delete(item.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting item: "  + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<?> updateItem(@PathVariable String id, @RequestBody Item updatedItem) {
        Optional<Item> item = itemRepository.findById(id);
        if (!item.isPresent()) {
            return new ResponseEntity<>("No item with id: " + id, HttpStatus.NOT_FOUND);
        }
        try {
            updatedItem.setId(id);
            Item savedItem = itemRepository.save(updatedItem);
            return new ResponseEntity<>(savedItem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating item: "  + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
