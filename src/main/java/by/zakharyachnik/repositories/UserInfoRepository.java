package by.zakharyachnik.repositories;

import by.zakharyachnik.entity.User;
import by.zakharyachnik.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    UserInfo findByFullName(String searchValue);

    UserInfo findByPhoneNumber(String searchValue);
}
