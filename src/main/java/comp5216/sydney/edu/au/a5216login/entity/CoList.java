package comp5216.sydney.edu.au.a5216login.entity;

public class CoList {

    private Long listId;
    private String listName;
    private String time;
    private Integer type;
    private String invitationCode;
    private Long leaderId;


    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    @Override
    public String toString() {
        return "CoList{" +
                "listId=" + listId +
                ", listName='" + listName + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", invitationCode='" + invitationCode + '\'' +
                ", leaderId=" + leaderId +
                '}';
    }
}
