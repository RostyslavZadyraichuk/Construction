package org.zadyraichuk.construction.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.zadyraichuk.construction.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByEmail(String email);

    User findByFullName(String fullName);

}
