/**
 * Project Name : jwaf-rule <br>
 * File Name : SecurityGoodServiceImpl.java <br>
 * Package Name : com.lee.jwaf.rule.cases <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule.cases;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * ClassName : SecurityGoodServiceImpl <br>
 * Description : Good service not annotated with @PreAuthorize <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Service
@PreAuthorize("")
@Scope(value = SCOPE_PROTOTYPE)
public class SecurityGoodServiceImpl implements SecurityGoodService {

    /*
     * (non-Javadoc)
     * @see com.lee.jwaf.rule.cases.SecurityGoodService#foo()
     */
    @Override
    @PreAuthorize("")
    public void foo() {

    }
}
