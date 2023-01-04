package com.rpc.netty;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author 王贤
 */
public class ServiceInvokeRequest implements Serializable {
    private static final long serialVersionUID = -349675930021881135L;

    private String serviceName;
    private String methodName;
    private Class<?>[] requestParamType;
    private Object[] args;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getRequestParamType() {
        return requestParamType;
    }

    public void setRequestParamType(Class<?>[] requestParamType) {
        this.requestParamType = requestParamType;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "ServiceInvokeRequest{" +
                "serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", requestParamType=" + Arrays.toString(requestParamType) +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
