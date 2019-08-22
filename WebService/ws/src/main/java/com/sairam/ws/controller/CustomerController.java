package com.sairam.ws.controller;

import com.sairam.ws.entity.Customers;
import com.sairam.ws.repository.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomersRepository repository;

    @GetMapping("/")
    public ResponseEntity<Object> getAllCustomers() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomer(@PathVariable Integer id) {
        Optional<Customers> customer = repository.findById(id);
        return customer
                .<ResponseEntity<Object>>map(value -> new ResponseEntity<>(value, HttpStatus.FOUND))
                .orElseGet(() -> new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    public ResponseEntity<Object> createCustomer(@RequestBody Customers customer) {
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(null);
        Customers savedEntity = repository.save(customer);
        if (savedEntity == null) {
            return new ResponseEntity<>("Error occured while creating customer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Integer id, @RequestBody Customers customer) {
        if (id == null) {
            return new ResponseEntity<>("Please specify customer id", HttpStatus.BAD_REQUEST);
        }
        Optional<Customers> existingCustomer = repository.findById(id);
        if (existingCustomer.isPresent()) {
            Customers newCustomer = existingCustomer.get();
            newCustomer.setEmail(customer.getEmail());
            newCustomer.setFirstName(customer.getFirstName());
            newCustomer.setLastName(customer.getLastName());
            newCustomer.setIp(customer.getIp());
            newCustomer.setLatitude(customer.getLatitude());
            newCustomer.setLongitude(customer.getLongitude());
            newCustomer.setUpdatedAt(LocalDateTime.now());
            Customers savedCustomer = repository.save(newCustomer);
            if (savedCustomer == null) {
                return new ResponseEntity<>("Error occurred while updating the customer", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
        }
        return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> partialUpdateCustomer(@PathVariable Integer id, @RequestBody Customers customer) {
        if (id == null) {
            return new ResponseEntity<>("Please specify customer id", HttpStatus.BAD_REQUEST);
        }
        Optional<Customers> existingCustomer = repository.findById(id);
        if (existingCustomer.isPresent()) {
            Customers newCustomer = existingCustomer.get();
            if (customer.getFirstName() != null && !newCustomer.getFirstName().equals(customer.getFirstName())) {
                newCustomer.setFirstName(customer.getFirstName());
            }
            if (customer.getLastName() != null && !newCustomer.getLastName().equals(customer.getLastName())) {
                newCustomer.setLastName(customer.getLastName());
            }
            if (customer.getIp() != null && !newCustomer.getIp().equals(customer.getIp())) {
                newCustomer.setIp(customer.getIp());
            }
            if (customer.getLatitude() != null && !newCustomer.getLatitude().equals(customer.getLatitude())) {
                newCustomer.setLatitude(customer.getLatitude());
            }
            if (customer.getLongitude() != null && !newCustomer.getLongitude().equals(customer.getLongitude())) {
                newCustomer.setLongitude(customer.getLongitude());
            }
            newCustomer.setUpdatedAt(LocalDateTime.now());
            Customers savedCustomer = repository.save(newCustomer);
            if (savedCustomer == null) {
                return new ResponseEntity<>("Error occurred while updating the customer", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
        }
        return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>("Please specify customer id", HttpStatus.BAD_REQUEST);
        }
        Optional<Customers> existingCustomer = repository.findById(id);
        if (existingCustomer.isPresent()) {
            repository.delete(existingCustomer.get());
            return new ResponseEntity<>("Customer deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
    }
}
