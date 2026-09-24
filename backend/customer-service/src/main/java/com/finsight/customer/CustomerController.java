package com.finsight.customer;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
 private final List<Customer> customers=List.of(
  new Customer("C1001","Arun Kumar","XXXX-1001","RETAIL","Bengaluru","LOW","ACTIVE"),
  new Customer("C1002","Priya Sharma","XXXX-1002","PREMIUM","Hyderabad","MEDIUM","ACTIVE"),
  new Customer("C1003","Rahul Verma","XXXX-1003","RETAIL","Mumbai","LOW","ACTIVE"),
  new Customer("C1004","Sara Joseph","XXXX-1004","SME","Chennai","HIGH","ACTIVE")
 );
 @GetMapping public List<Customer> all(){return customers;}
 @GetMapping("/{id}") public Customer byId(@PathVariable String id){
  return customers.stream().filter(c->c.customerId().equalsIgnoreCase(id)).findFirst()
   .orElseThrow(()->new NoSuchElementException("Customer not found: "+id));
 }
 @GetMapping("/search") public List<Customer> search(@RequestParam String name){
  return customers.stream().filter(c->c.fullName().toLowerCase().contains(name.toLowerCase())).toList();
 }
}