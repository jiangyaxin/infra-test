package com.jyx.infra.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author asa
 * @since 2021/11/5 23:33
 */
public class Maps {

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static class Builder<K, V> {
        private final Map<K, V> builderMap = new HashMap<>();

        public Builder<K, V> put(K k, V v) {
            builderMap.put(k, v);
            return this;
        }

        public Builder<K, V> putAll(Map<K, V> map) {
            builderMap.putAll(map);
            return this;
        }

        public HashMap<K, V> build() {
            return new HashMap<>(builderMap);
        }
    }
}
