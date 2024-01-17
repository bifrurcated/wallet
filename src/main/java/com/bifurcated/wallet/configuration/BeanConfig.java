package com.bifurcated.wallet.configuration;

import com.bifurcated.wallet.operation.OperationType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class BeanConfig {
    @Bean
    public Map<String, OperationType> operationTypeMap(Collection<OperationType> operationType) {
        return operationType.stream()
                .collect(Collectors.toMap(ot -> ot.name().toUpperCase(), Function.identity()));
    }
}
