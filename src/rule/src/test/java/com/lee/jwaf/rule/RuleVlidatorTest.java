/**
 * Project Name : jwaf-rule <br>
 * File Name : RuleVlidatorTest.java <br>
 * Package Name : com.lee.jwaf.rule <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lee.jwaf.rule.cases.ComponentBadController;
import com.lee.jwaf.rule.cases.ComponentBadDao;
import com.lee.jwaf.rule.cases.ComponentBadServiceImpl;
import com.lee.jwaf.rule.cases.IgnoreRuleCase;
import com.lee.jwaf.rule.cases.InterfaceBadServiceImpl;
import com.lee.jwaf.rule.cases.NameBadControllerxxx;
import com.lee.jwaf.rule.cases.NameBadRepository;
import com.lee.jwaf.rule.cases.NameBadService;
import com.lee.jwaf.rule.cases.ScopeBadController;
import com.lee.jwaf.rule.cases.ScopeBadDao;
import com.lee.jwaf.rule.cases.ScopeBadServiceImpl;
import com.lee.jwaf.rule.cases.SecurityBadController;
import com.lee.jwaf.rule.cases.SecurityBadDao;
import com.lee.jwaf.rule.cases.SecurityGoodServiceImpl;

/**
 * ClassName : RuleVlidatorTest <br>
 * Description : unit test of {@link RuleValidator} <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-rule-issue.xml" })
public class RuleVlidatorTest {

    @Resource
    private ValidatorHolder holder;

    @Test
    public void testValidateSecurityProxy() {
        List<Issue> issues = holder.get().validateSecurityProxy();
        assertThat(issues.size(), is(4));
        assertThat(issues.toString(), containsString(SecurityBadController.class.getName()));
        assertThat(issues.toString(), containsString(SecurityBadDao.class.getName()));
        assertThat(issues.toString(), not(containsString(SecurityGoodServiceImpl.class.getName())));
    }

    @Test
    public void testValidateComponentsAnnotation() {
        List<Issue> issues = holder.get().validateComponentsAnnotation();
        assertThat(issues.size(), is(3));
        assertThat(issues.toString(), containsString(ComponentBadController.class.getName()));
        assertThat(issues.toString(), containsString(ComponentBadDao.class.getName()));
        assertThat(issues.toString(), containsString(ComponentBadServiceImpl.class.getName()));
    }

    @Test
    public void testValidateDaosName() {
        List<Issue> issues = holder.get().validateDaosName();
        assertThat(issues.size(), is(1));
        assertThat(issues.toString(), containsString(NameBadRepository.class.getName()));
    }

    @Test
    public void testValidateDaosScope() {
        List<Issue> issues = holder.get().validateDaosScope();
        assertThat(issues.size(), is(1));
        assertThat(issues.toString(), containsString(ScopeBadDao.class.getName()));
    }

    @Test
    public void testValidateServicesInterface() {
        List<Issue> issues = holder.get().validateServicesInterface();
        assertThat(issues.size(), is(1));
        assertThat(issues.toString(), containsString(InterfaceBadServiceImpl.class.getName()));
    }

    @Test
    public void testValidateServicesName() {
        List<Issue> issues = holder.get().validateServicesName();
        assertThat(issues.size(), is(1));
        assertThat(issues.toString(), containsString(NameBadService.class.getName()));
    }

    @Test
    public void testValidateServicesScope() {
        List<Issue> issues = holder.get().validateServicesScope();
        assertThat(issues.size(), is(1));
        assertThat(issues.toString(), containsString(ScopeBadServiceImpl.class.getName()));
    }

    @Test
    public void testvalidateControllersName() {
        List<Issue> issues = holder.get().validateControllersName();
        assertThat(issues.size(), is(1));
        assertThat(issues.toString(), containsString(NameBadControllerxxx.class.getName()));
    }

    @Test
    public void testvalidateControllersScope() {
        List<Issue> issues = holder.get().validateControllersScope();
        assertThat(issues.size(), is(1));
        assertThat(issues.toString(), containsString(ScopeBadController.class.getName()));
    }

    @Test
    public void testIgnoreRole() {
        List<Issue> issues = new LinkedList<>();
        issues.addAll(holder.get().validateComponentsAnnotation());
        issues.addAll(holder.get().validateScopeDefinitions());
        issues.addAll(holder.get().validateNameDefinitions());
        issues.addAll(holder.get().validateSecurityProxy());
        issues.addAll(holder.get().validateControllerProxy());
        issues.addAll(holder.get().validateServicesInterface());
        assertThat(issues.toString(), not(containsString(IgnoreRuleCase.class.getName())));
    }
}
