package org.debux.webmotion.pollmotion;

import org.debux.webmotion.unittest.WebMotionTestNG;
import org.testng.annotations.Test;

/**
 *
 * @author julien
 */
public class TestMainController extends WebMotionTestNG {
    
    @Test
    public void test() throws Exception {
        String result = createRequest("/").Get()
                .execute().returnContent().asString();
        System.out.println(result);
    }
    
}
