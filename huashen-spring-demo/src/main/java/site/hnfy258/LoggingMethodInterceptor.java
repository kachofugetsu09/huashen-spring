package site.hnfy258;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class LoggingMethodInterceptor implements MethodInterceptor {
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("[AOP] 方法执行前: " + invocation.getMethod().getName());
        
        long startTime = System.currentTimeMillis();
        Object result = invocation.proceed(); // 调用原始方法
        long endTime = System.currentTimeMillis();
        
        System.out.println("[AOP] 方法执行后: " + invocation.getMethod().getName() + 
                ", 耗时: " + (endTime - startTime) + "ms");
        
        return result;
    }
}
