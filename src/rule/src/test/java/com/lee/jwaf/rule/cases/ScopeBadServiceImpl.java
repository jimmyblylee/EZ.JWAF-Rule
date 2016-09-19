/**
 * Project Name : jwaf-rule <br>
 * File Name : ScopeBadServiceImpl.java <br>
 * Package Name : com.lee.jwaf.rule.cases <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule.cases;

import org.springframework.stereotype.Service;

/**
 * ClassName : ScopeBadServiceImpl <br>
 * Description : service should not be sigleton <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
@Service
public class ScopeBadServiceImpl implements ComponentBadService {

}
