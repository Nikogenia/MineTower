package de.nikogenia.mtbase.sql;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "player")
public class SQLPlayer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED")
    Integer id;

    @Column(name = "uuid", nullable = false, unique = true, length = 36)
    String uuid;

    @Column(name = "name", nullable = false, unique = true, length = 16)
    String name;

    @ManyToOne
    @JoinColumn(name = "server", nullable = false)
    SQLInstance server;

    @Column(name = "online", nullable = false)
    boolean online;

    @Column(name = "time_played", columnDefinition = "INT UNSIGNED", nullable = false)
    Integer timePlayed;

    @Column(name = "first_joined")
    Timestamp firstJoined;

    @Column(name = "number_joined", columnDefinition = "INT UNSIGNED", nullable = false)
    Integer numberJoined;

    @Column(name = "last_joined")
    Timestamp lastJoined;

    @Column(name = "last_disconnect")
    Timestamp lastDisconnect;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SQLInstance getServer() {
        return server;
    }

    public void setServer(SQLInstance server) {
        this.server = server;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Integer getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(Integer timePlayed) {
        this.timePlayed = timePlayed;
    }

    public Timestamp getFirstJoined() {
        return firstJoined;
    }

    public void setFirstJoined(Timestamp firstJoined) {
        this.firstJoined = firstJoined;
    }

    public Integer getNumberJoined() {
        return numberJoined;
    }

    public void setNumberJoined(Integer numberJoined) {
        this.numberJoined = numberJoined;
    }

    public Timestamp getLastJoined() {
        return lastJoined;
    }

    public void setLastJoined(Timestamp lastJoined) {
        this.lastJoined = lastJoined;
    }

    public Timestamp getLastDisconnect() {
        return lastDisconnect;
    }

    public void setLastDisconnect(Timestamp lastDisconnect) {
        this.lastDisconnect = lastDisconnect;
    }

}
