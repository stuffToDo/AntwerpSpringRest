package com.gobstoppers.antwerp.rest.rest.assembler;

import com.gobstoppers.antwerp.rest.model.Portfolio;
import com.gobstoppers.antwerp.rest.rest.PortfolioController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PortfolioModelAssembler implements RepresentationModelAssembler<Portfolio, EntityModel<Portfolio>> {

    @Override
    public EntityModel<Portfolio> toModel(Portfolio entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PortfolioController.class).one(entity.getUuid())).withSelfRel(),
                linkTo(methodOn(PortfolioController.class).all()).withRel("portfolios"));
    }

    @Override
    public CollectionModel<EntityModel<Portfolio>> toCollectionModel(Iterable<? extends Portfolio> entities) {
        Collection<EntityModel<Portfolio>> c = StreamSupport.stream(entities.spliterator(), false)
                .map(p -> EntityModel.of((Portfolio)p, linkTo(methodOn(PortfolioController.class).one(p.getUuid())).withSelfRel()))
                .collect(Collectors.toList());
        return CollectionModel.of(c, linkTo(methodOn(PortfolioController.class).all()).withSelfRel());
    }
}
