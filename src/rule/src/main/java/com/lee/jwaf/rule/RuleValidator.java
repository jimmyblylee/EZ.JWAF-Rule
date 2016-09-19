/**
 * Project Name : jwaf-rule <br>
 * File Name : RuleValidator.java <br>
 * Package Name : com.lee.jwaf.rule <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.rule;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * ClassName : RuleValidator <br>
 * Description : check whether the Componet are defined under the rule of scope <br>
 * <h1>Rules:</h1>
 * <h2>1. Beans' Annotation rules</h2> <br>
 * <ul>
 * <li><b>Controller Layer</b> beans must only annotated by {@link Controller}.</li>
 * <li><b>Service Layer</b> beans must only annotated by {@link Service}.</li>
 * <li><b>Dao Layer</b> beans must only annotated by {@link Repository}.</li>
 * </ul>
 * <h2>2. Beans' Scope rules</h2> <br>
 * <ul>
 * <li><b>Controllers'</b> Scope must be <b>prototype</b> with annotation {@link Scope} with param {@link ConfigurableBeanFactory#SCOPE_PROTOTYPE}.</li>
 * <li><b>Services'</b> Scope must be <b>prototype</b> with annotation {@link Scope} with param {@link ConfigurableBeanFactory#SCOPE_PROTOTYPE}</li>
 * <li><b>Daos'</b> Scope must be <b>singleton</b> with annotation {@link Scope} with param {@link ConfigurableBeanFactory#SCOPE_SINGLETON}</li>
 * </ul>
 * <h2>3. Beans' Name rules</h2> <br>
 * <ul>
 * <li><b>Controllers'</b> name must only end with 'Controller' like 'FooController'.</li>
 * <li><b>Services'</b> name must only end with <b>'ServiceImpl'</b> like 'FooServiceImpl'.</li>
 * <li><b>Daos'</b> name must only end with 'Dao' like 'FooDao'.</li>
 * </ul>
 * <h2>4. Security proxy rules</h2> <br>
 * Method security validation with http session, <b>must only be cut at service layer</b> with annotation
 * like @PreAuthorize(\"hasAnyAuthority(['ADD_USER','LOGIN','DEL_GROUP'])\").
 * <h2>5. Controller proxy rules</h2> <br>
 * <b>Controller must not be proxied by any way</b>. If you set some AOP login at controller layer, please remove it, or cut it into service layer.
 * <h2>6. Service Interface rules</h2> <br>
 * <b>Services must implement an interface named end with 'Service'</b> like 'FooServiceImpl' implemnting 'FooService'. <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public class RuleValidator implements BeanFactoryPostProcessor {

    protected ConfigurableListableBeanFactory factory;
    private Logger log = LoggerFactory.getLogger(getClass());

    private final static String CNS_ADVISE_SINGLETON = "Please add @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) to the type definition";
    private final static String CNS_ADVISE_PROTOTYPE = "Please add @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) to the type definition";

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.
     * ConfigurableListableBeanFactory)
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("###########################################################");
        log.info("Validating the BeanDefinition.");
        log.info("");
        this.factory = beanFactory;
        List<Issue> issues = new LinkedList<>();
        issues.addAll(validateComponentsAnnotation());
        issues.addAll(validateScopeDefinitions());
        issues.addAll(validateNameDefinitions());
        issues.addAll(validateSecurityProxy());
        issues.addAll(validateControllerProxy());
        issues.addAll(validateServicesInterface());
        if (issues.size() == 0) {
            log.info("Result: validation success, and there is no bad codes.");
        } else {
            for (Issue issue : issues) {
                printIssue(issue.className, issue.className, issue.message, issue.advise);
            }
            printRule();
            log.error("Result: validation failed!, Please change code fllowing the advise above.");
        }
        log.info("");
        log.info("Validation of BeanDefinition done");
        log.info("###########################################################");
        if (issues.size() > 0) { throw new BeanDefinitionValidationException("There are beans with bad Scope Definition"); }
    }

    private void printRule() {
        log.info("Rules: ");
        log.info("1. Beans' Annotation rules:");
        log.info("    Controller Layer beans must only annotated by @Controller.");
        log.info("    Service Layer beans must only annotated by @Service.");
        log.info("    Dao Layer beans must only annotated by @Repository.");
        log.info("");
        log.info("2. Beans' Scope rules:");
        log.info("    Controllers' Scope must be prototype with annotation @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE).");
        log.info("    Services' Scope must be prototype with annotation @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE).");
        log.info("    Daos' Scope must be singleton with annotation @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON).");
        log.info("");
        log.info("3. Beans' Name rules:");
        log.info("    Controllers' name must only end with 'Controller' like 'FooController'.");
        log.info("    Services' name must only end with 'ServiceImpl' like 'FooServiceImpl'.");
        log.info("    Daos' name must only end with 'Dao' like 'FooDao'.");
        log.info("");
        log.info("4. Security proxy rules:");
        log.info("    Method security validation with http session, "
                + "must only be cut at service layer with annotation like @PreAuthorize(\"hasAnyAuthority(['ADD_USER','LOGIN','DEL_GROUP'])\").");
        log.info("");
        log.info("5. Controller proxy rules:");
        log.info("    Controller must not be proxied by any way. "
                + "If you set some AOP login at controller layer, please remove it, or cut it into service layer.");
        log.info("");
        log.info("6. Service Interface rules:");
        log.info("    Services must implement an interface named end with 'Service' like 'FooServiceImpl' implemnting 'FooService'.");
        log.info("");
    }

    protected void printIssue(String type, String name, String issue, String advise) {
        log.error("Bad code found:");
        log.error("bean: {}", type);
        log.error("name: \"{}\"", name);
        log.error("issue: {}", issue);
        log.error("advise: {}", advise);
        log.error("");
    }

    /**
     * Description : check whether target type should ignore rule validation <br>
     * Create Time: Apr 15, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @param type the type you want check
     * @return true for the target type wants ignore the rule check
     */
    private boolean isIgnore(Class<?> type) {
        return type.getAnnotation(IgnoreRule.class) != null;
    }

    /**
     * Description : target type's scope is defined by "singleton" <br>
     * Create Time: Apr 15, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @param type the type you want check
     * @return true for the bean is defined by Singleton Scope or no Scope defined with singleton by default
     */
    private boolean isScopeSingleton(Class<?> type) {
        Scope scope = type.getAnnotation(Scope.class);
        return scope == null || ConfigurableBeanFactory.SCOPE_SINGLETON.equals(scope);
    }

    /**
     * Description : validate, controller service and dao's scope definition <br>
     * Create Time: Apr 15, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @see #validateControllersScope()
     * @see #validateServicesScope()
     * @see #validateDaosScope()
     * @return the list for issues
     */
    protected List<Issue> validateScopeDefinitions() {
        List<Issue> issues = new LinkedList<Issue>();
        issues.addAll(validateControllersScope());
        issues.addAll(validateServicesScope());
        issues.addAll(validateDaosScope());
        return issues;
    }

    /**
     * Description : validate whether the controller, service and dao are named by rule <br>
     * Create Time: Apr 15, 2016 <br>
     * Create by : xiangyu_li@asdc.com.cn <br>
     *
     * @see #validateControllersName()
     * @see #validateServicesName()
     * @see #validateDaosName()
     * @return the list for issues
     */
    protected List<Issue> validateNameDefinitions() {
        List<Issue> issues = new LinkedList<Issue>();
        issues.addAll(validateControllersName());
        issues.addAll(validateServicesName());
        issues.addAll(validateDaosName());
        return issues;
    }

    /**
     * Description : validate controller scope definition <br>
     * The controller's scope should be "prototype" <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the list for issues
     */
    protected List<Issue> validateControllersScope() {
        List<Issue> issues = new LinkedList<Issue>();
        String[] names = factory.getBeanNamesForAnnotation(Controller.class);
        for (String name : names) {
            Class<?> type = factory.getType(name);
            if (!isIgnore(type) && isScopeSingleton(type)) {
                issues.add(new Issue(type.getName(), name, "It's a controller. But the Scope is Singleton.", CNS_ADVISE_PROTOTYPE));
            }
        }
        return issues;
    }

    /**
     * Description : validate whether the controller is named by "*Controller" <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the list for issues
     */
    protected List<Issue> validateControllersName() {
        List<Issue> issues = new LinkedList<Issue>();
        String[] names = factory.getBeanNamesForAnnotation(Controller.class);
        for (String name : names) {
            Class<?> type = factory.getType(name);
            if (!isIgnore(type) && !type.getName().endsWith("Controller")) {
                issues.add(new Issue(type.getName(), name, "It's a controller. But it's not named end with \"Controller\".",
                        "Please rename it like " + type.getName() + "Controller"));
            }
        }
        return issues;
    }

    /**
     * Description : validate whether the controller is wrapped with a proxy <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the list for issues
     */
    protected List<Issue> validateControllerProxy() {
        List<Issue> issues = new LinkedList<Issue>();
        String[] names = factory.getBeanNamesForAnnotation(Controller.class);
        for (String name : names) {
            Object bean = factory.getBean(name);
            if (AopUtils.isAopProxy(bean)) {
                issues.add(new Issue(((Advised) bean).getTargetClass().getName(), name, "It's a proxied Controller, And a controller should not be proxied. ",
                        "Please verify it. Maybe it's been configed by Security or Other AOP."));
            }
        }
        return issues;
    }

    /**
     * Description : validate whether the servie is named by "*ServiceImpl" <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the list for issues
     */
    protected List<Issue> validateServicesScope() {
        List<Issue> issues = new LinkedList<Issue>();
        String[] names = factory.getBeanNamesForAnnotation(Service.class);
        for (String name : names) {
            Class<?> type = factory.getType(name);
            if (!isIgnore(type) && isScopeSingleton(type)) {
                issues.add(new Issue(type.getName(), name, "It's a service. But the Scope is Singleton.", CNS_ADVISE_PROTOTYPE));
            }
        }
        return issues;
    }

    /**
     * Description : validate whether the servie is named by "*ServiceImpl" <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the list for issues
     */
    protected List<Issue> validateServicesName() {
        List<Issue> issues = new LinkedList<Issue>();
        String[] names = factory.getBeanNamesForAnnotation(Service.class);
        for (String name : names) {
            Class<?> type = factory.getType(name);
            if (!isIgnore(type) && !type.getName().endsWith("ServiceImpl")) {
                issues.add(new Issue(type.getName(), name, "It's a service. But it's not named end with \"ServiceImpl\".",
                        "Please rename it like " + type.getName() + "ServiceImpl"));
            }
        }
        return issues;
    }

    /**
     * Description : validate whether the service implemented a "*Service" named interface <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the list for issues
     */
    protected List<Issue> validateServicesInterface() {
        List<Issue> issues = new LinkedList<Issue>();
        String[] names = factory.getBeanNamesForAnnotation(Service.class);
        for (String name : names) {
            Class<?> type = factory.getType(name);
            if (!isIgnore(type)) {
                // check interface
                boolean serviceInterfaceFound = false;
                for (Class<?> clazz : type.getInterfaces()) {
                    if (clazz.getName().endsWith("Service")) {
                        serviceInterfaceFound = true;
                        break;
                    }
                }
                if (!serviceInterfaceFound) {
                    issues.add(new Issue(type.getName(), name, "It's a service. But it has no \"*Service\" named Interface.",
                            "Please extract an interface named like \"*Service\" from it."));
                }
            }
        }
        return issues;
    }

    /**
     * Description : validate whether the daos scope is "sigleton" <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the list for issues
     */
    protected List<Issue> validateDaosScope() {
        List<Issue> issues = new LinkedList<Issue>();
        String[] names = factory.getBeanNamesForAnnotation(Repository.class);
        for (String name : names) {
            Class<?> type = factory.getType(name);
            if (!isIgnore(type) && !isScopeSingleton(type)) {
                issues.add(new Issue(type.getName(), name, "It's a Dao. But the Scope is Prototype.", CNS_ADVISE_SINGLETON));
            }
        }
        return issues;
    }

    /**
     * Description : validate whether the daos are named like "*Dao" <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the list for issues
     */
    protected List<Issue> validateDaosName() {
        List<Issue> issues = new LinkedList<Issue>();
        String[] names = factory.getBeanNamesForAnnotation(Repository.class);
        for (String name : names) {
            Class<?> type = factory.getType(name);
            if (!isIgnore(type) && !type.getName().endsWith("Dao")) {
                issues.add(
                        new Issue(type.getName(), name, "It's a DAO. But it's not named end with \"Dao\".", "Please rename it like " + type.getName() + "Dao"));
            }
        }
        return issues;
    }

    /**
     * Description : validate whether the beans are right annotated <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the list for issues
     */
    protected List<Issue> validateComponentsAnnotation() {
        List<Issue> issues = new LinkedList<Issue>();
        String[] names = factory.getBeanNamesForAnnotation(Component.class);
        for (String name : names) {
            Class<?> type = factory.getType(name);
            if (!isIgnore(type)) {
                if (type.getName().endsWith("Controller") && type.getAnnotation(Component.class) != null) {
                    issues.add(new Issue(type.getName(), name, "It's named end with \"Controller\" like a controoler.",
                            "please change the @Component with @Controller"));
                }
                if (type.getName().endsWith("ServiceImpl") && type.getAnnotation(Component.class) != null) {
                    issues.add(new Issue(type.getName(), name, "It's named end with \"ServiceImpl\" like a service.",
                            "please change the @Component with @Service"));
                }
                if (type.getName().endsWith("Dao") && type.getAnnotation(Component.class) != null) {
                    issues.add(new Issue(type.getName(), name, "It's named end with \"Dao\" like a dao.", "please change the @Component with @Repository"));
                }
            }
        }
        return issues;
    }

    /**
     * Description : validate whether the security AOP is on Service Layer <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the list of issues
     */
    protected List<Issue> validateSecurityProxy() {
        List<Issue> issues = new LinkedList<Issue>();
        String[] names = factory.getBeanDefinitionNames();
        for (String name : names) {
            Class<?> type = factory.getType(name);
            if (!isIgnore(type)) {
                if (type.getAnnotation(Controller.class) != null || type.getAnnotation(Repository.class) != null) {
                    if (type.getAnnotation(PreAuthorize.class) != null) {
                        issues.add(new Issue(type.getName(), name, "It's not a Service, and Type should not do security check by @PreAuthorize",
                                "Please remove the @PreAuthorize, and add it to the service if it's nessesary!"));
                    }
                    for (Method method : type.getMethods()) {
                        if (method.getAnnotation(PreAuthorize.class) != null) {
                            issues.add(new Issue(type.getName(), name,
                                    "It's not a service, And Method {" + method.getName() + "} should not annotated by @PreAuthorize",
                                    "Please remove the @PreAuthorize, and add it to the service if it's nessesary!"));
                        }
                    }
                }
            }
        }
        return issues;
    }
}
