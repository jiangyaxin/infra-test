package com.jyx.feature.test.jdk.reflection.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Archforce
 * @since 2023/4/17 11:17
 */
@Slf4j
public class SignatureTest {

    private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

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

        String sign = sign(source, privateKey);
        log.info("签名 = {}", sign);

        Assertions.assertTrue(varify(source, sign, publicKey));
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

    public String sign(String source, String privateStr) {
        try {
            RSAPrivateKey privateKey = transferPrivateKey(privateStr);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(source.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = signature.sign();

            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean varify(String source, String sign, String publicKeyStr) {
        try {
            RSAPublicKey publicKey = transferPublicKey(publicKeyStr);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(source.getBytes(StandardCharsets.UTF_8));

            return signature.verify(Base64.getDecoder().decode(sign));
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
