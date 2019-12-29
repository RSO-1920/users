package si.fri.rso.lib;


public class ChannelDTO {
    private String adminId;
    private String channelName;
    private Integer channelType;

    public String getChannelName() {
        return channelName;
    }
    public String getAdminId() {
        return adminId;
    }
    public Integer getChannelType() {
        return channelType;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }
}