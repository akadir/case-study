package io.github.akadir.casestudy;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CaseStudyTest {

    @Test
    public void create() {
        Assertions.assertThatCode(CaseStudy::new)
                .as("Crete CaseStudy should not throw exception")
                .doesNotThrowAnyException();
    }
}
