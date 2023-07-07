package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.ArrayList;
import java.util.List;

/**
 * 力扣 93. 复原 IP 地址
 * 有效 IP 地址 正好由四个整数（每个整数位于 0 到 255 之间组成，且不能含有前导 0），整数之间用 '.' 分隔。
 *
 * 例如："0.1.2.201" 和 "192.168.1.1" 是 有效 IP 地址，但是 "0.011.255.245"、"192.168.1.312" 和 "192.168@1.1" 是 无效 IP 地址。
 * 给定一个只包含数字的字符串 s ，用以表示一个 IP 地址，返回所有可能的有效 IP 地址，这些地址可以通过在 s 中插入 '.' 来形成。你 不能 重新排序或删除 s 中的任何数字。你可以按 任何 顺序返回答案。
 *
 * 示例 1：
 * 输入：s = "25525511135"
 * 输出：["255.255.11.135","255.255.111.35"]
 *
 * 示例 2：
 * 输入：s = "0000"
 * 输出：["0.0.0.0"]
 *
 * 示例 3：
 * 输入：s = "101023"
 * 输出：["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
 *
 * 提示：
 * 1 <= s.length <= 20
 * s 仅由数字组成
 * @author jiangyaxin
 * @since 2022/4/13 19:51
 */
public class L93RestoreIpAddress {

    private static final String POINT = ".";

    private static final char LOCAL_IP_BIT = '0';

    private static final int MAX = 255;

    private static final int PATH_LIMIT = 4-1;

    private static final int IP_LIMIT = 3;

    List<String> result = new ArrayList<>();

    public List<String> restoreIpAddresses(String s) {
        List<String> path = new ArrayList<>();
        restoreIpAddresses(path,s);
        return result;
    }

    public void restoreIpAddresses(List<String> throughPath,String remainS) {
        if(throughPath.size() == PATH_LIMIT && isLegal(remainS)){
            throughPath.add(remainS);
            result.add(String.join(POINT, throughPath));
            throughPath.remove(throughPath.size()-1);
            return;
        }
        int currentMaxLimit = remainS.length() - 1;
        int limit = Math.min(IP_LIMIT, currentMaxLimit);
        for(int splitCharAt = 1; splitCharAt <= limit ; splitCharAt ++){
            String pathNode = remainS.substring(0, splitCharAt);
            if(!isLegal(pathNode)){
                break;
            }
            String nextRemainS = remainS.substring(splitCharAt);
            throughPath.add(pathNode);
            restoreIpAddresses(throughPath,nextRemainS);
            throughPath.remove(throughPath.size()-1);
        }
    }

    private boolean isLegal(String str){
        long ipBit = Long.parseLong(str);
        if(ipBit > MAX || ipBit < 0){
            return false;
        }
        return str.length() <= 1 || str.charAt(0) != LOCAL_IP_BIT;
    }

}
