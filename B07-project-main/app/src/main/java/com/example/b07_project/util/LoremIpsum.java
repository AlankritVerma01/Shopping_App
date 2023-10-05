package com.example.b07_project.util;

import java.util.Arrays;

public class LoremIpsum {
    private static final String[] loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi ut malesuada dui. Ut pellentesque elementum erat, at cursus mauris imperdiet eget. Nullam venenatis ac sapien eu iaculis. Integer eget dictum ipsum. Vivamus efficitur tellus sit amet neque semper, eu placerat nisi luctus. Etiam vitae condimentum leo, malesuada fermentum turpis. In rhoncus vestibulum nisl, a volutpat metus ullamcorper eu. Ut eleifend eleifend arcu.".split(" ");
    public static String generate(int n) {
        if (n > loremIpsum.length) {
            throw new IllegalArgumentException(String.format("Cannot generate more than %d lipsum elements", loremIpsum.length));
        }
        return String.join(" ", Arrays.copyOfRange(loremIpsum, 0, n));
    }
}
