package org.zadyraichuk.construction.service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.zadyraichuk.construction.dto.BuildingCompanyDTO;
import org.zadyraichuk.construction.dto.simple.CompanySimpleDTO;
import org.zadyraichuk.construction.dto.simple.UserSimpleDTO;
import org.zadyraichuk.construction.entity.BuildingCompany;
import org.zadyraichuk.construction.service.UserService;

@Mapper(componentModel = "spring")
public abstract class BuildingCompanyMapper extends GeneralMapper {

    protected UserService us;

    @Autowired
    public void setUs(UserService us) {
        this.us = us;
    }

    @Mapping(source = "companyId", target = "id", qualifiedByName = "toObjectId")
    @Mapping(source = "companyName", target = "name")
    @Mapping(source = "owner.userId", target = "companyOwnerUserId", qualifiedByName = "toObjectId")
    @Mapping(source = "owner.fullName", target = "companyOwnerFullName")
    public abstract BuildingCompany toEntity(CompanySimpleDTO company);

    @Mapping(source = "companyId", target = "id", qualifiedByName = "toObjectId")
    @Mapping(source = "companyName", target = "name")
    @Mapping(source = "owner.userId", target = "companyOwnerUserId", qualifiedByName = "toObjectId")
    @Mapping(source = "owner.fullName", target = "companyOwnerFullName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "website", target = "website")
    @Mapping(source = "officeLocation", target = "officeLocation")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    public abstract BuildingCompany toEntity(BuildingCompanyDTO company);

    @Mapping(source = "id", target = "companyId", qualifiedByName = "toStringId")
    @Mapping(source = "name", target = "companyName")
    @Mapping(source = "entity.companyOwnerUserId", target = "owner", qualifiedByName = "findUser")
    public abstract CompanySimpleDTO toConnectorDto(BuildingCompany entity);

    @Mapping(source = "id", target = "companyId", qualifiedByName = "toStringId")
    @Mapping(source = "name", target = "companyName")
    @Mapping(source = "entity.companyOwnerUserId", target = "owner", qualifiedByName = "findUser")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "website", target = "website")
    @Mapping(source = "officeLocation", target = "officeLocation")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    public abstract BuildingCompanyDTO toDto(BuildingCompany entity);

    @Named("findUser")
    public UserSimpleDTO findUser(ObjectId id) {
        String userId = toStringId(id);
        return us.findById(userId).orElse(null);
    }

}
