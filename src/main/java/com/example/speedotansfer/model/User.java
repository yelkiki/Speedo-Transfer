package com.example.speedotansfer.model;


import com.example.speedotansfer.dto.customerDTOs.UserDTO;
import com.example.speedotansfer.enums.Country;
import com.example.speedotansfer.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // 5aleeha rakam w auto increment (same LENGTH)
    @Id
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastname;

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

    @Column(nullable = false,unique = true)
    @Pattern(regexp = "^(\\+201|01|00201)[0-2,5][0-9]{8}")
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Country country;

    @Column(nullable = false)
    private LocalDate birthdate;

    @CreationTimestamp
    private final LocalDateTime creationTimeStamp = LocalDateTime.now();

    @UpdateTimestamp
    private final LocalDateTime updatedAt = LocalDateTime.now();

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    private Account account;


    @OneToMany(mappedBy = "sender")
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private List<Transaction> receivedTransactions;

    @OneToMany(mappedBy = "favouriteUser")
    private List<Favourite> fav;

    // momken add role for extra bonus

    public UserDTO toDTO() {
        return UserDTO.builder()
                .customerId(id)
                .firstName(firstName)
                .lastName(lastname)
                .username(username)
                .email(email)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .birthDate(birthdate)
                .accNumber(account.getAccountNumber())
                .build();

    }


}
