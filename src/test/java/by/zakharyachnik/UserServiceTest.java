package by.zakharyachnik;

import by.zakharyachnik.entity.User;
import by.zakharyachnik.repositories.UserInfoRepository;
import by.zakharyachnik.repositories.UserRepository;
import by.zakharyachnik.service.UserService;
import by.zakharyachnik.service.exceptions.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserInfoRepository userInfoRepository;

    @Test
    public void registrationTest() throws Exception {
        String username = "TestingUser";
        String password = "TestingPass";
        String phoneNumber = "+375333456789";
        String fullName = "Testing Testing Testing";

        User user = userService.registration(username, password, phoneNumber, fullName);

        Assert.assertNotNull(user);
        Assert.assertEquals(username, user.getUsername());
        Assert.assertNotEquals(password, user.getPassword());
        Assert.assertTrue(user.isActive());
    }
}
