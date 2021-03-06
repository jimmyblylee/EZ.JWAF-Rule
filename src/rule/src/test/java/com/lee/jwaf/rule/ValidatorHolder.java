/**
 * Project Name : jwaf-rule <br>
 * File Name : ValidatorHolder.java <br>
 * Package Name : com.lee.jwaf.rule <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName : ValidatorHolder <br>
 * Description : Hold the RuleValidator <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Component
@IgnoreRule
public class ValidatorHolder implements BeanFactoryPostProcessor {

    private ConfigurableListableBeanFactory factory;
    private String pts;

    public void setPackagesToScan(String packagesToScan) {
        this.pts = packagesToScan;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0) throws BeansException {
        this.factory = arg0;
    }

    public RuleValidator get() {
        RuleValidator validator = new RuleValidator();
        validator.factory = this.factory;
        validator.setPackagesToScan(pts);
        return validator;
    }
}
