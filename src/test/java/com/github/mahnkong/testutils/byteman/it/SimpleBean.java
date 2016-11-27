package com.github.mahnkong.testutils.byteman.it;

import javax.ejb.Stateless;

/**
 * Created by mahnkong on 03.05.15.
 */
@Stateless
public class SimpleBean {
    public String sayHello() {
        return "Hello!";
    }
}
