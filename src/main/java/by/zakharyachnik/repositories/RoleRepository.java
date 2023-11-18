package by.zakharyachnik.repositories;

import by.zakharyachnik.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<UserRole, Integer> {
    UserRole findById(int id);
    UserRole findByRole(String role);
}
