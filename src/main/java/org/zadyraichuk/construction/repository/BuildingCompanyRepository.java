package org.zadyraichuk.construction.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.zadyraichuk.construction.entity.BuildingCompany;

@Repository
public interface BuildingCompanyRepository extends MongoRepository<BuildingCompany, ObjectId> {

}
