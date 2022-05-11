package info.tinywit;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class FeatureExtractorTest {
    private Map<String, String> testTable;

    @Before
    public void prepare() {
        testTable = new LinkedHashMap<>();
        testTable.put("这个老板不怎么骗人，但是一次上门体验的经历还是让我非常后悔。", "不.骗人 -1;非常.后悔 5");
        testTable.put("这块蛋糕没有异味。", "没.异味 -1");
        testTable.put("这个电饭煲感觉不怎么有惊喜，买来以后感觉很后悔。", "不.有.惊喜 1;后悔 2");
        testTable.put("这台车的发动机有很明显的且让人厌烦的异响，我本来高高兴兴的，结果让我喜爱不起来", "有.异响 1;本来.高兴 1;喜爱 -1");
        testTable.put("我真是后悔不早点买下来，不是我不爱你，是因为一直没赚到钱。", "悔.不.早.买 -5;不.不.爱你 -0.01");
    }

    @Test
    public void testExtract() {
        for (Map.Entry<String, String> entry : testTable.entrySet()) {
            Assert.assertEquals(FeatureExtractor.extract(entry.getKey()), entry.getValue());
        }
    }
}
