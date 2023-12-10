package com.m1a2st.simplebackendpractice.template;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author m1a2st
 * @Date 2023/8/20
 * @Version v1.0
 */
@Data
@AllArgsConstructor
public class UserIdGeneratorTestCase {
    private String displayName;
    private boolean isFeatureEnabled;
    private String firstName;
    private String lastName;
    private String expectedUserId;
}
