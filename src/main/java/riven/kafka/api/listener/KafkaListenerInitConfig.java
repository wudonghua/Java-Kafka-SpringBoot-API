package riven.kafka.api.listener;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.annotation.KafkaListener;
import riven.kafka.api.configuration.ConsumerConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author dw07-Riven770[wudonghua@gznb.com]
 * @Date 2017/12/1415:18
 * 为实现了IKafkaListener接口方法listener追加注解
 */
public class KafkaListenerInitConfig implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private static final String TOPICS = "topics";
    private static final String LISTENER = "listener";
    @Autowired
    private ConsumerConfiguration consumerConfiguration;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        try {
            if (bean instanceof IKafkaListener)
                return injectBean(setAnnotation(bean));
            //设置参数注解:
            return bean;
        } catch (NotFoundException | CannotCompileException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 添加Autowired注入的内容
     *
     * @param iKafkaListener
     * @return
     * @throws IllegalAccessException
     */
    private IKafkaListener injectBean(IKafkaListener iKafkaListener) throws IllegalAccessException {
        Field[] fields = iKafkaListener.getClass().getFields();
        Field[] declaredFields = iKafkaListener.getClass().getDeclaredFields();
        Field[] allFiles = ArrayUtils.addAll(fields, declaredFields);
        //判断是否有@Autowired注解
        for (Field field : allFiles) {
            if (field.getAnnotation(Autowired.class) == null)
                continue;
            field.setAccessible(true);
            field.set(iKafkaListener, this.applicationContext.getBean(field.getName()));
        }
        return iKafkaListener;
    }

    /**
     * 添加注解及其属性
     *
     * @param bean
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private IKafkaListener setAnnotation(Object bean) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
        ClassPool classPool = ClassPool.getDefault();
        //获取当前Bean的常量池
        CtClass ctClass = classPool.getCtClass(bean.getClass().getName());
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        //获取对应的注解内容
        List<StringMemberValue> list = new ArrayList<>(consumerConfiguration.getTopics().size());
        //创建注解属性
        consumerConfiguration.getTopics().forEach(topic -> list.add(new StringMemberValue(topic, constPool)));
        MemberValue[] memberValues = new MemberValue[list.size()];
        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
        arrayMemberValue.setValue(list.toArray(memberValues));
        //创建注解
        Annotation topics = new Annotation(KafkaListener.class.getName(), constPool);
        //为注解属性赋值
        topics.addMemberValue(TOPICS, arrayMemberValue);
        //创建注解容器
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        annotationsAttribute.setAnnotation(topics);
        //把注解放到目标方法
        ctClass.getDeclaredMethod(LISTENER).getMethodInfo().addAttribute(annotationsAttribute);

        //生成一个全新的对象
        Class aClass = ctClass.toClass(new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return super.loadClass(name);
            }
        }, null);
        return (IKafkaListener) aClass.newInstance();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 获取SpringIOC
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
