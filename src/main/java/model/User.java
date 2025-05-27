package model;

import enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false,unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public String getPassword() {return this.password;}
    public String getUsername() {return this.username;}
    public Role getRole() {return this.role;}
    public void setRole(Role role) {this.role = role;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}


}
