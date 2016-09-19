/**
 * Project Name : jwaf-rule <br>
 * File Name : ScopeBadDao.java <br>
 * Package Name : com.lee.jwaf.rule.cases <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule.cases;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * ClassName : ScopeBadDao <br>
 * Description : dao should be sigleton <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Repository
@Scope(value = SCOPE_PROTOTYPE)
public class ScopeBadDao {

}
