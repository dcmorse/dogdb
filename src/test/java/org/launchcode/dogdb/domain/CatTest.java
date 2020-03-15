package org.launchcode.dogdb.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.launchcode.dogdb.web.rest.TestUtil;

public class CatTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cat.class);
        Cat cat1 = new Cat();
        cat1.setId(1L);
        Cat cat2 = new Cat();
        cat2.setId(cat1.getId());
        assertThat(cat1).isEqualTo(cat2);
        cat2.setId(2L);
        assertThat(cat1).isNotEqualTo(cat2);
        cat1.setId(null);
        assertThat(cat1).isNotEqualTo(cat2);
    }
}
