package com.jyx.feature.test.jdk.reflection.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Archforce
 * @since 2023/4/17 11:17
 */
@Slf4j
public class Des3Test {

    private static final String DES3 = "DESede";

    @Test
    public void desTest() {
        String keyStr = generateKeyStr();
        log.info("秘钥字符串 = {}", keyStr);

        String source = "我是明文";
        log.info("明文 = {}", source);

        String target = encode(source, keyStr);
        log.info("密文 = {}", target);

        Assertions.assertEquals(source, decode(target, keyStr));
    }

    public String generateKeyStr() {
        try {
            // 生成指定算法的密钥生成器,参数是算法名称
            KeyGenerator keyGenerator = KeyGenerator.getInstance(DES3);
            // 初始化密钥生成器,指定密钥生成器产生密钥的长度
            keyGenerator.init(168);
            // 生成一个SecretKey密钥对象
            SecretKey secretKey = keyGenerator.generateKey();
            // 返回一个密钥字节数组
            byte[] bytes = secretKey.getEncoded();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String encode(String source, String keyStr) {
        try {
            SecretKey secretKey = transferKey(keyStr);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(source.getBytes(StandardCharsets.UTF_8.name()));

            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String decode(String target, String keyStr) {
        try {
            SecretKey secretKey = transferKey(keyStr);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(target));

            return new String(bytes,StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private SecretKey transferKey(String keyStr) {
        try {
            byte[] bytes = Base64.getDecoder().decode(keyStr);
            // 生成 DES3 规则的密钥对象
            DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(bytes);
            // 获取一个密钥工厂实例
            SecretKeyFactory factory = SecretKeyFactory.getInstance(DES3);
            // 符合符合DES算法的密钥
            return factory.generateSecret(deSedeKeySpec);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
