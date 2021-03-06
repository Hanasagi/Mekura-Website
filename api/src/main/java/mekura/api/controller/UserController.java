package mekura.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mekura.api.model.User;
import mekura.api.repository.UserRepository;
import mekura.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mekura.api.exception.ResourceNotFoundException;
import mekura.api.DTO.UserDTO;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping(value="/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    // get all users
    @GetMapping
    public List <User> getAllUsers() {
        return userRepository.findAll();
    }

    // create user rest api
    @PostMapping
    public UserDTO createUser(@RequestBody String user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode userObj = mapper.readTree(user);
        JsonNode userInfo = mapper.readTree(userObj.at("/user").asText());

        UserDTO userDTO = new UserDTO(userInfo.get("username").toString(),userInfo.get("profilePic").toString(),userInfo.get("token").toString());

        return userService.save(userDTO);
    }

    // get user by id rest api
    @PostMapping("/search")
    public ResponseEntity < UserDTO > getUserByName(@RequestBody String username) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode usernameObj = mapper.readTree(username);
        UserDTO user = userService.findByUsername(usernameObj.at("/username").toString());
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity < UserDTO > getUserById(@PathVariable Long id) throws Exception {
        UserDTO user = userService.findById(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    // update user rest api

    @PutMapping("/{id}")
    public ResponseEntity < UserDTO > updateUser(@PathVariable Long id, @RequestBody UserDTO userDetails) throws Exception {
        UserDTO user = userService.findById(id);
        user.setUsername(userDetails.getUsername());
        user.setProfilePic(userDetails.getProfilePic());
        user.setToken(userDetails.getToken());
        UserDTO updatedUser = userService.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    // delete user rest api
    @DeleteMapping("/{id}")
    public ResponseEntity < Map < String, Boolean >> deleteUser(@PathVariable Long id) throws Exception {
        UserDTO user = userService.findById(id);
        userService.delete(user);
        Map < String, Boolean > response = new HashMap < > ();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}