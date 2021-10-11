package net.cukrus.itunesdemo.repo;

import net.cukrus.itunesdemo.model.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepo extends CrudRepository<Album, Long> {
}
