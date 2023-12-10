package com.m1a2st.simplebackendpractice.template;

import lombok.AllArgsConstructor;

/**
 * @Author m1a2st
 * @Date 2023/8/20
 * @Version v1.0
 */
@AllArgsConstructor
public class UserIdServiceImpl implements UserIdService {
    
    private boolean isFeaturedEnabled;
    
    public String generate(String firstname, String lastname){
        String concatName = firstname.charAt(0) + lastname;
        return isFeaturedEnabled ? "test".concat(concatName) : concatName;
    }
}
