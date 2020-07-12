package com.bigduu.infrastructuremongodb.baseentity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.util.Date;

@Getter
@Setter
public class BaseMongoEntity {

    @Id
    protected String id;

    @Version
    protected Long dataVersion;

    @CreatedDate
    protected Date dataCreatedDate;

    @LastModifiedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    protected Date dataLastModifiedDate;

    /**
     * 数据状态
     */
    protected Boolean dataStatus = true;
}
