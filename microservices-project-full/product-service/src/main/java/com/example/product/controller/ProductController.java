
package com.example.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

 @Autowired
 private ProductRepository repo;

 @PostMapping
 public Product create(@RequestBody Product p,
   @RequestHeader("X-USER-ID") String userId){

  p.setCreatedBy(Long.parseLong(userId));
  return repo.save(p);
 }

 @GetMapping
 public List<Product> all(){
  return repo.findAll();
 }

 @GetMapping("/{id}")
 public Product get(@PathVariable Long id){
  return repo.findById(id).orElseThrow();
 }

 @PutMapping("/{id}")
 public Product update(@PathVariable Long id,
   @RequestBody Product p){

  Product old = repo.findById(id).orElseThrow();

  old.setName(p.getName());
  old.setPrice(p.getPrice());
  old.setDescription(p.getDescription());

  return repo.save(old);
 }

 @DeleteMapping("/{id}")
 public void delete(@PathVariable Long id){
  repo.deleteById(id);
 }
}
