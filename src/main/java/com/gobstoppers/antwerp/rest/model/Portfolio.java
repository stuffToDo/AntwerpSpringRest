package com.gobstoppers.antwerp.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.UUID;

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Portfolio {

    @Id
    private UUID uuid;

    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @SortNatural
    private Set<String> stocks;

    public Portfolio() {}

    public Portfolio(UUID uuid, Set<String> stocks) {
        this.uuid = uuid;
        this.stocks = stocks;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Portfolio setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Set<String> getStocks() {
        return stocks;
    }

    public void setStocks(Set<String> stocks) {
        this.stocks = stocks;
    }
}
