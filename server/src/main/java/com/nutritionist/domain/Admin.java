package com.nutritionist.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin {

  @Id
  @Column(length = 10)
  private String account;

  @Column(name = "password", nullable = false, length = 255)
  private String password;

  @Column(nullable = false, length = 5)
  private String name;

  @Column(length = 13)
  private String phone;

  @Column(name = "avatar")
  private byte[] avatar;

  public Admin() {}

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public byte[] getAvatar() {
    return avatar;
  }

  public void setAvatar(byte[] avatar) {
    this.avatar = avatar;
  }
}