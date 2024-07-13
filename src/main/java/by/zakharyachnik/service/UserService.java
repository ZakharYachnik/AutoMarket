package by.zakharyachnik.service;

import by.zakharyachnik.entity.Publication;
import by.zakharyachnik.entity.User;
import by.zakharyachnik.entity.UserInfo;
import by.zakharyachnik.entity.UserRole;
import by.zakharyachnik.repositories.PublicationRepository;
import by.zakharyachnik.repositories.RoleRepository;
import by.zakharyachnik.repositories.UserInfoRepository;
import by.zakharyachnik.repositories.UserRepository;
import by.zakharyachnik.service.exceptions.ServiceException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PublicationRepository publicationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User registration(String username, String password, String phoneNumber, String fullName) throws ServiceException {
        User userFromDb = userRepository.findByUsername(username);

        if(userFromDb != null){
            throw new ServiceException("User exists!");
        }

        User regUser = new User();
        regUser.setActive(true);
        regUser.setUsername(username);
        regUser.setPassword(passwordEncoder.encode(password));
        UserRole userRole = roleRepository.findByRole("USER");
        regUser.setUserRole(userRole);

        userRepository.save(regUser);

        UserInfo userInfo = new UserInfo(regUser, phoneNumber, fullName);
        userInfoRepository.save(userInfo);

        return regUser;
    }

    public void editUserProfile(User user, String username, String password, String phoneNumber, String fullName) {
        if(password != null && !password.isEmpty()){
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setUsername(username);
        userRepository.save(user);

        user.getUserInfo().setPhoneNumber(phoneNumber);
        user.getUserInfo().setFullName(fullName);
        userInfoRepository.save(user.getUserInfo());
    }

    public void saveUserRole(User user){

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void changeUserRole(User user, String userRole) {
        user.setUserRole(roleRepository.findByRole(userRole));
        userRepository.save(user);
    }

    public void deleteUserById(Integer userId) {
        userRepository.deleteById(userId);
    }


    @Transactional
    public void addFavoritePublication(User user, Publication publication) {
        user.getFavoritePublications().add(publication);
        userRepository.save(user);
    }

    public Boolean getIsFavorite(User user, Publication publication) {
        for(Publication favoritePublication : user.getFavoritePublications()) {
            if(favoritePublication.getId().equals(publication.getId())) {
                return true;
            }
        }
        return false;
    }

    public void deleteFavoritePublication(User user, Publication publication) {
        for(Publication favoritePublication : user.getFavoritePublications()) {
            if (favoritePublication.getId().equals(publication.getId())) {
                user.getFavoritePublications().remove(favoritePublication);
                break;
            }
        }
        publicationRepository.save(publication);
        userRepository.save(user);
    }

    public List<Publication> getFavoritePublications(User user) {
        return user.getFavoritePublications();
    }

    public List<Publication> getUserPublicationsList(User user) {
        return publicationRepository.findAllByUser(user);
    }
}
