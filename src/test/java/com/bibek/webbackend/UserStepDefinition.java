package com.bibek.webbackend;

import com.bibek.webbackend.Dto.UserDto;
import com.bibek.webbackend.Repository.UserRepository;
import com.bibek.webbackend.Service.UserService;
import io.cucumber.java.en.Given;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserStepDefinition {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Given("get list of users")
    public void get_list_of_users() {
        List<UserDto> allUser = userService.getAllUsers();
        log.info(allUser);
        Assert.assertTrue(!allUser.isEmpty());
    }


    @Given("find by user id")
    public void find_by_user_id() {
        userService.getIdFromEmail("random123@gmail.com");
    }

}
