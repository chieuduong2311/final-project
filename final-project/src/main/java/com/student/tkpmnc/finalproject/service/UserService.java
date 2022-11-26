package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.User;
import com.student.tkpmnc.finalproject.api.model.UserStatus;
import com.student.tkpmnc.finalproject.api.model.UserType;
import com.student.tkpmnc.finalproject.entity.RawUser;
import com.student.tkpmnc.finalproject.entity.RawVehicle;
import com.student.tkpmnc.finalproject.exception.NotFoundException;
import com.student.tkpmnc.finalproject.exception.RequestException;
import com.student.tkpmnc.finalproject.repository.UserRepository;
import com.student.tkpmnc.finalproject.repository.VehicleRepository;
import com.student.tkpmnc.finalproject.service.helper.SchemaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SchemaHelper schemaHelper;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final String SCHEMA_NAME = "user";

    @Transactional
    public User createUser(User request) {
        log.info("createUser received request: {}", request);
        schemaHelper.validate(SCHEMA_NAME, request);
        if (userRepository.findFirstByPhone(request.getPhone()).isPresent()) {
            throw new RequestException("This phone is belonged to another driver, invalid request");
        }
        if (userRepository.findFirstByUsername(request.getUsername()).isPresent()) {
            throw new RequestException("This username is belonged to another driver, invalid request");
        }

        String pw = bCryptPasswordEncoder.encode(request.getPassword());
        RawUser user = RawUser.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .phone(request.getPhone())
                .isDeleted(false)
                .isDriver(false)
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.CUSTOMER)
                .password(pw)
                .build();

        user = userRepository.saveAndFlush(user);

        request.id(user.getId())
                .userType(user.getUserType())
                .userStatus(user.getUserStatus());
        log.info("createUser returned: {}", request);
        return request;
    }

    @Transactional
    public void deleteUser(String id) {
        Long idInLong = Long.parseLong(id);
        var rawUserOpt = userRepository.findById(idInLong);
        if (rawUserOpt.isEmpty()) {
            throw new NotFoundException("User is not found");
        }
        rawUserOpt.get().setIsDeleted(true);
        userRepository.saveAndFlush(rawUserOpt.get());
        log.info("deleteUser returned OK: {}", id);
    }

    @Transactional
    public User getUserById(String id) {
        Long idInLong = Long.parseLong(id);
        var rawUserOpt = userRepository.findById(idInLong);
        if (rawUserOpt.isEmpty()) {
            throw new NotFoundException("User is not found");
        }
        User user = rawUserOpt.get().toUser();
        if (rawUserOpt.get().getIsDriver()) {
            var vehicleOpt = vehicleRepository.findFirstByDriverId(rawUserOpt.get().getId());
            if (vehicleOpt.isEmpty()) {
                throw new NotFoundException("Driver's vehicle is not found");
            }
            user.vehicleInfo(vehicleOpt.get().toVehicle());
        }
        log.info("getUserById returned: {}", user);
        return user;
    }

    @Transactional
    public RawUser getUserByUsername(String username) {
        var rawUserOpt = userRepository.findFirstByUsername(username);
        if (rawUserOpt.isEmpty()) {
            return null;
        }
        log.info("getUserByUsername returned: {}", rawUserOpt.get());
        return rawUserOpt.get();
    }

    @Transactional
    public User getUserByPhone(String phone) {
        var rawUserOpt = userRepository.findFirstByPhone(phone);
        if (rawUserOpt.isEmpty()) {
            throw new NotFoundException("User is not found");
        }
        User user = rawUserOpt.get().toUser();
        if (rawUserOpt.get().getIsDriver()) {
            var vehicleOpt = vehicleRepository.findFirstByDriverId(rawUserOpt.get().getId());
            if (vehicleOpt.isEmpty()) {
                throw new NotFoundException("Driver's vehicle is not found");
            }
            user.vehicleInfo(vehicleOpt.get().toVehicle());
        }
        log.info("getUserByPhone returned: {}", user);
        return user;
    }

    @Transactional
    public User updateUser(String id, User request) {
        log.info("updateUser received request: {} - {}", id, request);
        Long idInLong = Long.parseLong(id);
        var rawUserOpt = userRepository.findById(idInLong);
        if (rawUserOpt.isEmpty()) {
            throw new NotFoundException("User is not found");
        }
        schemaHelper.validate(SCHEMA_NAME, request);

        if (!rawUserOpt.get().getPhone().equals(request.getPhone())) {
            if (userRepository.findFirstByPhone(request.getPhone()).isPresent()) {
                throw new RequestException("This phone is belonged to another user, invalid request");
            }
        }


//        if (userRepository.findFirstByUsername(request.getUsername()).isPresent()) {
//            throw new RequestException("This username is belonged to another user, invalid request");
//        }

        //prevent updating username
        RawUser rawUser = rawUserOpt.get();
        rawUser.setPersonalId(request.getPersonalId());
//        String pw = bCryptPasswordEncoder.encode(request.getPassword());
//        rawUser.setPassword(pw);
        rawUser.setFirstName(request.getFirstName());
        rawUser.setLastName(request.getLastName());
        rawUser.setPhone(request.getPhone());

        rawUser = userRepository.saveAndFlush(rawUser);

        if (rawUser.getIsDriver()) {
            var vehicleOpt = vehicleRepository.findFirstByDriverId(rawUser.getId());
            RawVehicle rawVehicle;
            if (vehicleOpt.isEmpty()) {
                rawVehicle = RawVehicle.builder()
                        .driverId(rawUser.getId())
                        .type(request.getVehicleInfo().getType())
                        .controlNumber(request.getVehicleInfo().getControlNumber())
                        .build();
            } else {
                rawVehicle = vehicleOpt.get();
                rawVehicle.setType(request.getVehicleInfo().getType());
                rawVehicle.setControlNumber(request.getVehicleInfo().getControlNumber());
            }
            rawVehicle = vehicleRepository.saveAndFlush(rawVehicle);
            request.setVehicleInfo(rawVehicle.toVehicle());
        }

        request.id(rawUser.getId())
                .username(rawUser.getUsername())
                .userType(rawUser.getUserType())
                .userStatus(rawUser.getUserStatus());
        log.info("updateUser returned: {}", request);
        return request;
    }

}
