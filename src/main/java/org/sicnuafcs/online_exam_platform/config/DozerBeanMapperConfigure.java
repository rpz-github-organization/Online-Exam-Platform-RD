package org.sicnuafcs.online_exam_platform.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
    public class DozerBeanMapperConfigure {
        @Bean
        public DozerBeanMapper mapper() {
            DozerBeanMapper mapper = new DozerBeanMapper();
            return mapper;
        }
    }