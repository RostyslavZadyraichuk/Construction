package org.zadyraichuk.construction.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.zadyraichuk.construction.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, ObjectId> {

    List<Review> findAllByProjectId(ObjectId projectId);

}
