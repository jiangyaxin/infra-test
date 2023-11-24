package com.jyx.feature.test.jdk.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author jiangyaxin
 * @since 2023/4/17 11:17
 */
@Slf4j
public class MessageDigestTest {

    @Test
    public void desTest() {
        String source = "我是明文";
        log.info("明文 = {}", source);

        String target = digest(source, "md5");
        log.info("密文 = {}", target);

        Assertions.assertEquals(target, digest(source, "md5"));
    }

    public String digest(String source, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(source.getBytes(StandardCharsets.UTF_8.name()));
            byte[] bytes = messageDigest.digest();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
