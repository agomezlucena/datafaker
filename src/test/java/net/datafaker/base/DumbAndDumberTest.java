package net.datafaker.base;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DumbAndDumberTest extends AbstractBaseFakerTest {

    @Test
    void actor() {
        assertThat(faker.dumbAndDumber().actor()).isNotEmpty();
    }

    @Test
    void character() {
        assertThat(faker.dumbAndDumber().character()).isNotEmpty();
    }

    @Test
    void quote() {
        assertThat(faker.dumbAndDumber().quote()).isNotEmpty();
    }
}
