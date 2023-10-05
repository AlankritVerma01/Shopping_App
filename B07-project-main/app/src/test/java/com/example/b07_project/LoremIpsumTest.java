package com.example.b07_project;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.example.b07_project.util.LoremIpsum;

public class LoremIpsumTest {
    @Test
    public void test() {
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi ut", LoremIpsum.generate(10));
    }
}