package com.bence.mate.annotations;

import org.glassfish.jersey.linking.ProvideLink;
import org.glassfish.jersey.linking.Binding;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@ProvideLink(value = ProvideLink.InheritFromAnnotation.class, rel = "next",
        bindings = {
                @Binding(name = "page", value = "${instance.number + 1}"),
                @Binding(name = "size", value = "${instance.size}"),
        }, condition = "${instance.nextPageAvailable}")
@ProvideLink(value = ProvideLink.InheritFromAnnotation.class, rel = "prev",
        bindings = {
                @Binding(name = "page", value = "${instance.number - 1}"),
                @Binding(name = "size", value = "${instance.size}"),
        }, condition = "${instance.previousPageAvailable}")
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageLinks {
    Class<?> value();
}