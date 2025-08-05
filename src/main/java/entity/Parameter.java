package entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Parameter {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("key")
    private String key;

    @JsonProperty("description")
    private String description;

    @JsonProperty("type")
    private String type; // BOOLEAN, INTEGER, FLOAT, ENUM, STRING

    @JsonProperty("value")
    private String value;

    @JsonProperty("default_value")
    private String defaultValue;

    @JsonProperty("options")
    private String options; // JSON字符串存储枚举选项

    @JsonProperty("validation_rules")
    private String validationRules; // JSON字符串存储验证规则

    @JsonProperty("category")
    private String category;

    @JsonProperty("group")
    private String group;

    @JsonProperty("sort")
    private Integer sort;

    @JsonProperty("is_required")
    private Boolean isRequired;

    @JsonProperty("is_readonly")
    private Boolean isReadonly;

    @JsonProperty("is_visible")
    private Boolean isVisible;

    @JsonProperty("scope")
    private String scope; // GLOBAL, DEPARTMENT, USER

    @JsonProperty("status")
    private String status; // ACTIVE, INACTIVE

    @JsonProperty("created_by")
    private Long createdBy;

    @JsonProperty("updated_by")
    private Long updatedBy;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // 关联字段
    @JsonProperty("creator")
    @JsonBackReference
    private User creator;

    @JsonProperty("updater")
    @JsonBackReference
    private User updater;

    @JsonProperty("parameter_values")
    @JsonBackReference
    private List<ParameterValue> parameterValues;

    @JsonProperty("extra_fields")
    private Map<String, Object> extraFields;

    // 构造函数
    public Parameter() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "ACTIVE";
        this.isRequired = false;
        this.isReadonly = false;
        this.isVisible = true;
        this.sort = 0;
    }

    public Parameter(String name, String key, String description, String type, String value) {
        this();
        this.name = name;
        this.key = key;
        this.description = description;
        this.type = type;
        this.value = value;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) {
        this.value = value;
        this.updatedAt = LocalDateTime.now();
    }

    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }

    public String getValidationRules() { return validationRules; }
    public void setValidationRules(String validationRules) { this.validationRules = validationRules; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }

    public Boolean getIsRequired() { return isRequired; }
    public void setIsRequired(Boolean isRequired) { this.isRequired = isRequired; }

    public Boolean getIsReadonly() { return isReadonly; }
    public void setIsReadonly(Boolean isReadonly) { this.isReadonly = isReadonly; }

    public Boolean getIsVisible() { return isVisible; }
    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @JsonIgnore
    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    @JsonIgnore
    public User getUpdater() { return updater; }
    public void setUpdater(User updater) { this.updater = updater; }

    @JsonIgnore
    public List<ParameterValue> getParameterValues() { return parameterValues; }
    public void setParameterValues(List<ParameterValue> parameterValues) { this.parameterValues = parameterValues; }

    public Map<String, Object> getExtraFields() { return extraFields; }
    public void setExtraFields(Map<String, Object> extraFields) { this.extraFields = extraFields; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(id, parameter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}