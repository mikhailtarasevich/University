package com.mikhail.tarasevich.university.util;

import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;

public class JndiProperty {

    private static final JndiTemplate jndiTemplate = new JndiTemplate();

    public static String getProperty(String jndiProperty, String defaultProperty) {
        try {
            return (String) jndiTemplate.lookup(jndiProperty);
        } catch (NamingException e) {
            return defaultProperty;
        }
    }

}
