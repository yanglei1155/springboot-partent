package com.insigma;

import com.insigma.po.soldier.SysRegional;
import com.insigma.service.SysRegionalService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VeteransplacementProjectApplicationTests {
  @Autowired
  private SysRegionalService sysRegionalService;
	@Test
	public void contextLoads() {
		//List<SysRegional> fullNameOfAllArea = sysRegionalService.getFullNameOfAllArea();

	}

}
