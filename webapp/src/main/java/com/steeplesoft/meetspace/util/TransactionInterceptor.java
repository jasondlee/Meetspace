package com.steeplesoft.meetspace.util;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.UserTransaction;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 15, 2010
 * Time: 10:16:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
@Interceptor
public class TransactionInterceptor implements Serializable {
    @Inject
    private UserTransaction txn;

    @AroundInvoke
    public Object transaction(InvocationContext invocation) throws Exception {
        try {
            txn.begin();
            Object result = invocation.proceed();
            txn.commit();
            return result;
        } catch (Exception e) {
            try {
                txn.rollback();
                throw e;
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }

        return null;// shouldn't ever get here? 
    }
}
