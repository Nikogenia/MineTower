package de.nikogenia.mtbase.sql;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "motd")
public class SQLMotd {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED")
    Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 32)
    String name;

    @Column(name = "line1", nullable = false)
    String line1;

    @Column(name = "line2", nullable = false)
    String line2;

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

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

}
