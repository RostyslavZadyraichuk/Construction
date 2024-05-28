package org.zadyraichuk.construction.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.zadyraichuk.construction.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByFullName(String fullName);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}
