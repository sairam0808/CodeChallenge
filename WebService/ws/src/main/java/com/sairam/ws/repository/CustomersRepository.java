package com.sairam.ws.repository;

import com.sairam.ws.entity.Customers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomersRepository extends CrudRepository<Customers, Integer> {
}
