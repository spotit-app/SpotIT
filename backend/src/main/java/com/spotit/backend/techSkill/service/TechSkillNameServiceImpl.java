package com.spotit.backend.techSkill.service;

import static com.spotit.backend.storage.service.StorageServiceImpl.getFilenameFromUrl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.service.AbstractServiceImpl;
import com.spotit.backend.storage.service.StorageService;
import com.spotit.backend.techSkill.model.TechSkillName;
import com.spotit.backend.techSkill.repository.TechSkillNameRepository;

@Service
public class TechSkillNameServiceImpl
        extends AbstractServiceImpl<TechSkillName, Integer>
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
    public List<TechSkillName> getAll() {
        return techSkillNameRepository.findByCustom(false);
    }

    @Override
    public Optional<TechSkillName> getByName(String name) {
        return techSkillNameRepository.findByName(name);
    }

    @Override
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

        return repository.save(foundTechSkillName);
    }

    @Override
    public void delete(Integer id) {
        TechSkillName techSkillNameToDelete = getById(id);

        storageService.deleteFile(getFilenameFromUrl(techSkillNameToDelete.getLogoUrl()));

        repository.delete(techSkillNameToDelete);
    }
}
