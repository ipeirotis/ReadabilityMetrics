package com.ipeirotis.readability;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UserServiceTest.class, TextServiceTest.class,
		MetricServiceTest.class })
public class AllTests {

}
