package org.xry.churchmodule.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Admin {
    private Integer id;
    private String adminName;
//    @JsonIgnore     双向阻止
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  //仅阻止反序列化
    private String password;
    private String email;
    private String phone;
    private String address;

    public Admin(){}

    public Admin(Integer id, String adminName, String password, String email, String phone, String address) {
        this.id = id;
        this.adminName = adminName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", adminName='" + adminName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
