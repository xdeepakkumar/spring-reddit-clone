package com.redditclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redditclone.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
