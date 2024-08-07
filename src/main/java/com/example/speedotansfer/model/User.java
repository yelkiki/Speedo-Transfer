package com.example.speedotansfer.model;


import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.enums.Country;
import com.example.speedotansfer.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "users", indexes = @Index(columnList = "internalId", name = "internalId_idx", unique = true))
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @CreationTimestamp
    private final LocalDateTime creationTimeStamp = LocalDateTime.now();
    @UpdateTimestamp
    private final LocalDateTime updatedAt = LocalDateTime.now();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internalId;
    @Column(nullable = false)
    private UUID externalId;
    @Column(nullable = false)
    private String fullName;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    @Email
    private String email;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^(\\+201|01|00201)[0-2,5][0-9]{8}")
    private String phoneNumber;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Country country;
    @Column(nullable = false)
    private LocalDate birthdate;

    @OneToMany(mappedBy = "user")
    private List<Account> account;


    @OneToMany(mappedBy = "sender")
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private List<Transaction> receivedTransactions;

    @OneToMany(mappedBy = "favouriteUser")
    private List<Favourite> fav;

    // momken add role for extra bonus

    public UserDTO toDTO() {
        return UserDTO.builder()
                .userId(externalId)
                .fullName(fullName)
                .username(username)
                .email(email)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .birthDate(birthdate)
                .build();

    }


}
