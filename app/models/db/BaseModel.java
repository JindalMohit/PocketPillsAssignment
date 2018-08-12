package models.db;

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
    @Column(name = "id", unique = true)
    Long id;

    @WhenCreated
    Timestamp whenCreated;

    @SoftDelete
    @Index
    boolean enabled;

    @WhenModified
    Timestamp whenModified;

    @WhoCreated
    Long whoCreated;

    @WhoModified
    Long whoModified;

    @Version
    Long version;

    public Long getId() {
        return id;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
