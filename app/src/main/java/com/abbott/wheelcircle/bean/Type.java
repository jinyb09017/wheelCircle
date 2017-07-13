package com.abbott.wheelcircle.bean;

import com.abbott.libcircle.entity.PickerItem;

/**
 * @author jyb jyb_96@sina.com on 2017/7/13.
 * @version V1.0
 * @Description: add comment
 * @date 16-4-21 11:21
 * @copyright www.tops001.com
 */

public class Type implements PickerItem {
    private String name;
    private String id;

    @Override
    public String getText() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
