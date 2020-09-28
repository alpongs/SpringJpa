package study.springjpa.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

import javax.persistence.Column;

public class BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false)
    private LocalDateTime createDate;
    @LastModifiedBy
    private LocalDateTime lastModifiedDate;
}
