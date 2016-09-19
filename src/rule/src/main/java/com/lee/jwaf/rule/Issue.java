/**
 * Project Name : jwaf-rule <br>
 * File Name : Issue.java <br>
 * Package Name : com.lee.jwaf.rule <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule;

/**
 * ClassName : Issue <br>
 * Description : bad code issue by rule <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public class Issue {

    protected String className;
    protected String beanName;
    protected String message;
    protected String advise;

    public Issue(String className, String beanName, String message, String advise) {
        this.className = className;
        this.beanName = beanName;
        this.message = message;
        this.advise = advise;
    }
    
    @Override
    public String toString() {
        return className;
    }
}
