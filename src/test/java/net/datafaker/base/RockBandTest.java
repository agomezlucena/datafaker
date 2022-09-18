package net.datafaker.base;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RockBandTest extends AbstractBaseFakerTest {

    @Test
    void name() {
        assertThat(faker.rockBand().name()).matches("([\\w'/.,&]+ ?)+");
    }
}
