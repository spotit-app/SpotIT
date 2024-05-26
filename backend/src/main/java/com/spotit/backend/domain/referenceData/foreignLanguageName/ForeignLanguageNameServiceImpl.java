package com.spotit.backend.domain.referenceData.foreignLanguageName;

import static com.spotit.backend.storage.StorageServiceImpl.getFilenameFromUrl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.spotit.backend.domain.referenceData.abstraction.AbstractReferenceDataServiceImpl;
import com.spotit.backend.storage.StorageService;

@Service
public class ForeignLanguageNameServiceImpl
        extends AbstractReferenceDataServiceImpl<ForeignLanguageName, Integer>
        implements ForeignLanguageNameService {

    private static final String FLAGS_DIRECTORY_NAME = "flags";

    private final ForeignLanguageNameRepository foreignLanguageNameRepository;
    private final StorageService storageService;

    public ForeignLanguageNameServiceImpl(
            ForeignLanguageNameRepository foreignLanguageNameRepository,
            StorageService storageService) {
        super(foreignLanguageNameRepository);
        this.foreignLanguageNameRepository = foreignLanguageNameRepository;
        this.storageService = storageService;
    }

    @Override
    @Cacheable(key = "'all'")
    public List<ForeignLanguageName> getAll() {
        return foreignLanguageNameRepository.findAll();
    }

    @Override
    @Caching(evict = @CacheEvict(key = "'all'"), put = @CachePut(key = "#result.id"))
    public ForeignLanguageName create(String name, byte[] flag) {
        String flagUrl = storageService.uploadFile(
                flag,
                FLAGS_DIRECTORY_NAME,
                name + "-flag");

        ForeignLanguageName foreignLanguageNameToCreate = ForeignLanguageName.builder()
                .name(name)
                .flagUrl(flagUrl)
                .build();

        return create(foreignLanguageNameToCreate);
    }

    @Override
    @Caching(evict = @CacheEvict(key = "'all'"), put = @CachePut(key = "#result.id"))
    public ForeignLanguageName update(Integer id, String name, byte[] flag) {
        ForeignLanguageName foundForeignLanguageName = getById(id);

        if (name != null) {
            foundForeignLanguageName.setName(name);
        }

        if (flag != null) {
            String oldFlagFileName = getFilenameFromUrl(foundForeignLanguageName.getFlagUrl());

            String newFlagUrl = storageService.uploadFile(
                    flag,
                    FLAGS_DIRECTORY_NAME,
                    foundForeignLanguageName.getName() + "-flag");

            foundForeignLanguageName.setFlagUrl(newFlagUrl);

            storageService.deleteFile(oldFlagFileName);
        }

        return foreignLanguageNameRepository.save(foundForeignLanguageName);
    }

    @Override
    @CacheEvict(key = "{#id, 'all'}")
    public void delete(Integer id) {
        ForeignLanguageName foreignLanguageNameToDelete = getById(id);

        storageService.deleteFile(getFilenameFromUrl(foreignLanguageNameToDelete.getFlagUrl()));

        foreignLanguageNameRepository.delete(foreignLanguageNameToDelete);
    }
}
