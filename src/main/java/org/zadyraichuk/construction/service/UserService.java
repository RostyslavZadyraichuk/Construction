package org.zadyraichuk.construction.service;

import org.zadyraichuk.construction.dto.UserDTO;
import org.zadyraichuk.construction.dto.creation.RegisterUserDTO;
import org.zadyraichuk.construction.dto.simple.UserSimpleDTO;

import java.util.Optional;

public interface UserService {

    Optional<UserSimpleDTO> findSimpleById(String id);

    Optional<UserSimpleDTO> findSimpleByEmail(String email);

    Optional<UserSimpleDTO> findSimpleByUsername(String username);

    Optional<UserDTO> findById(String id);

    Optional<UserDTO> findByEmail(String email);

    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> save(RegisterUserDTO user);

    Optional<UserDTO> update(UserDTO user);

    boolean deleteById(String id);

    boolean delete(UserDTO user);

}
