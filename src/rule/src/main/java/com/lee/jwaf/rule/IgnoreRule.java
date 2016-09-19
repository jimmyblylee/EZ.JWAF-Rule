/**
 * Project Name : jwaf-rule <br>
 * File Name : IgnoreRule.java <br>
 * Package Name : com.lee.jwaf.rule <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ClassName : IgnoreRule <br>
 * Description : Ignore the validation of the rule defination <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface IgnoreRule {

}
