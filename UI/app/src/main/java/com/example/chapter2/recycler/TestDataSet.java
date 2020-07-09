package com.example.chapter2.recycler;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet {
    public static List<TestData> getData() {
        List<TestData> result = new ArrayList();
        result.add(new TestData("腾讯","辣酱突然不香了","tencent","刚刚"));
        result.add(new TestData("老干妈","不打广告","laoganma","昨天"));
        result.add(new TestData("字节跳动","全球创作与交流平台","bytedance","昨天"));
        result.add(new TestData("加多宝","我是正宗凉茶","jiaduobao","7-5"));
        result.add(new TestData("王老吉","我才是正宗凉茶","wanglaoji","7-5"));
        result.add(new TestData("美团外卖","送啥都快","meituan","6-28"));
        result.add(new TestData("饿了么","扫码领红包","eleme","6-24"));
        return result;
    }
}
