/**
 * Project Name : jwaf-rule <br>
 * File Name : InterfaceBadServiceImpl.java <br>
 * Package Name : com.lee.jwaf.rule.cases <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule.cases;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * ClassName : InterfaceBadServiceImpl <br>
 * Description : service should implement a interface named end with "Service" <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Service
@Scope(value=SCOPE_PROTOTYPE)
public class InterfaceBadServiceImpl {

}
