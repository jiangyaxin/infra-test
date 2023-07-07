package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.interview.GenerateParenthesis;
import com.jyx.feature.test.jdk.algorithm.interview.SpecialNumber;
import com.jyx.feature.test.jdk.algorithm.interview.UncommonWord;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jiangyaxin
 * @since 2022/7/27 16:21
 */
public class InterviewTests {

    @Test
    public void generateParenthesisTest(){
        assertThat(new GenerateParenthesis().generateParenthesis(3))
                .as("generateParenthesisTest")
                .isEqualTo(Lists.list("((()))","(()())","(())()","()(())","()()()"));
    }

    @Test
    public void isSpecialNumberTest(){
        assertThat(new SpecialNumber().isSpecialNumber(19))
                .as("isSpecialNumberTest")
                .isEqualTo(true);
        assertThat(new SpecialNumber().isSpecialNumber(2))
                .as("isSpecialNumberTest")
                .isEqualTo(false);
    }

    @Test
    public void uncommonWordTest(){
        assertThat(new UncommonWord().uncommonWord("this apple is sweet","this apple is sour"))
                .as("uncommonWordTest")
                .isEqualTo(new String[]{"sweet","sour"});
        assertThat(new UncommonWord().uncommonWord("apple apple","banana"))
                .as("uncommonWordTest")
                .isEqualTo(new String[]{"banana"});
    }
}
