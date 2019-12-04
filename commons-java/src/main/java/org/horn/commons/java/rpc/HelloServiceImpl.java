package org.horn.commons.java.rpc;

public class HelloServiceImpl implements HelloService {
	public String hello(String name) {  
        return "Hello " + name;  
    }  
}
