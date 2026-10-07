package com.example.manikantasales.serviceimpl;


import com.example.manikantasales.entity.User;
import com.example.manikantasales.repository.UserRepository;
import com.example.manikantasales.service.UserService;

import org.springframework.stereotype.Service;


import java.util.List;



@Service
public class UserServiceImpl 
        implements UserService {



private final UserRepository userRepository;



public UserServiceImpl(
        UserRepository userRepository
){

this.userRepository=userRepository;

}


@Override
public User getUserByEmail(String email){

    return userRepository.findByEmail(email)
            .orElseThrow(
                () -> new RuntimeException("User not found")
            );

}


@Override
public List<User> getAllUsers(){

return userRepository
        .findAllByOrderByIdDesc();

}






@Override
public User getUserById(Long id){


return userRepository
        .findById(id)
        .orElseThrow(
        ()->new RuntimeException(
        "User not found"
        ));

}






@Override
public void deleteUser(Long id){

userRepository.deleteById(id);

}





@Override
public void blockUser(Long id){


User user=getUserById(id);


user.setAccountLocked(true);


userRepository.save(user);


}







@Override
public void unblockUser(Long id){


User user=getUserById(id);


user.setAccountLocked(false);


userRepository.save(user);


}





@Override
public Long getUserCount(){

return userRepository.count();

}



}