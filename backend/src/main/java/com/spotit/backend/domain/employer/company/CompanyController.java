package com.spotit.backend.domain.employer.company;

import static com.spotit.backend.utils.FileUtils.getBytesFromFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping("/company/{id}")
    public CompanyReadDto getCompanyInformation(@PathVariable Integer id) {
        return companyMapper.toReadDto(companyService.getById(id));
    }

    @GetMapping("/userAccount/{auth0Id}/company")
    public List<CompanyReadDto> getAllCompaniesOfUser(@PathVariable String auth0Id) {
        return companyService.getAllByUserAccountAuth0Id(auth0Id)
                .stream()
                .map(companyMapper::toReadDto)
                .toList();
    }

    @GetMapping("/userAccount/{auth0Id}/company/{id}")
    public CompanyReadDto getCompanyById(@PathVariable Integer id) {
        return companyMapper.toReadDto(companyService.getById(id));
    }

    @PostMapping("/userAccount/{auth0Id}/company")
    public CompanyReadDto createCompanyOfUser(
            @PathVariable String auth0Id,
            @RequestBody CompanyWriteDto writeDto) {
        Company companyToCreate = companyMapper.fromWriteDto(writeDto);
        Company createdEntity = companyService.create(auth0Id, companyToCreate);

        return companyMapper.toReadDto(createdEntity);
    }

    @PutMapping("/userAccount/{auth0Id}/company/{id}")
    public CompanyReadDto updateCompanyById(@PathVariable Integer id, @RequestPart Optional<CompanyWriteDto> writeDto,
            @RequestPart Optional<MultipartFile> profilePicture) {
        Company companyToUpdate = companyMapper.fromWriteDto(writeDto.orElse(null));
        Company editedCompany = companyService.update(id, companyToUpdate,
                getBytesFromFile(profilePicture.orElse(null)));

        return companyMapper.toReadDto(editedCompany);
    }

    @DeleteMapping("/userAccount/{auth0Id}/company/{id}")
    public Map<String, Integer> deleteCompanyById(@PathVariable Integer id) {
        companyService.delete(id);

        return Map.of("id", id);
    }
}
