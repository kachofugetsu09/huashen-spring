package site.hnfy258;

import site.hnfy258.aop.AdvisedSupport;
import site.hnfy258.aop.TargetSource;
import site.hnfy258.aop.aspectj.AspectJExpressionPointcut;
import site.hnfy258.aop.framework.Cglib2AopProxy;
import site.hnfy258.aop.framework.JdkDynamicAopProxy;

public class MyApplication  {
    public static void main(String[] args) throws Exception {
        HuashenApplicationContext context = new HuashenApplicationContext(MyConfig.class);
        // 添加测试事件的部分
        System.out.println("\n===== 测试事件机制 =====");
        // 直接使用context发布事件，而不是通过listener
        context.publishEvent(new CustomEvent(context, "这是一个测试事件"));
        context.close();
        HuashenApplicationContext context2 = new HuashenApplicationContext("classpath:spring.xml");
        TestBean testBean = (TestBean) context2.getBean("testBean");
        System.out.println("testBean:" + testBean);
        HuashenService huashenService = (HuashenService) context.getBean("huashenService");
        HuashenService huashenService2 = (HuashenService) context.getBean("huashenService");
        huashenService.useUserService();
        System.out.println("huashenService:" + huashenService);
        System.out.println("huashenService2:" + huashenService2);
        UserService userService = (UserService) context.getBean("userService");
        userService.queryUserInfo("10001");
        huashenService.test();


        System.out.println("Testing ApplicationContextAware functionality:");
        AwareTestBean awareTestBean = (AwareTestBean) context.getBean("awareTestBean");
        awareTestBean.testAwareFunction();


        // 测试AOP功能
        System.out.println("\n===== 测试AOP功能 =====");
        TestService testService = (TestService) context.getBean("testService");

        // 创建代理
        // 1. 设置切入点表达式
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* site.hnfy258.ITestService.*(..))");

        // 2. 设置支持类
        AdvisedSupport advisedSupport = new AdvisedSupport();
        TargetSource targetSource = new TargetSource(testService);
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.setMethodInterceptor(new LoggingMethodInterceptor());
        advisedSupport.setMethodMatcher(pointcut.getMethodMatcher());

        // 3. 创建JDK动态代理
        System.out.println("=== JDK动态代理 ===");
        JdkDynamicAopProxy jdkProxy = new JdkDynamicAopProxy(advisedSupport);
        // 注意这里改成了接口类型
        ITestService jdkProxyService = (ITestService) jdkProxy.getProxy();
        jdkProxyService.doSomething();
        String jdkResult = jdkProxyService.getInfo("test-jdk");
        System.out.println("JDK代理结果: " + jdkResult);

        // 4. 创建CGLIB代理
        System.out.println("\n=== CGLIB代理 ===");
        Cglib2AopProxy cglibProxy = new Cglib2AopProxy(advisedSupport);
        TestService cglibProxyService = (TestService) cglibProxy.getProxy();
        cglibProxyService.doSomething();
        String cglibResult = cglibProxyService.getInfo("test-cglib");
        System.out.println("CGLIB代理结果: " + cglibResult);

        // 测试方法不匹配的情况
        System.out.println("\n=== 测试方法不匹配 ===");
        // 创建一个只匹配getInfo方法的切入点
        AspectJExpressionPointcut specificPointcut = new AspectJExpressionPointcut(
                "execution(* site.hnfy258.ITestService.getInfo(..))");

        AdvisedSupport specificSupport = new AdvisedSupport();
        specificSupport.setTargetSource(targetSource);
        specificSupport.setMethodInterceptor(new LoggingMethodInterceptor());
        specificSupport.setMethodMatcher(specificPointcut.getMethodMatcher());

        JdkDynamicAopProxy specificProxy = new JdkDynamicAopProxy(specificSupport);
        // 注意这里也改成了接口类型
        ITestService specificProxyService = (ITestService) specificProxy.getProxy();

        System.out.println("调用doSomething (不应匹配):");
        specificProxyService.doSomething();  // 不应该被拦截

        System.out.println("调用getInfo (应匹配):");
        specificProxyService.getInfo("test-specific");  // 应该被拦截

    }
}
