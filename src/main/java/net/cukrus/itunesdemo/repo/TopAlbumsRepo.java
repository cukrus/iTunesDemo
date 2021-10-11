package net.cukrus.itunesdemo.repo;

import net.cukrus.itunesdemo.model.TopAlbums;
import org.springframework.data.repository.CrudRepository;

public interface TopAlbumsRepo extends CrudRepository<TopAlbums, Long> {
    TopAlbums findByArtistArtistId(Long artistId);
    TopAlbums findByArtistAmgArtistId(Long amgArtistId);
}
