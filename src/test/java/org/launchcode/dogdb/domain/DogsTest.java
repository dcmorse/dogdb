package org.launchcode.dogdb.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.launchcode.dogdb.web.rest.TestUtil;

public class DogsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dogs.class);
        Dogs dogs1 = new Dogs();
        dogs1.setId(1L);
        Dogs dogs2 = new Dogs();
        dogs2.setId(dogs1.getId());
        assertThat(dogs1).isEqualTo(dogs2);
        dogs2.setId(2L);
        assertThat(dogs1).isNotEqualTo(dogs2);
        dogs1.setId(null);
        assertThat(dogs1).isNotEqualTo(dogs2);
    }
}
