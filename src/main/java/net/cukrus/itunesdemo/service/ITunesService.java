package net.cukrus.itunesdemo.service;

import net.cukrus.itunesdemo.model.Album;
import net.cukrus.itunesdemo.model.Artist;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ITunesService {
    List<Artist> searchArtist(String term) throws ExecutionException, InterruptedException;
    List<Album> lookupByAmg(Long amg) throws ExecutionException, InterruptedException;
    List<Album> lookupById(Long id) throws ExecutionException, InterruptedException;
}