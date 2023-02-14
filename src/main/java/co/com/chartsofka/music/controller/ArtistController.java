package co.com.chartsofka.music.controller;

import co.com.chartsofka.music.dto.AlbumDTO;
import co.com.chartsofka.music.dto.ArtistDTO;
import co.com.chartsofka.music.service.impl.ArtistServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200/")
public class ArtistController {
    @Autowired
    ArtistServiceImpl artistService;

    @GetMapping("/artists")
    private ResponseEntity<List<ArtistDTO>> obtenerArtistas(){
        return artistService.getArtists().isEmpty() ? ResponseEntity.status(204).body(Collections.emptyList()) : ResponseEntity.ok(artistService.getArtists());
    }

    @GetMapping("/artists/{id}")
    private ResponseEntity<ArtistDTO> obtenerArtistaPorId(@PathVariable("id") String idArtist){
        return artistService.findArtistById(idArtist) == null ? ResponseEntity.status(404).body(new ArtistDTO()) : ResponseEntity.ok(artistService.findArtistById(idArtist));
    }

    @GetMapping("/artists/byType/{type}")
    private ResponseEntity<List<ArtistDTO>> getArtistByType(@PathVariable("type") String type){
        return artistService.getArtistByType(type) == null ?
                ResponseEntity.status(404).body(new ArrayList<>()) :
                ResponseEntity.ok(artistService.getArtistByType(type));
    }

    @PostMapping("/artists")
    private ResponseEntity<ArtistDTO> guardarArtista(@RequestBody ArtistDTO artistDTO){
        ArtistDTO artistSaved = artistService.saveArtist(artistDTO);
        return  artistSaved == null ? ResponseEntity.status(400).body(artistDTO) : ResponseEntity.status(201).body(artistSaved);
    }

    @PutMapping("/artists")
    private ResponseEntity<ArtistDTO> updateArtist(@RequestBody ArtistDTO artistDTO){
        return ResponseEntity.ok().body(artistService.updateArtist(artistDTO));
    }

    @DeleteMapping("/artists/{id}")
    private ResponseEntity<ArtistDTO> deleteArtist(@PathVariable("id") String idArtist){
        artistService.deleteArtist(idArtist);
        return artistService.findArtistById(idArtist) == null ? ResponseEntity.status(204).build():
                ResponseEntity.status(200).build();
    }

}
