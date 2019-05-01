package com.example.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Native;
import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    //SQL native
    @Query(value = "SELECT * FROM User WHERE email = :email AND :email IS NOT NULL",
            nativeQuery = true)
    Collection<User> findEmail(String email);


    //Using JPQL
    @Query(value= "SELECT u FROM User u WHERE u.email =:email AND u.password =:password",
            nativeQuery = false)
    Collection<User> checkingEmailAndPassword(String email, String password);

}

