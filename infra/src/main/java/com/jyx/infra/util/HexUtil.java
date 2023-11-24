package com.jyx.infra.util;

/**
 * @author jiangyaxin
 * @since 2023/11/7 9:55
 */
public class HexUtil {

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex.toUpperCase()).append(" ");
        }
        return sb.toString();
    }
}
