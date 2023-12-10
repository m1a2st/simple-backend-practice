package com.m1a2st.simplebackendpractice.template;

import org.junit.jupiter.api.extension.*;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * @Author m1a2st
 * @Date 2023/8/20
 * @Version v1.0
 */
public class UserIdGeneratorTestInvocationContextProvider implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        boolean featureDisabled = false;
        boolean featureEnabled = true;

        return Stream.of(
                featureDisabledContext(
                        new UserIdGeneratorTestCase(
                                "test 1",
                                featureDisabled,
                                "John",
                                "Smith",
                                "JSmith")),
                featureEnabledContext(
                        new UserIdGeneratorTestCase(
                                "test 2",
                                featureEnabled,
                                "John",
                                "Smith",
                                "testJSmith"))
        );
    }

    private TestTemplateInvocationContext featureDisabledContext(UserIdGeneratorTestCase userIdGeneratorTestCase) {
        return new TestTemplateInvocationContext() {

            @Override
            public String getDisplayName(int invocationIndex) {
                return userIdGeneratorTestCase.getDisplayName();
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return asList(
                        new GenericTypedParameterResolver<>(userIdGeneratorTestCase),
                        (BeforeTestExecutionCallback) extensionContext -> System.out.println("BeforeTestExecutionCallback:Disabled context"),
                        (AfterTestExecutionCallback) extensionContext -> System.out.println("AfterTestExecutionCallback:Disabled context")
                );
            }
        };
    }

    private TestTemplateInvocationContext featureEnabledContext(
            UserIdGeneratorTestCase userIdGeneratorTestCase) {
        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return userIdGeneratorTestCase.getDisplayName();
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return asList(
                        new GenericTypedParameterResolver<>(userIdGeneratorTestCase),
//                        new DisabledOnQAEnvironmentExtension(),
                        new BeforeEachCallback() {
                            @Override
                            public void beforeEach(ExtensionContext extensionContext) {
                                System.out.println("BeforeEachCallback:Enabled context");
                            }
                        },
                        new AfterEachCallback() {
                            @Override
                            public void afterEach(ExtensionContext extensionContext) {
                                System.out.println("AfterEachCallback:Enabled context");
                            }
                        }
                );
            }
        };
    }
}
