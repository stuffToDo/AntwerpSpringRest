package com.gobstoppers.antwerp.repository;

import com.gobstoppers.antwerp.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
}
