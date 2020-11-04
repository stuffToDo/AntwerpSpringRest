package com.gobstoppers.antwerp.rest;

import com.gobstoppers.antwerp.model.Portfolio;
import com.gobstoppers.antwerp.repository.PortfolioRepository;
import com.gobstoppers.antwerp.rest.assembler.PortfolioModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/portfolios", produces = "application/hal+json")
@ExposesResourceFor(Portfolio.class)
public class PortfolioController {

    @Autowired
    private PortfolioModelAssembler assembler;

    @Autowired
    private PortfolioRepository repo;

    @GetMapping("/{id}")
    public EntityModel<Portfolio> one(@PathVariable UUID id) {
        try {
            return assembler.toModel(repo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(id.toString()))
            );
        } catch(EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public CollectionModel<EntityModel<Portfolio>> all() {
        return assembler.toCollectionModel(repo.findAll());
    }

    @RequestMapping(path ="/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public EntityModel<Portfolio> save(@RequestBody @Valid Portfolio p, @PathVariable UUID id) {
        return assembler.toModel(repo.save(p.setUuid(id)));
    }

    @DeleteMapping(path ="/{id}")
    public EntityModel<UUID> delete(@PathVariable UUID id) {
        repo.deleteById(id);
        return EntityModel.of(id,linkTo(methodOn(PortfolioController.class).all())
                        .withRel("portfolios")
        );
    }
}
