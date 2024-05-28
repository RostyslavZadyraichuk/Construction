package org.zadyraichuk.construction.service;

import org.zadyraichuk.construction.dto.BuildingCompanyDTO;
import org.zadyraichuk.construction.dto.creation.CompanyRegisterDTO;
import org.zadyraichuk.construction.dto.simple.CompanySimpleDTO;
import org.zadyraichuk.construction.dto.simple.UserSimpleDTO;

import java.util.Optional;

public interface BuildingCompanyService {

    Optional<CompanySimpleDTO> findSimpleById(String id);

    Optional<CompanySimpleDTO> findSimpleByName(String companyName);

    Optional<BuildingCompanyDTO> findById(String id);

    Optional<BuildingCompanyDTO> findByName(String companyName);

    Optional<BuildingCompanyDTO> findByOwnerId(String ownerId);

    Optional<BuildingCompanyDTO> findByOwnerFullName(String fullName);

    Optional<BuildingCompanyDTO> findByOwner(UserSimpleDTO owner);

    Optional<BuildingCompanyDTO> save(CompanyRegisterDTO company);

    Optional<BuildingCompanyDTO> update(BuildingCompanyDTO company);

    boolean deleteById(String id);

    boolean delete(CompanySimpleDTO company);

}
