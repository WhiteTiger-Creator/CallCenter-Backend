package org.comcom.service.implementation;

import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.comcom.config.security.SecurityUtils;
import org.comcom.dto.*;
import org.comcom.exception.BadInputException;
import org.comcom.exception.CompanyCategoryNotFoundException;
import org.comcom.exception.CompanyNotFoundException;
import org.comcom.model.Company;
import org.comcom.model.Users;
import org.comcom.model.Company_Category;
import org.comcom.repository.CompanyCategoryRepository;
import org.comcom.repository.CompanyRepository;
import org.comcom.repository.UserRepository;
import org.comcom.repository.VideoCallRoomRepository;
import org.comcom.service.CompanyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyDB;

    private final UserRepository usersDB;

    private final CompanyCategoryRepository categoryDB;

    private final VideoCallRoomRepository videoCallRoomRepository;

    @Override
    public CompanyDataDto createCompany(CreateCompanyDto createCompanyRequest) {
        Optional<Company> companyOptional = companyDB.findByEmail(createCompanyRequest.getEmail());
        if (companyOptional.isPresent()) {
            throw new BadInputException("Mail is already in use.","Mail is already in use.");
        }
        else {
            Optional<Company> companyOptional1 = companyDB.findByWebsiteUrl(createCompanyRequest.getWebsiteUrl());
            if (companyOptional1.isPresent()) {
                throw new BadInputException("WebsiteUrl is already in use.", "WebsiteUrl is already in use.");
            } else {
                Company companyEntity = new Company();
                BeanUtils.copyProperties(createCompanyRequest, companyEntity);
                companyEntity.setCreatedOn(LocalDateTime.now());
                companyEntity.setUpdatedOn(LocalDateTime.now());

                Optional<Company_Category> companyCategory = categoryDB.findById(createCompanyRequest.getCompanyCategoryId());

                if (companyCategory.isPresent()) {
                    companyEntity.setCompanyCategory(companyCategory.get());
                    System.out.println("pass company");
                    Users user = usersDB.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get();
                    companyDB.save(companyEntity);
                    user.setCompany(companyEntity);
                    usersDB.save(user);
                    return companyDB.save(companyEntity).toDto();
                }

                throw new BadInputException("Incorrect Company Category Id", "Company Category Id doesn't  exist");
            }
        }
    }

    @Override
    public List<CompanyDataDto> fetchCompanies() {
        return companyDB.findAll().stream().map(Company::toDto).collect(Collectors.toList());
    }

    @Override
    public CompanyDataDto fetchCompany(Long companyId) {
        if(companyDB.findById(companyId).isPresent()){
            return companyDB.findById(companyId).get().toDto();
        }
        throw new CompanyNotFoundException(companyId);
    }

    @Override
    public List<CompanyCallRespondDto> getCallRequests(){
        List<CompanyCallRespondDto> companyCallRespondDtos = new ArrayList<>();
        Long employerCompany_id = usersDB.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get().getCompany().getId();
        videoCallRoomRepository.findAll()
                .stream()
                .filter(videoCallRoom -> videoCallRoom.getCompany() != null && videoCallRoom.getCompany().getId() == employerCompany_id && videoCallRoom.getRoom_status() != null && videoCallRoom.getRoom_status() == 0)
                .forEach(videoCallRoom ->companyCallRespondDtos.add(new CompanyCallRespondDto(videoCallRoom)));

        return companyCallRespondDtos;
    }

    @Override
    public CompanyDataDto getCompanyByUrl(String url){
        Company company = new Company();
        companyDB.findAll()
                .stream()
                .filter(company1 -> company1.getWebsiteUrl().equals(url))
                .forEach(company1 -> BeanUtils.copyProperties(company1, company));

        return company.toDto();
    }

    @Override
    public CompanyCategoryDataDto createCategory(CompanyCategoryDto createCompanyCategory) {
        return categoryDB.save(new Company_Category(createCompanyCategory.getName(), createCompanyCategory.getDescription())).toDto();
    }

    @Override
    public List<CompanyCategoryDataDto> fetchCategories() {
        return categoryDB.findAll().stream().map(Company_Category::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Users> getCompanyEmployees(Long id) {
        return usersDB.findAll()
                .stream()
                .filter(user -> user.getVerified() && user.getProfile().getId() == 5 && id == user.getCompany().getId())
                .collect(Collectors.toList()); // Specify the collector to collect into a list
    }

    @Override
    public boolean deleteCompany(Long companyId) {
        if(companyDB.findById(companyId).isPresent()){
            companyDB.deleteById(companyId);
            Users user = usersDB.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get();
            user.setCompany(null);
            return true;
        }else {
            throw new CompanyNotFoundException(companyId);
        }
    }

    @Override
    public boolean deleteCompanyCategory(Long categoryId) {
        if(categoryDB.findById(categoryId).isPresent()){
            categoryDB.deleteById(categoryId);
            return true;
        }else {
            throw new CompanyCategoryNotFoundException(categoryId);
        }
    }
}
