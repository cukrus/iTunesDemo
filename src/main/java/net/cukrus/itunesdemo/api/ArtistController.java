package net.cukrus.itunesdemo.api;

import net.cukrus.itunesdemo.model.ApiResult;
import net.cukrus.itunesdemo.model.ApiResultStatus;
import net.cukrus.itunesdemo.model.Artist;
import net.cukrus.itunesdemo.model.Favorite;
import net.cukrus.itunesdemo.repo.ArtistRepo;
import net.cukrus.itunesdemo.repo.FavoriteRepo;
import net.cukrus.itunesdemo.service.ITunesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/artist")
public class ArtistController {
    private static final Logger LOG = LoggerFactory.getLogger(ArtistController.class);

    @Autowired
    private ITunesService iTunesService;
    @Autowired
    private FavoriteRepo favoriteRepo;
    @Autowired
    private ArtistRepo artistRepo;

    @PostMapping("/search")
    public ApiResult search(@RequestParam String term) {
        ApiResult<Artist> result = new ApiResult();
        result.setStarted(new Date());

        try {
            result.setResult(iTunesService.searchArtist(term));
            result.setStatus(ApiResultStatus.SUCCESS);
        } catch (Exception e) {
            result.setError(e.getMessage());
            result.setStatus(ApiResultStatus.ERROR);
        }

        result.setEnded(new Date());
        result.setTook(result.getEnded().getTime() - result.getStarted().getTime());
        return result;
    }

    @PostMapping("/addFavorite")
    public ApiResult addFavorite(@RequestParam Long userId, @RequestBody Artist artist) {
        ApiResult result = new ApiResult();
        result.setStarted(new Date());

        Favorite found = favoriteRepo.findByUserIdAndArtistId(userId, artist.getArtistId());
        if (found == null) {
            try {
                artistRepo.save(artist);
                favoriteRepo.save(new Favorite(userId, artist.getArtistId()));
                result.setStatus(ApiResultStatus.SUCCESS);
            } catch (Exception e) {
                result.setStatus(ApiResultStatus.ERROR);
                result.setError(e.getMessage());
            }
        } else {
            result.setStatus(ApiResultStatus.SUCCESS);
            result.setError("Artist already in favorites");
        }

        result.setEnded(new Date());
        result.setTook(result.getEnded().getTime() - result.getStarted().getTime());
        return result;
    }

    @PostMapping("/getFavorites")
    public ApiResult getFavorites(@RequestParam Long userId) {
        ApiResult<Artist> result = new ApiResult();
        result.setStarted(new Date());

        List<Favorite> favorites = favoriteRepo.findAllByUserId(userId);
        if (CollectionUtils.isEmpty(favorites)) {
            result.setStatus(ApiResultStatus.SUCCESS);
            result.setError("No favorites found for user(id=" + userId + ")");
        } else {
            List<Long> ids = favorites.stream().map(f -> f.getArtistId()).collect(Collectors.toList());
            List<Artist> found = artistRepo.findAllByArtistIdIn(ids);
            if (CollectionUtils.isEmpty(found)) {
                result.setStatus(ApiResultStatus.ERROR);
                result.setError("No Artists by ids=" + ids);
            } else {
                result.setStatus(ApiResultStatus.SUCCESS);
                result.setResult(found);
            }
        }

        result.setEnded(new Date());
        result.setTook(result.getEnded().getTime() - result.getStarted().getTime());
        return result;
    }
}