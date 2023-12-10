package com.m1a2st.simplebackendpractice.user.repository;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {

    Optional<UserProfile> findByUsername(String username);

    boolean existsByUsername(String username);
    
}
