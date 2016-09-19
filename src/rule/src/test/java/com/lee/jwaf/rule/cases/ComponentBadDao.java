/**
 * Project Name : jwaf-rule <br>
 * File Name : ComponentBadDao.java <br>
 * Package Name : com.lee.jwaf.rule.cases <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule.cases;

import org.springframework.stereotype.Component;

/**
 * ClassName : ComponentBadDao <br>
 * Description : bean named end with "Dao" should not annotated with {@link Component} <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Component
public class ComponentBadDao {

}
