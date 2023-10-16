package de.nikogenia.mtsmp.sql;

import de.nikogenia.mtbase.sql.SQLPlayer;
import jakarta.persistence.*;

@Entity
@Table(name = "home")
public class SQLHome {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "player", nullable = false)
    SQLPlayer player;

    @Column(name = "world", nullable = false)
    String world;

    @Column(name = "x", nullable = false)
    Double x;

    @Column(name = "y", nullable = false)
    Double y;

    @Column(name = "z", nullable = false)
    Double z;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SQLPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SQLPlayer player) {
        this.player = player;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

}
