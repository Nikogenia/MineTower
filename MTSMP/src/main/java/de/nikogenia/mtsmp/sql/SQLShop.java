package de.nikogenia.mtsmp.sql;

import de.nikogenia.mtbase.sql.SQLPlayer;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "shop")
public class SQLShop {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "INT UNSIGNED")
    Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 32)
    String name;

    @Column(name = "created", nullable = false)
    Timestamp created;

    @ManyToOne
    @JoinColumn(name = "owner")
    SQLPlayer owner;

    @Column(name = "area", nullable = false)
    String area;

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

    public SQLPlayer getOwner() {
        return owner;
    }

    public void setOwner(SQLPlayer owner) {
        this.owner = owner;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}
