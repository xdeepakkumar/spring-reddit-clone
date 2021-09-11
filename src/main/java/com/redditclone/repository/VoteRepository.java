package com.redditclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redditclone.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
