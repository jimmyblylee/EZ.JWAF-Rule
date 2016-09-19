# EZ.JWAF-Rule
EZ series product ~ JWAF(Java-based Web Application Framework) ~ Rule validator, which will validate the application code

`validate the code managed by spring`

### Rule:
* Beans' Annotation rules

    * Controller Layer beans must only annotated by Controller.
    * Service Layer beans must only annotated by Service.
    * Dao Layer beans must only annotated by Repository.

* Beans' Scope rules

    * Controllers' Scope must be prototype with annotation Scope with param ConfigurableBeanFactory.SCOPE_PROTOTYPE.
    * Services' Scope must be prototype with annotation Scope with param ConfigurableBeanFactory.SCOPE_PROTOTYPE
    * Daos' Scope must be singleton with annotation Scope with param ConfigurableBeanFactory.SCOPE_SINGLETON

* Beans' Name rules

    * Controllers' name must only end with 'Controller' like 'FooController'.
    * Services' name must only end with 'ServiceImpl' like 'FooServiceImpl'.
    * Daos' name must only end with 'Dao' like 'FooDao'.

* Security proxy rules

    * Method security validation with http session, must only be cut at service layer with annotation like @PreAuthorize(\"hasAnyAuthority(['ADD_USER','LOGIN','DEL_GROUP'])\"). 
* Controller proxy rules

    * Controller must not be proxied by any way. If you set some AOP login at controller layer, please remove it, or cut it into service layer. 
* Service Interface rules

    * Services must implement an interface named end with 'Service' like 'FooServiceImpl' implemnting 'FooService'. 
