package org.example.api.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 视频资源实体
 * */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Video extends club.yunzhi.minicrm.entity.BaseEntity<Long> {
    private String title;
    private String url;
    private Integer duration;  // 秒
}
