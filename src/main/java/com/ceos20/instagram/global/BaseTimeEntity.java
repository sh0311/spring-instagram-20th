package com.ceos20.instagram.global;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // BaseEntity를 상속한 엔티티들이 BaseEntity의 필드들을 칼럼으로 인식하게 된다
@EntityListeners(AuditingEntityListener.class) // 엔티티의 수정/생성등의 이벤트가 발생하였을 때, 이와 같은 변경사항을 Audit하기 위함
public class BaseTimeEntity {
    @CreatedDate
    @Column(name="created_at", updatable = false, columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;
}
