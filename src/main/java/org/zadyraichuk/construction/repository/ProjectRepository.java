package org.zadyraichuk.construction.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.zadyraichuk.construction.entity.Project;

@Repository
public interface ProjectRepository extends MongoRepository<Project, ObjectId> {

    Page<Project> findAllByCompanyId(ObjectId companyId, Pageable pageable);

    Page<Project> findAllByContractorId(ObjectId contractorId, Pageable pageable);

}
