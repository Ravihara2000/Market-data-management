package com.Market.Data.Management.System.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@CrossOrigin
@RequestMapping
public @interface SecureCoreController {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};
}
