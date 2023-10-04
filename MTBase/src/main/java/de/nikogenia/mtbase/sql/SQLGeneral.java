package de.nikogenia.mtbase.sql;

import jakarta.persistence.*;

@Entity
@Table(name = "general")
public class SQLGeneral {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "INT UNSIGNED")
    Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 32)
    String name;

    @Column(name = "value", nullable = false)
    String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
