package com.jyx.feature.test.jdk.crypto;

import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @author jiangyaxin
 * @since 2023/4/17 15:19
 */
public class JksTest {

    public KeyPair loadKeyPair(String storePath, String storePassword, String keyAlias, String keyPassword) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(storePath), storePassword.toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());
            PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();
            return new KeyPair(publicKey, privateKey);
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
}
