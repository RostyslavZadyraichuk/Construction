package org.zadyraichuk.construction.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.zadyraichuk.construction.entity.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, ObjectId> {

    List<Project> findAllByCompanyId(ObjectId companyId);

    List<Project> findAllByContractorId(ObjectId contractorId);

}
