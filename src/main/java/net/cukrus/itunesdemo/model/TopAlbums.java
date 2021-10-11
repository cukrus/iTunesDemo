package net.cukrus.itunesdemo.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "artist_id" }))
public class TopAlbums {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "artistId")
    private Artist artist;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "artist_albums",
            joinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"))
    private List<Album> albums;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    public TopAlbums() {
        super();
    }

    public TopAlbums(Artist artist, List<Album> albums) {
        this.artist = artist;
        this.albums = albums;
        this.updated = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
