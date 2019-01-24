package com.claude.sgkata.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ListUtilsTest {

    @Test
    public void partition() {
        Assertions.assertThat(ListUtils.partition(Arrays.asList(1, 2, 3, 4, 5, 6), 3).size())
                .isEqualTo(2);
        Assertions.assertThat(ListUtils.partition(Arrays.asList(1, 2, 3, 4, 5, 6, 7), 2).size())
                .isEqualTo(4);
    }
}