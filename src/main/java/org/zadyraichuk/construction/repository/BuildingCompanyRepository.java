package org.zadyraichuk.construction.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.zadyraichuk.construction.entity.BuildingCompany;

import java.util.Optional;

@Repository
public interface BuildingCompanyRepository extends MongoRepository<BuildingCompany, ObjectId> {

    Optional<BuildingCompany> findByName(String name);

    Optional<BuildingCompany> findByCompanyOwnerUserId(ObjectId id);

    Optional<BuildingCompany> findByCompanyOwnerFullName(String fullName);

}
