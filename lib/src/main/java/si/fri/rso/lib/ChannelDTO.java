package si.fri.rso.lib;


public class ChannelDTO {

    private String channelName;
    private Integer channelAdminId;

    public String getChannelName() {
        return this.channelName;
    }
    public Integer getChannelAdminId() {
        return this.channelAdminId;
    }

    public void setChannelAdminId(Integer channelAdminId) {
        this.channelAdminId = channelAdminId;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}