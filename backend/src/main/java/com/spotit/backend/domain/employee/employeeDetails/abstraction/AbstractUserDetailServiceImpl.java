package com.spotit.backend.domain.employee.employeeDetails.abstraction;

import java.io.Serializable;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountService;

import jakarta.transaction.Transactional;

@Service
public abstract class AbstractUserDetailServiceImpl<E extends AbstractUserDetailEntity, ID extends Serializable>
        implements AbstractUserDetailService<E, ID> {

    protected final AbstractUserDetailRepository<E, ID> repository;
    protected final UserAccountService userAccountService;

    public AbstractUserDetailServiceImpl(
            AbstractUserDetailRepository<E, ID> repository,
            UserAccountService userAccountService) {
        this.repository = repository;
        this.userAccountService = userAccountService;
    }

    @Override
    @Cacheable(key = "#userAccountAuth0Id")
    public List<E> getAllByUserAccountAuth0Id(String userAccountAuth0Id) {
        UserAccount userAccount = userAccountService.getByAuth0Id(userAccountAuth0Id);

        return repository.findByUserAccountAuth0Id(userAccount.getAuth0Id());
    }

    @Override
    @Cacheable(key = "#id")
    public E getById(ID id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    @Caching(evict = @CacheEvict(key = "#userAccountAuth0Id"), put = @CachePut(key = "#result.id"))
    public E create(String userAccountAuth0Id, E entity) {
        UserAccount userAccount = userAccountService.getByAuth0Id(userAccountAuth0Id);
        entity.setUserAccount(userAccount);

        try {
            return repository.save(entity);
        } catch (Exception err) {
            throw new ErrorCreatingEntityException();
        }
    }

    @Override
    @Caching(evict = @CacheEvict(key = "#result.userAccount.auth0Id"), put = @CachePut(key = "#id"))
    public E update(ID id, E entity) {
        E foundEntity = getById(id);
        foundEntity.update(entity);

        return repository.save(foundEntity);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(ID id) {
        E entityToDelete = getById(id);

        repository.delete(entityToDelete);
    }
}
