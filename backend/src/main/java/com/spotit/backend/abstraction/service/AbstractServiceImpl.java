package com.spotit.backend.abstraction.service;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.exception.EntityNotFoundException;
import com.spotit.backend.abstraction.exception.ErrorCreatingEntityException;
import com.spotit.backend.abstraction.model.AbstractEntity;

import jakarta.transaction.Transactional;

@Service
public abstract class AbstractServiceImpl<E extends AbstractEntity, ID extends Serializable>
        implements AbstractService<E, ID> {

    protected final JpaRepository<E, ID> repository;

    public AbstractServiceImpl(JpaRepository<E, ID> repository) {
        this.repository = repository;
    }

    @Override
    public E create(E entity) {
        try {
            return repository.save(entity);
        } catch (Exception err) {
            throw new ErrorCreatingEntityException();
        }
    }

    @Override
    public E getById(ID id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public E update(ID id, E entity) {
        E foundEntity = getById(id);
        foundEntity.update(entity);

        return repository.save(foundEntity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        E entityToDelete = getById(id);
        repository.delete(entityToDelete);
    }
}
