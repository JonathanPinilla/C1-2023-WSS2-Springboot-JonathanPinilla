package co.com.chartsofka.music.utils;

import co.com.chartsofka.music.dto.AlbumDTO;
import co.com.chartsofka.music.dto.ArtistDTO;
import co.com.chartsofka.music.dto.SongDTO;
import co.com.chartsofka.music.entity.Album;
import co.com.chartsofka.music.entity.Artist;
import co.com.chartsofka.music.entity.Song;
import co.com.chartsofka.music.repository.AlbumRepository;
import co.com.chartsofka.music.repository.SongRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MapperUtil {
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    public MapperUtil(SongRepository songRepository,
                      AlbumRepository albumRepository) {
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
    }

    public Function<Artist, ArtistDTO> artistToDTO(){
        return artist -> {
            return new ArtistDTO(
                    artist.getArtistID(),
                    artist.getName(),
                    artist.getCountry(),
                    artist.getDebutDate(),
                    artist.getEnterprise(),
                    artist.getType(),
                    artist.getAlbums() == null ?
                            new ArrayList<>() :
                            artist.getAlbums().stream()
                                    .map(album ->
                                            albumToDTOWithoutArtist().apply(album))
                                    .collect(Collectors.toList())
            );
        };
    }

    public Function<Artist, ArtistDTO> artistToDTOWithoutAlbum(){
        return artist -> {
            return new ArtistDTO(
                    artist.getArtistID(),
                    artist.getName(),
                    artist.getCountry(),
                    artist.getDebutDate(),
                    artist.getEnterprise(),
                    artist.getType(),
                    new ArrayList<>()
            );
        };
    }

    public Function<Album, AlbumDTO> albumToDTOWithArtist(){
        return album -> new AlbumDTO(
                album.getAlbumID(),
                album.getTitle(),
                album.getTotalSongs(),
                album.getYearRelease(),
                album.getGenre(),
                artistToDTOWithoutAlbum().apply(album.getArtist()),
                album.getSongs() == null ?
                        new ArrayList<>() :
                        album.getSongs().stream()
                        .map(song -> songToDTOWithoutAlbum().apply(song))
                        .collect(Collectors.toList())
        );
    }

    public Function<Album, AlbumDTO> albumToDTOWithoutArtist(){
        return album -> new AlbumDTO(
                album.getAlbumID(),
                album.getTitle(),
                album.getTotalSongs(),
                album.getYearRelease(),
                album.getGenre(),
                null,
                album.getSongs() == null ?
                        new ArrayList<>() :
                        album.getSongs().stream()
                        .map(song ->
                                songToDTOWithoutAlbum().apply(song))
                        .collect(Collectors.toList())
        );
    }

    public Function<Song, SongDTO> songToDTOWithAlbum(){
        return song -> new SongDTO(
                song.getSongID(),
                song.getName(),
                song.getDuration(),
                albumToDTOWithoutArtist().apply(song.getAlbum()),
                song.getPlayed()
        );
    }

    public Function<Song, SongDTO> songToDTOWithoutAlbum(){
        return song -> new SongDTO(
                song.getSongID(),
                song.getName(),
                song.getDuration(),
                null,
                song.getPlayed()
        );
    }


    //---------------------------------------dtoToEntity-------------------------
    public Function<ArtistDTO, Artist> dtoToArtist(){
        return artist -> new Artist(
                artist.getArtistID(),
                artist.getName(),
                artist.getCountry(),
                artist.getDebutDate(),
                artist.getEnterprise(),
                artist.getType(),
                artist.getAlbums() == null ?
                        new ArrayList<>() :
                        artist.getAlbums().stream()
                        .map(album ->
                                dtoToAlbumWithoutArtist()
                                        .apply(album))
                        .collect(Collectors.toList())
        );
    }

    public Function<ArtistDTO, Artist> dtoToArtistWithoutAlbum(){
        return artist -> new Artist(
                artist.getArtistID(),
                artist.getName(),
                artist.getCountry(),
                artist.getDebutDate(),
                artist.getEnterprise(),
                artist.getType(),
                new ArrayList<>()
        );
    }

    public Function<AlbumDTO, Album> dtoToAlbumWithArtist(){
        return album -> new Album(
                album.getAlbumID(),
                album.getTitle(),
                album.getTotalSongs(),
                album.getYearRelease(),
                album.getGenre(),
                dtoToArtistWithoutAlbum().apply(album.getArtist()),
                album.getSongs() == null ?
                        new ArrayList<>() :
                        album.getSongs().stream()
                        .map(song ->
                                dtoToSongWithoutAlbum().apply(song))
                        .collect(Collectors.toList())
        );
    }

    public Function<AlbumDTO, Album> dtoToAlbumWithoutArtist(){
        return album -> new Album(
                album.getAlbumID(),
                album.getTitle(),
                album.getTotalSongs(),
                album.getYearRelease(),
                album.getGenre(),
                null,
                album.getSongs()
                        .stream()
                        .map(song -> dtoToSongWithoutAlbum().apply(song))
                        .collect(Collectors.toList())
        );
    }

    public Function<SongDTO, Song> dtoToSongWithAlbum(){
        return song -> new Song(
                song.getSongID(),
                song.getName(),
                song.getDuration(),
                dtoToAlbumWithoutArtist().apply(song.getAlbum()),
                song.getPlayed()
        );
    }

    public Function<SongDTO, Song> dtoToSongWithoutAlbum(){
        return song -> new Song(
                song.getSongID(),
                song.getName(),
                song.getDuration(),
                null,
                song.getPlayed()
        );
    }
}
