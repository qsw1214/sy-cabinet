package org.linlinjava.litemall.db;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linlinjava.litemall.db.service.LitemallRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Test01 {
    @Autowired
    private LitemallRegionService regionService;

    @Test
    public void test() {
        Integer cid = 420102;
        Integer pid = regionService.selectByCid(cid);
        System.out.println(pid);
    }

}
