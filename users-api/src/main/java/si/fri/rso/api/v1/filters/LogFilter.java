package si.fri.rso.api.v1.filters;

import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.cdi.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Log
@ApplicationScoped
@Provider
public class LogFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOG = LogManager.getLogger(LogFilter.class.getName());


    @Override
    public void filter(ContainerRequestContext reqContext) throws IOException {
        System.out.println("-- req info --");
        // log(reqContext.getUriInfo(), reqContext.getHeaders());

        ConfigurationUtil configurationUtil = ConfigurationUtil.getInstance();

        HashMap settings = new HashMap();

        settings.put("environmentType", EeConfig.getInstance().getEnv().getName());
        settings.put("applicationName", EeConfig.getInstance().getName());
        settings.put("applicationVersion", EeConfig.getInstance().getVersion());
        settings.put("uniqueInstanceId", EeRuntime.getInstance().getInstanceId());

        boolean isUniqueRequestIdSet = false;
        for(String key : reqContext.getHeaders().keySet()) {
            if (key.equals("uniqueRequestId")) {
                isUniqueRequestIdSet = true;
                settings.put("uniqueRequestId", reqContext.getHeaders().getFirst(key));
                break;
            }
            System.out.printf("  %s: %s\n", key, reqContext.getHeaders().getFirst(key));
        }

        if (!isUniqueRequestIdSet)
            settings.put("uniqueRequestId", UUID.randomUUID().toString());

        reqContext.setProperty("uniqueRequestId", settings.get("uniqueRequestId"));

        LOG.info(settings);

        System.out.println("requestID: " + settings.toString() + "  " + reqContext.getProperty("uniqueRequestId"));
    }

    @Override
    public void filter(ContainerRequestContext reqContext,
                       ContainerResponseContext resContext) throws IOException {
        // System.out.println("-- res info --");
        // log(reqContext.getUriInfo(), resContext.getHeaders());


    }

    private void log(UriInfo uriInfo, MultivaluedMap<String, ?> headers) {
        System.out.println("Path: " + uriInfo.getPath());
        headers.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}