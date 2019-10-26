package si.fri.rso.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-config")
public class UsersConfigProperties {

    @ConfigValue(value = "channel-api-url", watch = true)
    private String channelApiUrl;

    public String getChannelApiUrl() {
        return this.channelApiUrl;
    }

    public void setChannelApiUrl(String channelApiUrl) {
        this.channelApiUrl = channelApiUrl;
    }
}
