package entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Objects;

public class ParameterValue {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("parameter_id")
    private Long parameterId;

    @JsonProperty("value")
    private String value;

    @JsonProperty("previous_value")
    private String previousValue;

    @JsonProperty("change_reason")
    private String changeReason;

    @JsonProperty("created_by")
    private Long createdBy;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // 关联字段
    @JsonProperty("parameter")
    @JsonBackReference
    private Parameter parameter;

    @JsonProperty("creator")
    @JsonBackReference
    private User creator;

    // 构造函数
    public ParameterValue() {
        this.createdAt = LocalDateTime.now();
    }

    public ParameterValue(Long parameterId, String value, String previousValue, String changeReason, Long createdBy) {
        this();
        this.parameterId = parameterId;
        this.value = value;
        this.previousValue = previousValue;
        this.changeReason = changeReason;
        this.createdBy = createdBy;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getParameterId() { return parameterId; }
    public void setParameterId(Long parameterId) { this.parameterId = parameterId; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getPreviousValue() { return previousValue; }
    public void setPreviousValue(String previousValue) { this.previousValue = previousValue; }

    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @JsonIgnore
    public Parameter getParameter() { return parameter; }
    public void setParameter(Parameter parameter) { this.parameter = parameter; }

    @JsonIgnore
    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterValue that = (ParameterValue) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ParameterValue{" +
                "id=" + id +
                ", parameterId=" + parameterId +
                ", value='" + value + '\'' +
                ", previousValue='" + previousValue + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}