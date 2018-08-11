package models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ebean.Model;
import io.ebean.annotation.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.sql.Timestamp;

@MappedSuperclass
public class BaseModel extends Model {

    @Id
    @JsonIgnore
    @Column(name = "id", unique = true)
    Long id;

    @WhenCreated
    @JsonIgnore
    Timestamp whenCreated;

    @SoftDelete
    @JsonIgnore
    @JsonProperty("enabled")
    @Index
    boolean enabled;

    @WhenModified
    @JsonIgnore
    Timestamp whenModified;

    @WhoCreated
    @JsonIgnore
    Long whoCreated;

    @WhoModified
    @JsonIgnore
    Long whoModified;

    @Version
    @JsonIgnore
    Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Timestamp whenCreated) {
        this.whenCreated = whenCreated;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Timestamp getWhenModified() {
        return whenModified;
    }

    public void setWhenModified(Timestamp whenModified) {
        this.whenModified = whenModified;
    }

    public Long getWhoCreated() {
        return whoCreated;
    }

    public void setWhoCreated(Long whoCreated) {
        this.whoCreated = whoCreated;
    }

    public Long getWhoModified() {
        return whoModified;
    }

    public void setWhoModified(Long whoModified) {
        this.whoModified = whoModified;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
