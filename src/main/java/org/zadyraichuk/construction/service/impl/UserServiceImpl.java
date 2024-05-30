package org.zadyraichuk.construction.service.impl;

import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.dto.UserDTO;
import org.zadyraichuk.construction.dto.creation.RegisterUserDTO;
import org.zadyraichuk.construction.dto.simple.UserSimpleDTO;
import org.zadyraichuk.construction.entity.User;
import org.zadyraichuk.construction.repository.UserRepository;
import org.zadyraichuk.construction.service.PictureService;
import org.zadyraichuk.construction.service.UserService;
import org.zadyraichuk.construction.service.mapper.UserMapper;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository ur;

    private final UserMapper um;

    private final PictureService ps;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository ur,
                           UserMapper um,
                           PictureService ps,
                           BCryptPasswordEncoder passwordEncoder) {
        this.ur = ur;
        this.um = um;
        this.ps = ps;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserSimpleDTO> findSimpleById(String id) {
        Optional<User> userOpt = ur.findById(new ObjectId(id));
        return mapSimple(userOpt);
    }

    @Override
    public Optional<UserSimpleDTO> findSimpleByEmail(String email) {
        Optional<User> userOpt = ur.findByEmail(email);
        return mapSimple(userOpt);
    }

    @Override
    public Optional<UserSimpleDTO> findSimpleByUsername(String username) {
        Optional<User> userOpt = ur.findByUsername(username);
        return mapSimple(userOpt);
    }

    @Override
    public Optional<UserDTO> findById(String id) {
        Optional<User> userOpt = ur.findById(new ObjectId(id));
        return map(userOpt);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        Optional<User> userOpt = ur.findByEmail(email);
        return map(userOpt);
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        Optional<User> userOpt = ur.findByUsername(username);
        return map(userOpt);
    }

    @Override
    public Optional<UserDTO> save(RegisterUserDTO user) {
        User entity = um.toEntity(user);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        UserDTO created = um.toDTO(ur.save(entity));
        //TODO upload users picture
        return Optional.of(created);
    }

    @Override
    public Optional<UserDTO> update(UserDTO user) {
        UserDTO updated = um.toDTO(ur.save(um.toEntity(user)));
        //TODO create special UserUpdateDTO with File picture instead of URL
        return Optional.of(updated);
    }

    @Override
    public boolean deleteById(String id) {
        ObjectId objectId = new ObjectId(id);
        ur.deleteById(objectId);
        ps.removeUserPicture(id);
        return ur.findById(objectId).isPresent();
    }

    @Override
    public boolean delete(UserDTO user) {
        User entity = um.toEntity(user);
        ur.delete(entity);
        ps.removeUserPicture(user.getUserId());
        return ur.findById(entity.getId()).isEmpty();
    }

    private Optional<UserSimpleDTO> mapSimple(Optional<User> userOpt) {
        if (userOpt.isPresent()) {
            User entity = userOpt.get();
            UserSimpleDTO dto = um.toConnectorDTO(entity);

            if (entity.hasPicture()) {
                dto.setUserPicture(ps.getUserPictureURL(dto.getUserId()));
            } else {
                dto.setUserPicture(ps.getDefaultUserPicture());
            }

            return Optional.of(dto);
        }
        return Optional.empty();
    }

    private Optional<UserDTO> map(Optional<User> userOpt) {
        if (userOpt.isPresent()) {
            User entity = userOpt.get();
            UserDTO dto = um.toDTO(entity);

            if (entity.hasPicture()) {
                dto.setUserPicture(ps.getUserPictureURL(dto.getUserId()));
            } else {
                dto.setUserPicture(ps.getDefaultUserPicture());
            }

            return Optional.of(dto);
        }
        return Optional.empty();
    }


}
