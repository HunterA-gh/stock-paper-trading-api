package com.c9.financeapi.repo;

import com.c9.financeapi.model.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, Long> {
}
