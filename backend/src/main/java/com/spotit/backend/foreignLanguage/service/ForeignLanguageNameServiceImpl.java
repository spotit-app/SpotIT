package com.spotit.backend.foreignLanguage.service;

import static com.spotit.backend.storage.service.StorageServiceImpl.getFilenameFromUrl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.service.AbstractServiceImpl;
import com.spotit.backend.foreignLanguage.model.ForeignLanguageName;
import com.spotit.backend.foreignLanguage.repository.ForeignLanguageNameRepository;
import com.spotit.backend.storage.service.StorageService;

@Service
public class ForeignLanguageNameServiceImpl
        extends AbstractServiceImpl<ForeignLanguageName, Integer>
        implements ForeignLanguageNameService {

    private static final String FLAGS_DIRECTORY_NAME = "flags";

    private final StorageService storageService;

    public ForeignLanguageNameServiceImpl(
            ForeignLanguageNameRepository foreignLanguageNameRepository,
            StorageService storageService) {
        super(foreignLanguageNameRepository);
        this.storageService = storageService;
    }

    @Override
    public List<ForeignLanguageName> getAll() {
        return repository.findAll();
    }

    @Override
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
    public void delete(Integer id) {
        ForeignLanguageName foreignLanguageNameToDelete = getById(id);

        storageService.deleteFile(getFilenameFromUrl(foreignLanguageNameToDelete.getFlagUrl()));

        repository.delete(foreignLanguageNameToDelete);
    }

    @Override
    public ForeignLanguageName update(Integer id, String name, byte[] flag) {
        ForeignLanguageName foundForeignLanguageName = getById(id);

        if (name != null) {
            foundForeignLanguageName.setName(name);
        }

        if (flag != null) {
            storageService.uploadFile(
                    flag,
                    FLAGS_DIRECTORY_NAME,
                    getFilenameFromUrl(foundForeignLanguageName.getFlagUrl()));
        }

        return repository.save(foundForeignLanguageName);
    }
}
