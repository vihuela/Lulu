package com.youlongnet.lulu.db.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "new_friends_msgs")
public class DB_InviteMessge {
    @Id
    private long id;
    @Column(column = "username")
    private String username;
    @Column(column = "groupid")
    private String groupid;
    @Column(column = "groupname")
    private String groupname;
    @Column(column = "time")
    private String time;
    @Column(column = "reason")
    private String reason;
    @Column(column = "status")
    private String status;
    @Column(column = "isInviteFromMe")
    private String isInviteFromMe;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsInviteFromMe() {
        return isInviteFromMe;
    }

    public void setIsInviteFromMe(String isInviteFromMe) {
        this.isInviteFromMe = isInviteFromMe;
    }
}
