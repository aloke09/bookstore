package com.bridgelabz.user_microservice1.repository;

import com.bridgelabz.user_microservice1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    User findByEmail(String email);
}
