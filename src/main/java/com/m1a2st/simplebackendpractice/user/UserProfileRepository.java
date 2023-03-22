package com.m1a2st.simplebackendpractice.user;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {

    Optional<UserProfile> findByUsername(String username);

    boolean existsByUsername(String username);
}
