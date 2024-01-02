package com.spotit.backend.employee.referenceData.techSkillName;

import static com.spotit.backend.storage.StorageServiceImpl.getFilenameFromUrl;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.spotit.backend.employee.referenceData.abstraction.AbstractReferenceDataServiceImpl;
import com.spotit.backend.storage.StorageService;

@Service
public class TechSkillNameServiceImpl
        extends AbstractReferenceDataServiceImpl<TechSkillName, Integer>
        implements TechSkillNameService {

    private final TechSkillNameRepository techSkillNameRepository;
    private final StorageService storageService;

    private static final String LOGOS_DIRECTORY_NAME = "logos";

    public TechSkillNameServiceImpl(
            TechSkillNameRepository techSkillNameRepository,
            StorageService storageService) {
        super(techSkillNameRepository);
        this.techSkillNameRepository = techSkillNameRepository;
        this.storageService = storageService;
    }

    @Override
    @Cacheable(key = "'all'")
    public List<TechSkillName> getAll() {
        return techSkillNameRepository.findByCustom(false);
    }

    @Override
    public Optional<TechSkillName> getByName(String name) {
        return techSkillNameRepository.findByName(name);
    }

    @Override
    @Caching(evict = @CacheEvict(key = "'all'"), put = @CachePut(key = "#result.id"))
    public TechSkillName create(String name, byte[] logo) {
        String logoUrl = storageService.uploadFile(
                logo,
                LOGOS_DIRECTORY_NAME,
                name + "-logo");

        TechSkillName techSkillNameToCreate = TechSkillName.builder()
                .name(name)
                .logoUrl(logoUrl)
                .custom(false).build();

        return create(techSkillNameToCreate);
    }

    @Override
    @Caching(evict = @CacheEvict(key = "'all'"), put = @CachePut(key = "#result.id"))
    public TechSkillName update(Integer id, String name, byte[] logo) {
        TechSkillName foundTechSkillName = getById(id);

        if (name != null) {
            foundTechSkillName.setName(name);
        }

        if (logo != null) {
            storageService.uploadFile(
                    logo,
                    LOGOS_DIRECTORY_NAME,
                    getFilenameFromUrl(foundTechSkillName.getLogoUrl()));
        }

        return techSkillNameRepository.save(foundTechSkillName);
    }

    @Override
    @CacheEvict(key = "{#id, 'all'}")
    public void delete(Integer id) {
        TechSkillName techSkillNameToDelete = getById(id);

        storageService.deleteFile(getFilenameFromUrl(techSkillNameToDelete.getLogoUrl()));

        techSkillNameRepository.delete(techSkillNameToDelete);
    }
}
