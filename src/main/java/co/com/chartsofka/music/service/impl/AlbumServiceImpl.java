package co.com.chartsofka.music.service.impl;

import co.com.chartsofka.music.dto.AlbumDTO;
import co.com.chartsofka.music.entity.Album;
import co.com.chartsofka.music.repository.AlbumRepository;
import co.com.chartsofka.music.service.IAlbumService;
import co.com.chartsofka.music.utils.DTOToEntity;
import co.com.chartsofka.music.utils.EntityToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements IAlbumService {
    @Autowired
    AlbumRepository albumRepository;

    @Override
    public Album dtoToEntity(AlbumDTO albumDTO) {
        return DTOToEntity.album(albumDTO);

    }

    @Override
    public AlbumDTO entityToDTO(Album album) {
        return EntityToDTO.album(album);
    }

    @Override
    public List<AlbumDTO> getAlbums() {
        return albumRepository.findAll()
                .stream().map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AlbumDTO> findAlbumById(String idAlbum) {
        //return entityToDTO(albumRepository.findById(idAlbum).orElseThrow(NoSuchElementException::new));
        return albumRepository.findById(idAlbum).map(EntityToDTO::album);
    }

    @Override
    public AlbumDTO saveAlbum(AlbumDTO albumDTO) {

        return entityToDTO(albumRepository.save(dtoToEntity(albumDTO)));
    }

    @Override
    public AlbumDTO updateAlbum(AlbumDTO albumDTO) {
        Album update = DTOToEntity.album(albumDTO);
        Album toUpdate = albumRepository.findById(update.getAlbumID()).orElse(null);
        if(toUpdate != null){
            toUpdate.setTitle(update.getTitle());
            toUpdate.setGenre(update.getGenre());
            toUpdate.setArtist(update.getArtist());
            toUpdate.setYearRelease(update.getYearRelease());
            toUpdate.setTotalSongs(update.getTotalSongs());
            return EntityToDTO.album(albumRepository.save(toUpdate));
        }
        return null;
    }

    @Override
    public void deleteAlbum(String idAlbum) {
        albumRepository.deleteById(idAlbum);
    }
}
