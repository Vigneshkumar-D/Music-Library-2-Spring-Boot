package com.example.song.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.song.repository.SongRepository;
import com.example.song.model.SongRowMapper;
import com.example.song.model.Song;

// Write your code here
@Service
public class SongH2Service implements SongRepository{
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Song> getSongs(){
        List<Song> songList = db.query("select * from playlist", new SongRowMapper());
        ArrayList<Song> songs = new ArrayList<>(songList);
        return songs;
    }

    @Override
    public Song getSongById(int songId){
        try{
            Song song = db.queryForObject("select * from playlist where songId = ?", new SongRowMapper(), songId);
            return song;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public Song addSong(Song song){
        db.update("insert into playlist(songName, singer, lyricist, musicDirector) values(?, ?, ?, ?)", song.getSongName(), song.getSinger(), song.getLyricist(), song.getMusicDirector());
        Song newlyAddedSong  = db.queryForObject("select * from playlist where songName = ? and singer = ? ", new SongRowMapper(), song.getSongName(), song.getSinger());
        return newlyAddedSong;
    }

    @Override 
    public Song updateSong(int songId, Song song){
        if(song.getSongName() != null){
            db.update("update playlist set songName = ? where songId = ?", song.getSongName(), songId);
        }
         if(song.getLyricist() != null){
            db.update("update playlist set lyricist = ?  where songId = ?", song.getLyricist(),songId);
        }
         if(song.getSinger() != null){
            db.update("update playlist set singer = ?  where songId = ?", song.getSinger(), songId);
        }
         if(song.getMusicDirector() != null){
            db.update("update playlist set musicDirector = ?  where songId = ?", song.getMusicDirector(), songId);
        }
        return getSongById(songId);
    }

    @Override
    public void deleteSong(int songId){
        db.update("delete from playlist where songId = ?", songId);
    }
    
}