package com.bibek.webbackend.Service;


import com.bibek.webbackend.Dto.RegisterDto;
import com.bibek.webbackend.Dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    String getEmail(String email);

    int getIdFromEmail(String email);

    String userRegistration(RegisterDto registerDto);

    String updateUser(RegisterDto registerDto);

    UserDto getUserById(int userId);

    List<UserDto> getAllUsers();

    String deleteUserById(int userId);

    String getRole(String email);
}
