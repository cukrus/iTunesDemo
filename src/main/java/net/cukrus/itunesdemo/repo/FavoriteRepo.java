package net.cukrus.itunesdemo.repo;

import net.cukrus.itunesdemo.model.Favorite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoriteRepo extends CrudRepository<Favorite, Long> {
    List<Favorite> findAllByUserId(Long userId);
    Favorite findByUserIdAndArtistId(Long userId, Long artistId);
}