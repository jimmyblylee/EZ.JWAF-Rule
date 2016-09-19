/**
 * Project Name : jwaf-rule <br>
 * File Name : NameBadControllerxxx.java <br>
 * Package Name : com.lee.jwaf.rule.cases <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule.cases;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * ClassName : NameBadControllerxxx <br>
 * Description : controller should named end with "Controller" <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Controller
@Scope(value = SCOPE_PROTOTYPE)
public class NameBadControllerxxx {

}
