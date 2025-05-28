 OVERVIEW: What’s Happening in Milestone 2?
🎯 Goal: Register a new user (Admin or Cashier)

🧭 FLOW OF DATA
Client sends a registration request to:

arduino
Copy code
POST /register
{
  "username": "john",
  "password": "secret",
  "role": "ADMIN"
}
The request hits the AuthController.

AuthController calls the UserService.

UserService hashes the password and calls UserRepository.

UserRepository saves the User object into the PostgreSQL database.

📦 CLASSES INVOLVED
Let’s explain the purpose and role of each class.

1. ✅ User — The Entity Class
java
Copy code
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
🔹 Purpose:

Represents a row in the database.

Maps Java fields to database columns.

Used by Spring JPA to read/write data from the users table.

2. ✅ Role — The Enum
java
Copy code
public enum Role {
    ADMIN,
    CASHIER
}
🔹 Purpose:

Limits roles to two predefined options: ADMIN or CASHIER.

Ensures no invalid roles are assigned.

3. ✅ UserRepository — The Data Access Layer
java
Copy code
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
🔹 Purpose:

Provides access to the database using auto-generated queries.

We didn’t implement any logic here — Spring does it for us.

Offers methods like save, findById, delete, and our custom findByUsername.

4. ✅ UserService — The Business Logic Layer
java
Copy code
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
🔹 Purpose:

Contains the core logic for registration.

Encodes passwords (security).

Calls the repository to save the user.

5. ✅ AuthController — The HTTP Entry Point
java
Copy code
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }
}
🔹 Purpose:

Handles HTTP requests from frontend clients.

Accepts a User JSON payload.

Forwards it to the service.

Returns the saved user (or a status code).

🔄 DATA FLOW DIAGRAM
pgsql
Copy code
[Client/Postman]
     |
     |  POST /auth/register  { username, password, role }
     ↓
[AuthController]
     |
     |  Calls registerUser(user)
     ↓
[UserService]
     |
     |  - Hash password
     |  - Save user
     ↓
[UserRepository]
     |
     |  Talks to PostgreSQL using Spring Data JPA
     ↓
[Database]
     |
     |  User is saved in users table
     ↑
     |  Saved User object returned (with ID)
🔒 Why Is This Design Important?
Layer	Why We Use It
Controller	Handles HTTP requests & responses
Service	Handles business logic (like password hashing)
Repository	Handles database access
Entity	Represents the data model
Enum	Prevents bad input values (like "CEO" role)

This design follows the Separation of Concerns principle. Each layer has its job, so your code is clean, testable, and scalable.