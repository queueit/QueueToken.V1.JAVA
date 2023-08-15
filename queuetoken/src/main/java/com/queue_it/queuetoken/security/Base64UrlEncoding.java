package com.queue_it.queuetoken.security;

import java.util.Base64;

public class Base64UrlEncoding {

    private Base64UrlEncoding() {
    }

    public static String encode(byte[] input) {
        return Base64.getEncoder().encodeToString(input)
                .replaceAll("\\+", "-")
                .replaceAll("/", "_")
                .replaceAll("=", "");
    }

    public static byte[] decode(String input) {
        StringBuilder inputStringBuilder = new StringBuilder(input);

        for (int i = 0; i < inputStringBuilder.length(); i++) {
            if (inputStringBuilder.charAt(i) == '-') {
                inputStringBuilder.replace(i, i, "+");
            } else if (inputStringBuilder.charAt(i) == '_') {
                inputStringBuilder.replace(i, i, "/");
            }
        }

        int padding = inputStringBuilder.length() % 4;

        if (padding == 3) {
            padding = 1;
        }

        switch (padding) {
            case 0:
                return Base64.getDecoder().decode(inputStringBuilder.toString());
            case 1:
            case 3:
                padStringBuilderRight(inputStringBuilder, '=', 1);
                return Base64.getDecoder().decode(inputStringBuilder.toString());
            case 2:
                padStringBuilderRight(inputStringBuilder, '=', 2);
                return Base64.getDecoder().decode(inputStringBuilder.toString());
            default:
                break;
        }

        return new byte[0];
    }

    private static StringBuilder padStringBuilderRight(StringBuilder stringBuilder, char paddingChar,
            int paddingCount) {

        if (paddingCount > 1) {
            for (int i = 0; i < paddingCount; i++) {
                stringBuilder.append(paddingChar);
            }
        }
        return stringBuilder;
    }
}
