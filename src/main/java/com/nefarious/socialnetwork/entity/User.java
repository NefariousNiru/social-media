package com.nefarious.socialnetwork.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a User Domain with properties:
 * id, email, password, username, firstname, lastname, dateOfBirth, isEmailVerified
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /** Primary key: generated UUID */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Unique email address, non-null and unique */
    @Column(nullable = false, unique = true)
    private String email;

    /** Hashed password, non-null */
    @Column(name = "password", nullable = false)
    private String password;

    /** Display name/login name, non-null and unique */
    @Column(name = "username", nullable = false, unique = true, length = 30)
    private String username;

    /** First Name, non-null */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /** Last Name, non-null */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /** User’s date of birth, stored as SQL DATE */
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    /** Whether the user has verified their email—default is false */
    @Builder.Default
    @Column(name = "is_email_verified", nullable = false)
    private boolean isEmailVerified = false;
}
