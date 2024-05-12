package com.spotit.backend.domain.referenceData.abstraction;

import java.io.Serializable;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spotit.backend.abstraction.AbstractEntity;
import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;

public abstract class AbstractReferenceDataServiceImpl<E extends AbstractEntity, ID extends Serializable>
        implements AbstractReferenceDataService<E, ID> {

    protected JpaRepository<E, ID> repository;

    public AbstractReferenceDataServiceImpl(JpaRepository<E, ID> repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(key = "#id")
    public E getById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    @Caching(evict = @CacheEvict(key = "'all'"), put = @CachePut(key = "#result.id"))
    public E create(E entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new ErrorCreatingEntityException();
        }
    }

    @Override
    @Caching(evict = @CacheEvict(key = "'all'"), put = @CachePut(key = "#result.id"))
    public E update(ID id, E entity) {
        E entityToUpdate = getById(id);

        entityToUpdate.update(entity);

        return repository.save(entityToUpdate);
    }

    @Override
    @CacheEvict(key = "{#id, 'all'}")
    public void delete(ID id) {
        E entityToDelete = getById(id);

        repository.delete(entityToDelete);
    }
}
