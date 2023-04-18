package com.jyx.feature.test.jdk.reflection.crypto;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * @author Archforce
 * @since 2023/4/17 16:37
 */
public class SslTest {

    public void sslTest() throws Exception {
        String keyStoreFile = "D:\\code\\ttt.ks";
        String password = "poiuyt";

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream in = new FileInputStream(keyStoreFile);
        ks.load(in, password.toCharArray());
        ;

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, tmf.getTrustManagers(), null);
    }
}
