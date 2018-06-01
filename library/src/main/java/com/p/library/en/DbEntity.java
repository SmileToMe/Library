package com.p.library.en;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 数据库存储父类
 *
 * @author JH
 * @since 3.0.0
 */
public class DbEntity extends DataSupport implements Serializable {

    @SerializedName("db_id") //防止json 转换时被 id字段赋值
    private long id;

    /**
     * @return 在数据库中的ID
     */
    public long getDbId() {
        if (id == 0)
            return getBaseObjId();
        return id;
    }


    public void setDbId(long db_id) {
        this.id = db_id;
    }
}
