package by.zakharyachnik.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "advertisements")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ad_id", nullable = false)
    private Integer id;

    @Column(nullable = false, name="description")
    private String description;
    @Column(nullable = false, name="post_date")
    private LocalDate postDate;
    @Column(nullable = false, name="price")
    private Integer price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToMany(mappedBy = "favoritePublications", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

    public Publication(String description, LocalDate postDate, int price, User user, Car car) {
        this.description = description;
        this.postDate = postDate;
        this.price = price;
        this.user = user;
        this.car = car;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", postDate=" + postDate +
                ", price=" + price +
                ", user=" + user +
                ", car=" + car +
                ", users=" + users +
                '}';
    }


}
