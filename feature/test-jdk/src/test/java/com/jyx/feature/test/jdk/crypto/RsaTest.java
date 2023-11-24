package com.jyx.feature.test.jdk.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author jiangyaxin
 * @since 2023/4/17 11:17
 */
@Slf4j
public class RsaTest {

    private static final String RSA = "RSA";

    @Test
    public void desTest() {
        KeyPair keyPair = generateKeyPair();

        String publicKey = getPublicKey(keyPair);
        String privateKey = getPrivateKey(keyPair);
        log.info("公钥 = {}", publicKey);
        log.info("私钥 = {}", privateKey);

        String source = "我是明文";
        log.info("明文 = {}", source);

        String target1 = encode(source, publicKey, true);
        log.info("公钥加密密文 = {}", target1);

        Assertions.assertEquals(source, decode(target1, privateKey, false));

        String target2 = encode(source, privateKey, false);
        log.info("私钥加密密文 = {}", target2);

        Assertions.assertEquals(source, decode(target2, publicKey, true));
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(2048);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return keyPair;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();

        return Base64.getEncoder().encodeToString(bytes);
    }

    private String getPrivateKey(KeyPair keyPair) {
        PrivateKey publicKey = keyPair.getPrivate();
        byte[] bytes = publicKey.getEncoded();

        return Base64.getEncoder().encodeToString(bytes);
    }

    public String encode(String source, String keyStr, boolean publicKey) {
        try {
            Key key = publicKey ? transferPublicKey(keyStr) : transferPrivateKey(keyStr);

            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(source.getBytes(StandardCharsets.UTF_8.name()));

            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String decode(String target, String keyStr, boolean publicKey) {
        try {
            Key key = publicKey ? transferPublicKey(keyStr) : transferPrivateKey(keyStr);

            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(target));

            return new String(bytes, StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private RSAPublicKey transferPublicKey(String publicKeyStr) {
        try {
            byte[] bytes = Base64.getDecoder().decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
            return publicKey;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private RSAPrivateKey transferPrivateKey(String privateKeyStr) {
        try {
            byte[] bytes = Base64.getDecoder().decode(privateKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
            return privateKey;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
