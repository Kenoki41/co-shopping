package comp5216.sydney.edu.au.a5216login.entity;

public class CoListREQ {

    private Long userId;

    private String name;

    private Boolean isLeader;

    private String invitationCode;

    private Integer type;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLeader() {
        return isLeader;
    }

    public void setLeader(Boolean leader) {
        isLeader = leader;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CoListREQ{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", isLeader=" + isLeader +
                ", invitationCode='" + invitationCode + '\'' +
                ", type=" + type +
                '}';
    }
}
