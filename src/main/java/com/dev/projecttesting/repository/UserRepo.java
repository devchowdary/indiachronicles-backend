package com.dev.projecttesting.repository;

import com.dev.projecttesting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long>
{
    Optional<User> findByUserNameOrEmail(String userName,String email);


}
