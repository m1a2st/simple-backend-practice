package com.m1a2st.simplebackendpractice.template;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author m1a2st
 * @Date 2023/8/20
 * @Version v1.0
 */

@SpringBootTest
public class TestTemplateJUnit {

    @TestTemplate
    @ExtendWith(UserIdGeneratorTestInvocationContextProvider.class)
    public void whenUserIdRequested_thenUserIdIsReturnedInCorrectFormat(UserIdGeneratorTestCase testCase) {
        UserIdService userIdGenerator = new UserIdServiceImpl(testCase.isFeatureEnabled());

        String actualUserId = userIdGenerator.generate(testCase.getFirstName(), testCase.getLastName());

        assertThat(actualUserId).isEqualTo(testCase.getExpectedUserId());
    }
}
