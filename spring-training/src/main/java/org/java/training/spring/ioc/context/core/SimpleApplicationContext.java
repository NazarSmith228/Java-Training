package org.java.training.spring.ioc.context.core;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.java.training.spring.ioc.annotation.IBean;
import org.java.training.spring.ioc.context.support.BeanNameResolver;
import org.java.training.spring.ioc.context.support.BeanThrows;
import org.java.training.spring.ioc.exception.BeanCreationException;
import org.java.training.spring.ioc.exception.ExistingBeanException;
import org.java.training.spring.ioc.exception.NoSuchBeanException;
import org.java.training.spring.ioc.exception.NoUniqueBeanException;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.stream.Collectors.toMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SimpleApplicationContext implements IoCApplicationContext {

    private static final Package currentPackage = SimpleApplicationContext.class.getPackage();

    Map<String, Object> beansMap;
    BeanThrows beanThrows;
    BeanNameResolver nameResolver;
    String packageToScan;

    public SimpleApplicationContext() {
        this(currentPackage.getName());
    }

    public SimpleApplicationContext(String packageToScan) {
        this.beansMap = new HashMap<>();
        this.beanThrows = new BeanThrows();
        this.nameResolver = new BeanNameResolver();

        beanThrows.withCondition(packageToScan, ObjectUtils::isEmpty,
                        () -> new IllegalArgumentException("Invalid package: empty or null"))
                .throwIf();

        this.packageToScan = packageToScan;
    }

    public void start() {
        log.info("Application context has started...");
        startScan(packageToScan);
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        List<Object> instances = beansMap.values()
                .stream()
                .filter(beanType::isInstance)
                .toList();

        beanThrows.withCondition(instances, List::isEmpty, () -> new NoSuchBeanException(beanType.getName()))
                .withCondition(instances, l -> l.size() > 1, () -> new NoUniqueBeanException(beanType.getName()))
                .throwIf();

        return beanType.cast(instances.get(0));
    }

    @Override
    public Object getBean(String beanName) {
        Object bean = beansMap.get(beanName);
        beanThrows.withCondition(bean, Objects::isNull, () -> new NoSuchBeanException(beanName)).throwIf();
        return bean;
    }

    @Override
    public <T> T getBean(String beanName, Class<T> beanType) {
        Object bean = beansMap.get(beanName);

        beanThrows.withCondition(bean, Objects::isNull, () -> new NoSuchBeanException(beanName))
                .withCondition(beanType, type -> !type.isInstance(bean),
                        () -> new NoSuchBeanException(join(" of ", beanName, beanType.getName())))
                .throwIf();

        return beanType.cast(bean);
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return beansMap.entrySet()
                .stream()
                .filter(entry -> beanType.isInstance(entry.getValue()))
                .collect(toMap(Map.Entry::getKey, e -> beanType.cast(e.getValue())));
    }

    @Override
    public Set<String> getBeanNames() {
        return beansMap.keySet();
    }

    private void startScan(String packageToScan) {
        log.info("Starting package scan...");
        var scanner = new Reflections(packageToScan);
        Set<Class<?>> beanClasses = scanner.getTypesAnnotatedWith(IBean.class);
        beanClasses.stream()
                .map(this::createBean)
                .forEach(this::registerBean);
    }

    private Object createBean(Class<?> beanClass) {
        try {
            log.info("Creating a bean of {}", beanClass.getName());
            return beanClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException ex) {
            log.error("Error creating bean instance!");
            throw new BeanCreationException(
                    format("Cannot create bean instance from class %s", beanClass.getName()), ex);
        }
    }

    private void registerBean(Object bean) {
        String beanName = nameResolver.resolveBeanNameByType(bean.getClass());
        log.info("Registering bean {} of {}", beanName, bean.getClass().getName());

        beanThrows.withCondition(beanName, beansMap::containsKey, () -> new ExistingBeanException(beanName)).throwIf();
        beansMap.put(beanName, bean);
    }
}
