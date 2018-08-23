package com.wan.android.data.network.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author wzc
 * @date 2018/8/22
 */
@Entity
public class SearchHistoryData {
    @Id
    private Long id;
    private String key;
    @Generated(hash = 146626858)
    public SearchHistoryData(Long id, String key) {
        this.id = id;
        this.key = key;
    }
    @Generated(hash = 1885562190)
    public SearchHistoryData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    
   
}
