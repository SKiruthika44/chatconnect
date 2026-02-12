package com.chatConnect.backend.Service;

import com.chatConnect.backend.Modal.UserDTO;
import com.chatConnect.backend.Modal.Users;
import com.chatConnect.backend.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired

    private UserRepo repo;

    @Autowired
    private JWTService jwtService;
    @Autowired
    AuthenticationManager auth;
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
    public ResponseEntity<String> register(Users user){
        Users existUser=repo.findByUsername(user.getUsername());
        if(existUser!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        System.out.println(user.getUsername());
        return ResponseEntity.ok("User registered successfully");

    }

    public ResponseEntity<String> verify(Users user){
        Authentication authentication=auth.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if(authentication.isAuthenticated()){
            String token=jwtService.generateToken(user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(token);



        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    public List<UserDTO> getAllUsers(String username) {

        List<Users> allUsers=repo.findAll();
        List<UserDTO> allUsersExceptLoggedInUser=new ArrayList<>();
        for(Users user:allUsers){
            if(!user.getUsername().equals(username)){
                allUsersExceptLoggedInUser.add(new UserDTO(user.getId(),user.getUsername(),user.getLastSeen(),user.getProfileImage(),user.getPreferredLanguage()));
            }
        }
        for(UserDTO userDTO:allUsersExceptLoggedInUser){
            System.out.println(userDTO.toString());
        }

        System.out.println("allusers"+allUsersExceptLoggedInUser);
        return allUsersExceptLoggedInUser;
    }

    public ResponseEntity<?> uploadImage(String username, MultipartFile file) {
        try{
            Users user=repo.findByUsername(username);
            String folder="D:/SpringProject/websocket-With-Security/uploads/";

            File dir=new File(folder);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String fileName=System.currentTimeMillis()+"_"+username+"_"+file.getOriginalFilename();
            String filePath=folder+fileName;
            file.transferTo(new File(filePath));
            String dbPath="/uploads/"+fileName;

            user.setProfileImage(dbPath);
            repo.save(user);
            return ResponseEntity.ok(dbPath);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error Uploading file");
        }




    }

    public ResponseEntity<Users> getLoggedInUser(String username) {
        Users user=repo.findByUsername(username);
        UserDTO userDTO=new UserDTO(user.getId(),user.getUsername(),user.getLastSeen(),user.getProfileImage(),user.getPreferredLanguage());
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<UserDTO> changeLanguage(String username,String lang) {

        Users user=repo.findByUsername(username);
        String languageCode=languageCodeMap.get(lang);
        user.setPreferredLanguage(languageCode);
        repo.save(user);
        Users user2=repo.findByUsername(username);
        System.out.println(user2.toString());
        UserDTO userDTO=new UserDTO(user.getId(),user.getUsername(),user.getLastSeen(),user.getProfileImage(),user.getPreferredLanguage());
        System.out.println(userDTO.toString());
        return ResponseEntity.ok(userDTO);
    }

    public List<UserDTO> search(String username, String keyword) {

            List<Users> users=repo.findByUsernameContainingIgnoreCase(keyword);
            List<UserDTO> userDTOList=new ArrayList<>();
            for(Users user:users){
                if(!user.getUsername().equals(username)){
                    UserDTO userDTO=new UserDTO(user.getId(),user.getUsername(),user.getLastSeen(),user.getProfileImage(),user.getPreferredLanguage());
                    userDTOList.add(userDTO);
                }

            }
           return userDTOList;

    }
}
