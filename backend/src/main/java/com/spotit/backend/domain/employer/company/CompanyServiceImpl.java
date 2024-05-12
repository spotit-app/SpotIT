package com.spotit.backend.domain.employer.company;

import static com.spotit.backend.storage.StorageServiceImpl.getFilenameFromUrl;

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
import com.spotit.backend.storage.StorageService;

import jakarta.transaction.Transactional;

@Service
public class CompanyServiceImpl implements CompanyService {

    protected final CompanyRepository repository;
    protected final UserAccountService userAccountService;
    protected final StorageService storageService;

    private static final String COMPANY_LOGOS_DIRECTORY_NAME = "companyLogos";

    public CompanyServiceImpl(
            CompanyRepository repository,
            UserAccountService userAccountService, StorageService storageService) {
        this.repository = repository;
        this.userAccountService = userAccountService;
        this.storageService = storageService;
    }

    @Override
    @Cacheable(key = "#userAccountAuth0Id")
    public List<Company> getAllByUserAccountAuth0Id(String userAccountAuth0Id) {
        UserAccount userAccount = userAccountService.getByAuth0Id(userAccountAuth0Id);

        return repository.findByUserAccountAuth0Id(userAccount.getAuth0Id());
    }

    @Override
    @Cacheable(key = "#id")
    public Company getById(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    @Caching(evict = @CacheEvict(key = "#userAccountAuth0Id"), put = @CachePut(key = "#result.id"))
    public Company create(String userAccountAuth0Id, Company company) {
        UserAccount userAccount = userAccountService.getByAuth0Id(userAccountAuth0Id);
        company.setUserAccount(userAccount);

        try {
            return repository.save(company);
        } catch (Exception err) {
            throw new ErrorCreatingEntityException();
        }
    }

    @Override
    @Caching(evict = @CacheEvict(key = "#userAccountAuth0Id"), put = @CachePut(key = "#result.id"))
    public Company create(String userAccountAuth0Id, Company company, byte[] companyLogo) {
        String companyLogoUrl = storageService.uploadFile(
                companyLogo,
                COMPANY_LOGOS_DIRECTORY_NAME,
                company.getId().toString());

        return create(userAccountAuth0Id, company, companyLogoUrl);
    }

    @Override
    @Caching(evict = @CacheEvict(key = "#userAccountAuth0Id"), put = @CachePut(key = "#result.id"))
    public Company create(String userAccountAuth0Id, Company company, String companyLogoUrl) {
        company.setLogoUrl(companyLogoUrl);

        return create(userAccountAuth0Id, company);
    }

    @Override
    @Caching(evict = @CacheEvict(key = "#result.userAccount.auth0Id"), put = @CachePut(key = "#id"))
    public Company update(Integer id, Company companyToUpdate, byte[] companyLogo) {
        Company foundCompany = getById(id);

        if (companyLogo != null) {
            String companyLogoUrl = storageService.uploadFile(
                    companyLogo,
                    COMPANY_LOGOS_DIRECTORY_NAME,
                    foundCompany.getId().toString() + System.currentTimeMillis());

            companyToUpdate.setLogoUrl(companyLogoUrl);

            try {
                storageService.deleteFile(getFilenameFromUrl(foundCompany.getLogoUrl()));
            } catch (Exception ignored) {
            }
        }

        foundCompany.update(companyToUpdate);

        return repository.save(foundCompany);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        Company companyToDelete = getById(id);

        storageService.deleteFile(companyToDelete.getId().toString());

        repository.delete(companyToDelete);
    }
}
