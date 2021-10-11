package net.cukrus.itunesdemo.api;

import net.cukrus.itunesdemo.model.Album;
import net.cukrus.itunesdemo.model.ApiResult;
import net.cukrus.itunesdemo.model.ApiResultStatus;
import net.cukrus.itunesdemo.model.TopAlbums;
import net.cukrus.itunesdemo.repo.ArtistRepo;
import net.cukrus.itunesdemo.repo.TopAlbumsRepo;
import net.cukrus.itunesdemo.service.ITunesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/album")
public class AlbumController {
    private static final Logger LOG = LoggerFactory.getLogger(AlbumController.class);
    private static final long TWELVE_HOURS = TimeUnit.HOURS.toMillis(12);

    @Autowired
    private ITunesService iTunesService;
    @Autowired
    private TopAlbumsRepo topAlbumsRepo;
    @Autowired
    private ArtistRepo artistRepo;

    @PostMapping("/top5amg")
    public ApiResult top5ByAmg(@RequestParam Long artistAmg) {
        ApiResult<Album> result = new ApiResult();
        result.setStarted(new Date());

        TopAlbums topAlbums = topAlbumsRepo.findByArtistAmgArtistId(artistAmg);
        //we check for new albums every 12 hours from iTunes
        if(topAlbums == null || topAlbums.getUpdated().before(new Date(System.currentTimeMillis() - TWELVE_HOURS))) {
            try {
                List<Album> found = iTunesService.lookupByAmg(artistAmg);
                if (CollectionUtils.isEmpty(found)) {
                    result.setStatus(ApiResultStatus.SUCCESS);
                    result.setError("No albums found for artist(amg=" + artistAmg + ")");
                } else {
                    if (topAlbums == null) {
                        topAlbums = new TopAlbums(artistRepo.findByAmgArtistId(artistAmg), found);
                    } else {
                        topAlbums.setUpdated(new Date());
                        topAlbums.setAlbums(found);
                    }
                    topAlbumsRepo.save(topAlbums);
                    result.setResult(found);
                    result.setStatus(ApiResultStatus.SUCCESS);
                }
            } catch (Exception e) {
                result.setError(e.getMessage());
                result.setStatus(ApiResultStatus.ERROR);
            }
        } else {
            result.setResult(topAlbums.getAlbums());
            result.setStatus(ApiResultStatus.SUCCESS);
        }

        result.setEnded(new Date());
        result.setTook(result.getEnded().getTime() - result.getStarted().getTime());
        return result;
    }

    @PostMapping("/top5id")
    public ApiResult top5ById(@RequestParam Long artistId) throws ExecutionException, InterruptedException {
        ApiResult<Album> result = new ApiResult();
        result.setStarted(new Date());

        TopAlbums topAlbums = topAlbumsRepo.findByArtistArtistId(artistId);
        //we check for new albums every 12 hours from iTunes
        if(topAlbums == null || topAlbums.getUpdated().before(new Date(System.currentTimeMillis() - TWELVE_HOURS))) {
            try {
                List<Album> found = iTunesService.lookupById(artistId);
                if (CollectionUtils.isEmpty(found)) {
                    result.setStatus(ApiResultStatus.SUCCESS);
                    result.setError("No albums found for artist(id=" + artistId + ")");
                } else {
                    if (topAlbums == null) {
                        topAlbums = new TopAlbums(artistRepo.findById(artistId).get(), found);
                    } else {
                        topAlbums.setUpdated(new Date());
                        topAlbums.setAlbums(found);
                    }
                    topAlbumsRepo.save(topAlbums);
                    result.setResult(found);
                    result.setStatus(ApiResultStatus.SUCCESS);
                }
            } catch (Exception e) {
                result.setError(e.getMessage());
                result.setStatus(ApiResultStatus.ERROR);
            }
        } else {
            result.setResult(topAlbums.getAlbums());
            result.setStatus(ApiResultStatus.SUCCESS);
        }

        result.setEnded(new Date());
        result.setTook(result.getEnded().getTime() - result.getStarted().getTime());
        return result;
    }
}