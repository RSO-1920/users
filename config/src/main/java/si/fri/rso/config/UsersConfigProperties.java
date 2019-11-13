package si.fri.rso.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-config")
public class UsersConfigProperties {

    @ConfigValue(value = "channel-api-add-channel-path", watch = true)
    private String channelApiAddChannelPath;

    public String getChannelApiAddChannelPath() {
        return this.channelApiAddChannelPath;
    }
    public void setChannelApiAddChannelPath (String channelApiAddChannelPath) {
        this.channelApiAddChannelPath = channelApiAddChannelPath;
    }
}
