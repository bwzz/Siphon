package com.yuantiku.siphon.data;

import proguard.annotation.KeepClassMemberNames;

/**
 * Created by wanghb on 16/11/19.
 */
@KeepClassMemberNames
public class FenbiAccount {
    private String account;
    private String password;

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
