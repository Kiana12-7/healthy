package org.example.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class BaseEntity<ID extends Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected ID id;

    private Timestamp createdTime = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    @CreatedBy
    @JsonView(CreateByJsonView.class)
    protected User createdBy;

    @ManyToOne
    @LastModifiedBy
    @JsonView(UpdatedByJsonView.class)
    protected User updatedBy;

    @UpdateTimestamp
    protected Timestamp updatedTime = new Timestamp(System.currentTimeMillis());

    public BaseEntity() {
    }

    public BaseEntity(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public interface CreateByJsonView {
    }

    public interface UpdatedByJsonView {
    }
}