package com.m1a2st.simplebackendpractice.user;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {
}
