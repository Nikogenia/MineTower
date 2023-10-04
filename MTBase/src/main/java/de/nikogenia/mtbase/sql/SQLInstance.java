package de.nikogenia.mtbase.sql;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "instance")
public class SQLInstance {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED")
    Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 32)
    String name;

    @Column(name = "host", nullable = false, unique = true, length = 15)
    String host;

    @Column(name = "address", nullable = false, unique = true, length = 21)
    String address;

    @ManyToOne
    @JoinColumn(name = "agent", nullable = false)
    SQLAgent agent;

    @ManyToOne
    @JoinColumn(name = "cluster")
    SQLCluster cluster;

    @Column(name = "type", nullable = false, length = 32)
    String type;

    @Column(name = "enabled", nullable = false)
    boolean enabled;

    @Column(name = "created", nullable = false)
    Timestamp created;

    @Column(name = "mode", nullable = false, length = 32)
    String mode;

    @Column(name = "memory", nullable = false, length = 16)
    String memory;

    @Column(name = "version", nullable = false, length = 16)
    String version;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SQLAgent getAgent() {
        return agent;
    }

    public void setAgent(SQLAgent agent) {
        this.agent = agent;
    }

    public SQLCluster getCluster() {
        return cluster;
    }

    public void setCluster(SQLCluster cluster) {
        this.cluster = cluster;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
