package net.cukrus.itunesdemo.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Artist extends Wrapper {
    @Id
    private Long artistId;
    private Long amgArtistId;
    private String artistName;
    @JsonAlias({ "artistViewUrl" })
    private String artistLinkUrl;
    private String primaryGenreName;
    private String primaryGenreId;

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public Long getAmgArtistId() {
        return amgArtistId;
    }

    public void setAmgArtistId(Long amgArtistId) {
        this.amgArtistId = amgArtistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistLinkUrl() {
        return artistLinkUrl;
    }

    public void setArtistLinkUrl(String artistLinkUrl) {
        this.artistLinkUrl = artistLinkUrl;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    public void setPrimaryGenreName(String primaryGenreName) {
        this.primaryGenreName = primaryGenreName;
    }

    public String getPrimaryGenreId() {
        return primaryGenreId;
    }

    public void setPrimaryGenreId(String primaryGenreId) {
        this.primaryGenreId = primaryGenreId;
    }
}