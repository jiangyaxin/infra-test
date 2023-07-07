package com.jyx.feature.test.jdk.algorithm.interview;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * @author jiangyaxin
 * @since 2022/7/30 16:07
 */
public class UncommonWord {

    public String[] uncommonWord(String a,String b){
        String[] aWordArray = a.split(" ");
        String[] bWordArray = b.split(" ");

        BiFunction<String, Integer, Integer> wordFrequencyFunction = (word, frequency) -> {
            if (frequency == null) {
                frequency = 1;
            } else {
                frequency += 1;
            }
            return frequency;
        };

        Map<String,Integer> aWordFrequencyMap = new HashMap<>(256);
        for(String bWord : aWordArray){
            aWordFrequencyMap.compute(bWord, wordFrequencyFunction);
        }

        Map<String,Integer> bWordFrequencyMap = new HashMap<>(256);
        for(String bWord : bWordArray){
            bWordFrequencyMap.compute(bWord, wordFrequencyFunction);
        }

        return Stream.concat(aWordFrequencyMap.entrySet().stream(), bWordFrequencyMap.entrySet().stream())
                .filter(entry -> {
                    String word = entry.getKey();
                    if (aWordFrequencyMap.containsKey(word) && bWordFrequencyMap.containsKey(word)) {
                        return false;
                    }
                    return aWordFrequencyMap.getOrDefault(word, 0) <= 1 && bWordFrequencyMap.getOrDefault(word, 0) <= 1;
                })
                .map(Map.Entry::getKey)
                .distinct()
                .toArray(String[]::new);
    }
}
