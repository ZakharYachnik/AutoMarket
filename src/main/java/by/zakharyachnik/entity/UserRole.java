package by.zakharyachnik.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "user_role")
public class  UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "role", nullable = false)
    private String role;

    public UserRole() {
    }

    public UserRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
