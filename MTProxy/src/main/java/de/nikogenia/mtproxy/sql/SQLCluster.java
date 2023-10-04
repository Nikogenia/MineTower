package de.nikogenia.mtproxy.sql;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "cluster")
public class SQLCluster {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED")
    Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 32)
    String name;

    @Column(name = "created", nullable = false)
    Timestamp created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

}
