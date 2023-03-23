package com.m1a2st.simplebackendpractice.user;

import com.m1a2st.simplebackendpractice.user.po.UserLoginDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserLoginRepository extends MongoRepository<UserLoginDocument, String> {

    Page<UserLoginDocument> findAllByUsernameOrderByCreateByDesc(String username, Pageable pageable);
}
