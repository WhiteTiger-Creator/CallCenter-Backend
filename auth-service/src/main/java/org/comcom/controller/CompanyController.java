package org.comcom.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.comcom.dto.ApiResponse;
import org.comcom.dto.CompanyCategoryDto;
import org.comcom.dto.CreateCompanyDto;
import org.comcom.service.CompanyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.comcom.utils.ApiResponseUtils.buildResponse;

@CrossOrigin
@RestController
@RequestMapping("/v1/companies")
@AllArgsConstructor
@Slf4j(topic = "CompanyController")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> createCompany(@RequestBody CreateCompanyDto createCompanyRequest) {
         return buildResponse(companyService.createCompany(createCompanyRequest));
    }

    @GetMapping(path = "getCallRequests", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> getCallRequests(){
        return buildResponse(companyService.getCallRequests());
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> fetchCompanies() {
        return buildResponse(companyService.fetchCompanies());
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> fetchCompanyById(@PathVariable Long id) {
        return buildResponse(companyService.fetchCompany(id));
    }

    @GetMapping(path = "/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> getCompanyEmployees(@PathVariable Long id){
        return buildResponse(companyService.getCompanyEmployees(id));
    }

    @GetMapping(path = "/getCompanyByUrl", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> getCompanyByUrl(@RequestParam String url){
        return buildResponse(companyService.getCompanyByUrl(url));
    }

    @GetMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> getCompanyCategory() {
        return buildResponse(companyService.fetchCategories());
    }

    @PostMapping(path = "/category", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> createCompanyCategory(@RequestBody @Valid CompanyCategoryDto companyCategoryDto) {
        return buildResponse(companyService.createCategory(companyCategoryDto));
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> deleteCompanyById(@PathVariable Long id) {
        return buildResponse(companyService.deleteCompany(id));
    }


    @DeleteMapping(path = "/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> deleteCompanyCategoryById(@PathVariable Long id) {
        return buildResponse(companyService.deleteCompanyCategory(id));
    }
}
