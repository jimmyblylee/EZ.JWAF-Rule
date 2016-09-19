/**
 * Project Name : jwaf-rule <br>
 * File Name : RuleValidatorScanTest.java <br>
 * Package Name : com.lee.jwaf.rule <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ClassName : RuleValidatorScanTest <br>
 * Description : unit test for RuleValidator with scan <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-rule-issue-scan.xml" })
public class RuleValidatorScanTest {
    
    @Resource(name = "validator1")
    private ValidatorHolder validator1;
    @Resource(name = "validator2")
    private ValidatorHolder validator2;
    @Resource(name = "validator3")
    private ValidatorHolder validator3;
    
    @Test
    public void testValidate() {
        List<Issue> issues = validator1.get().validateComponentsAnnotation();
        assertThat(issues.size(), is(3));
        issues = validator2.get().validateComponentsAnnotation();
        assertThat(issues.size(), is(0));
        issues = validator3.get().validateComponentsAnnotation();
        assertThat(issues.size(), is(3));
    }
}
