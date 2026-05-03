package com.kiruthika.chatapp.user_service.service;


import com.kiruthika.chatapp.user_service.dto.LastSeenRequestDto;
import com.kiruthika.chatapp.user_service.dto.UserRequestDTO;
import com.kiruthika.chatapp.user_service.dto.UserResponseDTO;
import com.kiruthika.chatapp.user_service.exception.ForbiddenActionException;
import com.kiruthika.chatapp.user_service.exception.UserNotFoundException;
import com.kiruthika.chatapp.user_service.modal.User;
import com.kiruthika.chatapp.user_service.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    private final AuthenticationManager auth;

    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
    private static final Map<String,String> languageCodeMap=Map.of(
            "English","en",
            "Tamil", "ta",
            "Hindi", "hi",
            "Telugu", "te",
            "Kannada", "kn",
            "French", "fr",
            "Malayalam","ml"
    );

    private final JwtService jwtService;
    public ResponseEntity<String> registerUser(UserRequestDTO userRequestDTO) {
        boolean exists=userRepo.existsByUsername(userRequestDTO.getUsername());
        if(exists){
            throw new ForbiddenActionException("Username already exists");
        }
        User user= new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(encoder.encode(userRequestDTO.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");

    }

    public ResponseEntity<String> loginUser(UserRequestDTO userRequestDTO) {




        try{
            Authentication authentication=auth.authenticate(new UsernamePasswordAuthenticationToken(userRequestDTO.getUsername(),userRequestDTO.getPassword()));

                String token=jwtService.generateToken(userRequestDTO.getUsername());
                return ResponseEntity.status(HttpStatus.OK).body(token);






        }
        catch(BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        }

    }


    public ResponseEntity<UserResponseDTO> getLoggedInUser(String username) {
       User user= userRepo.findByUsername(username);
       UserResponseDTO userResponseDTO=new UserResponseDTO(user.getId(),user.getUsername(),user.getLastSeen(),user.getProfileImage(),user.getPreferredLanguage());
       return ResponseEntity.ok(userResponseDTO);

    }

    public List<UserResponseDTO> getAllUsers(String username) {


            List<User> allUsers=userRepo.findAll();
            List<UserResponseDTO> allUsersExceptLoggedInUser=new ArrayList<>();
            for(User user:allUsers){
                if(!user.getUsername().equals(username)){
                    allUsersExceptLoggedInUser.add(new UserResponseDTO(user.getId(),user.getUsername(),user.getLastSeen(),user.getProfileImage(),user.getPreferredLanguage()));
                }
            }
            for(UserResponseDTO userDTO:allUsersExceptLoggedInUser){
                System.out.println(userDTO.toString());
            }


            return allUsersExceptLoggedInUser;

    }

    public ResponseEntity<UserResponseDTO> changeLanguage(String username, String lang) {
        User user=userRepo.findByUsername(username);
        if(user==null){
            throw new UserNotFoundException("User not found");
        }
        String languageCode=languageCodeMap.get(lang);
        if(languageCode==null){
            throw new IllegalArgumentException("Unsupported language");
        }
        user.setPreferredLanguage(languageCode);
        userRepo.save(user);


        UserResponseDTO userResponseDTO=new UserResponseDTO(user.getId(),user.getUsername(),user.getLastSeen(),user.getProfileImage(),user.getPreferredLanguage());

        return ResponseEntity.ok(userResponseDTO);
    }

    public List<UserResponseDTO> search(String username, String keyword) {

        List<User> users=userRepo.findByUsernameContainingIgnoreCase(keyword);
        List<UserResponseDTO> userDTOList=new ArrayList<>();
        for(User user:users){
            if(!user.getUsername().equals(username)){
                UserResponseDTO userDTO=new UserResponseDTO(user.getId(),user.getUsername(),user.getLastSeen(),user.getProfileImage(),user.getPreferredLanguage());
                userDTOList.add(userDTO);
            }

        }
        return userDTOList;

    }

    public ResponseEntity<Void> updateLastseen(LastSeenRequestDto dto) {
        String username=dto.getUsername();
        User user=userRepo.findByUsername(username);
        if(user==null){
            throw new UserNotFoundException("user not found");
        }
        user.setLastSeen(dto.getLastseen());
        userRepo.save(user);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public Long getId(String username) {
        return userRepo.findByUsername(username).getId();
    }

    public String getUsername(Long id) {
        User user=userRepo.findById(id).orElseThrow(()->new RuntimeException("user id not found"));
        return user.getUsername();
    }

    public String getPreferredLanguage(Long id) {
        User user=userRepo.findById(id).orElseThrow(()->new RuntimeException("user id not found"));
        return user.getPreferredLanguage();

    }


    public ResponseEntity<?> uploadImage(String username, MultipartFile file)  {
        try{
            User user=userRepo.findByUsername(username);
            String folder="D:/ChatConnect/microservices/user-service/uploads/";

            File dir=new File(folder);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String fileName=System.currentTimeMillis()+"_"+username+"_"+file.getOriginalFilename();
            String filePath=folder+fileName;
            file.transferTo(new File(filePath));
            String dbPath="/uploads/"+fileName;

            user.setProfileImage(dbPath);
            userRepo.save(user);
            return ResponseEntity.ok(dbPath);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading the file");
        }


    }
}
