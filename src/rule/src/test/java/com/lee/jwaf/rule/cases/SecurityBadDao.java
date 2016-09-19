/**
 * Project Name : jwaf-rule <br>
 * File Name : SecurityBadDao.java <br>
 * Package Name : com.lee.jwaf.rule.cases <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule.cases;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
 * ClassName : SecurityBadDao <br>
 * Description : dao should not annotated with @PreAuthorize <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Repository
@PreAuthorize("")
public class SecurityBadDao {

    @PreAuthorize("")
    public void daoFoo() {

    }
}
