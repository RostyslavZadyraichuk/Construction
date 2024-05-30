package org.zadyraichuk.construction.service.impl;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.dto.BuildingCompanyDTO;
import org.zadyraichuk.construction.dto.creation.CompanyRegisterDTO;
import org.zadyraichuk.construction.dto.simple.CompanySimpleDTO;
import org.zadyraichuk.construction.dto.simple.UserSimpleDTO;
import org.zadyraichuk.construction.entity.BuildingCompany;
import org.zadyraichuk.construction.repository.BuildingCompanyRepository;
import org.zadyraichuk.construction.service.BuildingCompanyService;
import org.zadyraichuk.construction.service.PictureService;
import org.zadyraichuk.construction.service.mapper.BuildingCompanyMapper;

import java.util.Optional;

@Service
public class BuildingCompanyServiceImpl implements BuildingCompanyService {

    private BuildingCompanyRepository bcr;

    private BuildingCompanyMapper bcm;

    private PictureService ps;

    public BuildingCompanyServiceImpl(BuildingCompanyRepository bcr,
                                      BuildingCompanyMapper bcm,
                                      PictureService ps) {
        this.bcr = bcr;
        this.bcm = bcm;
        this.ps = ps;
    }

    @Override
    public Optional<CompanySimpleDTO> findSimpleById(String id) {
        Optional<BuildingCompany> company = bcr.findById(new ObjectId(id));
        return mapSimple(company);
    }

    @Override
    public Optional<CompanySimpleDTO> findSimpleByName(String companyName) {
        Optional<BuildingCompany> company = bcr.findByName(companyName);
        return mapSimple(company);
    }

    @Override
    public Optional<BuildingCompanyDTO> findById(String id) {
        Optional<BuildingCompany> company = bcr.findById(new ObjectId(id));
        return map(company);
    }

    @Override
    public Optional<BuildingCompanyDTO> findByName(String companyName) {
        Optional<BuildingCompany> company = bcr.findByName(companyName);
        return map(company);
    }

    @Override
    public Optional<BuildingCompanyDTO> findByOwnerId(String ownerId) {
        Optional<BuildingCompany> company = bcr.findByCompanyOwnerUserId(new ObjectId(ownerId));
        return map(company);
    }

    @Override
    public Optional<BuildingCompanyDTO> findByOwnerFullName(String fullName) {
        Optional<BuildingCompany> company = bcr.findByCompanyOwnerFullName(fullName);
        return map(company);
    }

    @Override
    public Optional<BuildingCompanyDTO> findByOwner(UserSimpleDTO owner) {
        Optional<BuildingCompany> company = bcr.findByCompanyOwnerUserId(new ObjectId(owner.getUserId()));
        if (company.isEmpty()) {
            company = bcr.findByCompanyOwnerFullName(owner.getFullName());
        }
        return company.map(bcm::toDto);
    }

    @Override
    public Optional<BuildingCompanyDTO> save(CompanyRegisterDTO company) {
        BuildingCompany entity = bcm.toEntity(company);
        BuildingCompanyDTO created = bcm.toDto(bcr.save(entity));
        //TODO upload company logo
        return Optional.of(created);
    }

    @Override
    public Optional<BuildingCompanyDTO> update(BuildingCompanyDTO company) {
        BuildingCompany entity = bcr.save(bcm.toEntity(company));
        BuildingCompanyDTO updated = bcm.toDto(entity);
        //TODO create special CompanyUpdateDTO with File picture instead of URL
        return Optional.of(updated);
    }

    @Override
    public boolean deleteById(String id) {
        ObjectId objectId = new ObjectId(id);
        bcr.deleteById(objectId);
        ps.removeCompanyPicture(id);
        return bcr.findById(objectId).isPresent();
    }

    @Override
    public boolean delete(CompanySimpleDTO company) {
        BuildingCompany entity = bcm.toEntity(company);
        bcr.delete(entity);
        ps.removeCompanyPicture(company.getCompanyId());
        return bcr.findById(entity.getId()).isEmpty();
    }

    private Optional<CompanySimpleDTO> mapSimple(Optional<BuildingCompany> companyOpt) {
        if (companyOpt.isPresent()) {
            BuildingCompany entity = companyOpt.get();
            CompanySimpleDTO dto = bcm.toConnectorDto(entity);

            if (entity.hasPicture()) {
                dto.setCompanyLogo(ps.getCompanyPictureURL(dto.getCompanyId()));
            } else {
                dto.setCompanyLogo(ps.getDefaultCompanyPicture());
            }

            return Optional.of(dto);
        }
        return Optional.empty();
    }

    private Optional<BuildingCompanyDTO> map(Optional<BuildingCompany> companyOpt) {
        if (companyOpt.isPresent()) {
            BuildingCompany entity = companyOpt.get();
            BuildingCompanyDTO dto = bcm.toDto(entity);

            if (entity.hasPicture()) {
                dto.setCompanyLogo(ps.getCompanyPictureURL(dto.getCompanyId()));
            } else {
                dto.setCompanyLogo(ps.getDefaultCompanyPicture());
            }

            return Optional.of(dto);
        }
        return Optional.empty();
    }
}
