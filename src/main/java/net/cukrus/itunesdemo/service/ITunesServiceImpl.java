package net.cukrus.itunesdemo.service;

import net.cukrus.itunesdemo.model.Album;
import net.cukrus.itunesdemo.model.Artist;
import net.cukrus.itunesdemo.model.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ITunesServiceImpl implements ITunesService {
    private static final Logger LOG = LoggerFactory.getLogger(ITunesServiceImpl.class);
    private static final String SEARCH_URL = "https://itunes.apple.com/search?entity=allArtist&term=";
    private static final String LOOKUP_BY_AMG_URL = "https://itunes.apple.com/lookup?entity=album&limit=5&amgArtistId=";
    private static final String LOOKUP_BY_ID_URL = "https://itunes.apple.com/lookup?entity=album&limit=5&id=";
    private static final String UTF8 = "UTF-8";
    /**
     * 100 calls/h = 1 call/36000millis
     */
    private static final long API_CALL_INTERVAL = 36000L;
    private static AtomicLong lastCallAt = new AtomicLong(0);

    @Autowired
    private RestTemplate restTemplate;
    private ThreadPoolExecutor executor;

    public ITunesServiceImpl() {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(100);
        executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, blockingQueue);
        executor.prestartAllCoreThreads();
    }

    @Override
    public List<Artist> searchArtist(String term) throws ExecutionException, InterruptedException {
        Callable<List<Artist>> callable = () -> {
            delayIfNeeded();
            List<Artist> result = new ArrayList<>();
            URI uri = URI.create(SEARCH_URL + UriUtils.encodeQueryParam(term, UTF8));
            ITunesResult response = restTemplate.postForObject(uri, null, ITunesResult.class);
            if (response != null && response.resultCount > 0) {
                result.addAll(response.results.stream().filter(w -> w.getObjectType().equals("Artist"))
                        .map(w -> (Artist) w).collect(Collectors.toList()));
            }
            return result;
        };
        return executor.submit(callable).get();
    }

    @Override
    public List<Album> lookupByAmg(Long amg) throws ExecutionException, InterruptedException {
        Callable<List<Album>> callable = () -> {
            delayIfNeeded();
            List<Album> result = new ArrayList<>();
            Artist artist = null;
            URI uri = URI.create(LOOKUP_BY_AMG_URL + amg);
            ITunesResult response = restTemplate.postForObject(uri, null, ITunesResult.class);
            if (response != null && response.resultCount > 1) {
                for(Wrapper wrap : response.results) {
                    String objType = wrap.getObjectType();
                    if(objType.equals("Artist")) {
                        artist = (Artist) wrap;
                    } else if(objType.equals("Album")) {
                        Album album = (Album) wrap;
                        album.setArtist(artist);
                        result.add(album);
                    }
                }
            }
            return result;
        };
        return executor.submit(callable).get();
    }

    @Override
    public List<Album> lookupById(Long id) throws ExecutionException, InterruptedException {
        Callable<List<Album>> callable = () -> {
            delayIfNeeded();
            List<Album> result = new ArrayList<>();
            Artist artist = null;
            URI uri = URI.create(LOOKUP_BY_ID_URL + id);
            ITunesResult response = restTemplate.postForObject(uri, null, ITunesResult.class);
            if (response != null && response.resultCount > 1) {
                for(Wrapper wrap : response.results) {
                    String objType = wrap.getObjectType();
                    if(objType.equals("Artist")) {
                        artist = (Artist) wrap;
                    } else if(objType.equals("Album")) {
                        Album album = (Album) wrap;
                        album.setArtist(artist);
                        result.add(album);
                    }
                }
            }
            return result;
        };
        return executor.submit(callable).get();
    }

    /**
     * Method that makes sure iTunes API calls happen not sooner than 36000 millis apart
     */
    private void delayIfNeeded() {
        long now = System.currentTimeMillis();
        long sinceLastCall = now - lastCallAt.get();
        if (sinceLastCall < API_CALL_INTERVAL) {
            try {
                Thread.sleep(API_CALL_INTERVAL - sinceLastCall);
            } catch (InterruptedException e) {
                LOG.error("sleep to next iTunes API call interrupted", e);
            }
        }
        lastCallAt.set(System.currentTimeMillis());
    }

    private static class ITunesResult {
        private int resultCount;
        private List<Wrapper> results;

        public int getResultCount() {
            return resultCount;
        }

        public void setResultCount(int resultCount) {
            this.resultCount = resultCount;
        }

        public List<Wrapper> getResults() {
            return results;
        }

        public void setResults(List<Wrapper> results) {
            this.results = results;
        }
    }
}