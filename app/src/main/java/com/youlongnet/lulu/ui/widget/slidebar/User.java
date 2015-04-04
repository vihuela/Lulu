package com.youlongnet.lulu.ui.widget.slidebar;

public class User implements Comparable<User> {

    private String name;
    private String header;//这个是每个名称转成英文时对应的第一个字母

    public User(String name, String header) {
        super();
        this.name = name;
        this.header = header;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof User)) {
            return false;
        }
        return getName().equals(((User) o).getName());
    }

    @Override
    public int hashCode() {
        return 17 * getName().hashCode();
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", header=" + header + "]";
    }

    @Override
    public int compareTo(User o) {
        if (!getHeader().equals(o.getHeader())) {
            return getHeader().compareTo(o.getHeader());
        }
        return 0;
    }

}
