package com.ComNCheck.ComNCheck.domain.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RoleHierarchyConfig {


    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_MAJOR_PRESIDENT \n" +
                "ROLE_MAJOR_PRESIDENT > ROLE_STUDENT_COUNCIL \n" +
                "ROLE_STUDENT_COUNCIL > ROLE_STUDENT \n" +
                "ROLE_STUDENT > ROLE_GRADUATE_STUDENT";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }


}
