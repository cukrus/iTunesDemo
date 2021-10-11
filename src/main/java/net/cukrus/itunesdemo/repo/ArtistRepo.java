package net.cukrus.itunesdemo.repo;

import net.cukrus.itunesdemo.model.Artist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArtistRepo extends CrudRepository<Artist, Long> {
    List<Artist> findAllByArtistIdIn(List<Long> id);
    Artist findByAmgArtistId(Long amgArtistId);
}