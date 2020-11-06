package comp5216.sydney.edu.au.a5216login.entity;

import java.io.Serializable;

public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long mainUserId;

    private Long friendUserId;

    private Integer type;

    private Integer beenDeleted;

    public UserGroup(Long mainUserId, Long friendUserId, Integer type) {
        this.mainUserId = mainUserId;
        this.friendUserId = friendUserId;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMainUserId() {
        return mainUserId;
    }

    public void setMainUserId(Long mainUserId) {
        this.mainUserId = mainUserId;
    }

    public Long getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(Long friendUserId) {
        this.friendUserId = friendUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBeenDeleted() {
        return beenDeleted;
    }

    public void setBeenDeleted(Integer beenDeleted) {
        this.beenDeleted = beenDeleted;
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "id=" + id +
                ", mainUserId=" + mainUserId +
                ", friendUserId=" + friendUserId +
                ", type=" + type +
                ", beenDeleted=" + beenDeleted +
                '}';
    }
}
