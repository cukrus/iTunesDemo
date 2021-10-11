package net.cukrus.itunesdemo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.MappedSuperclass;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "wrapperType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Album.class, name = "collection"),
        @JsonSubTypes.Type(value = Artist.class, name = "artist")
})
@MappedSuperclass
public abstract class Wrapper {
    @JsonAlias({ "artistType", "collectionType" })
    private String objectType;

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}