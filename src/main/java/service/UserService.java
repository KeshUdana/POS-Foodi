package service;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repo.UserRepo;

public class UserService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo=userRepo;
    }
    public User registerUser(User user){
        user.setpassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }
}
