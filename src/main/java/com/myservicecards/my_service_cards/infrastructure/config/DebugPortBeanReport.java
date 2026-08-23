package com.myservicecards.my_service_cards.infrastructure.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

// #region agent log
@Component
public class DebugPortBeanReport implements BeanFactoryPostProcessor {

    private static final Path LOG = Path.of("/Users/amineaqeblip/Documents/GitHub/my-service/.cursor/debug-e127a9.log");
    private static final List<String> PORTS = List.of(
            "com.myservicecards.my_service_cards.ports.out.CardProviderRepositoryPort",
            "com.myservicecards.my_service_cards.ports.out.BankRepositoryPort",
            "com.myservicecards.my_service_cards.ports.out.BankCardRepositoryPort",
            "com.myservicecards.my_service_cards.ports.out.CardCryptoPort"
    );

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            StringBuilder names = new StringBuilder();
            if (beanFactory instanceof BeanDefinitionRegistry registry) {
                for (String name : registry.getBeanDefinitionNames()) {
                    String className = beanFactory.getBeanDefinition(name).getBeanClassName();
                    if (className != null && (className.contains("Persistence") || className.contains("Grpc") || className.contains("Repository"))) {
                        names.append(name).append("=").append(className).append(";");
                    }
                }
            }
            for (String port : PORTS) {
                boolean hasDef = false;
                String matching = "";
                try {
                    Class<?> portClass = Class.forName(port);
                    String[] beanNames = beanFactory.getBeanNamesForType(portClass, true, false);
                    hasDef = beanNames.length > 0;
                    matching = Arrays.toString(beanNames);
                } catch (ClassNotFoundException e) {
                    matching = "CLASS_NOT_FOUND";
                }
                String line = String.format(
                        "{\"sessionId\":\"e127a9\",\"runId\":\"pre-fix\",\"hypothesisId\":\"H1-H5\",\"location\":\"DebugPortBeanReport\",\"message\":\"port bean scan\",\"data\":{\"port\":\"%s\",\"hasBean\":%s,\"beanNames\":\"%s\",\"adapterDefs\":\"%s\"},\"timestamp\":%d}%n",
                        port, hasDef, matching.replace("\"", "'"), names.toString().replace("\"", "'"), System.currentTimeMillis()
                );
                Files.writeString(LOG, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        } catch (Exception ignored) {
            // debug only
        }
    }
}
// #endregion
