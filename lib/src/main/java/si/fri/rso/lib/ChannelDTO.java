package si.fri.rso.lib;


public class ChannelDTO {
    private Integer adminId;
    private String channelName;
    private Integer channelType;

    public String getChannelName() {
        return channelName;
    }
    public Integer getAdminId() {
        return adminId;
    }
    public Integer getChannelType() {
        return channelType;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }
}