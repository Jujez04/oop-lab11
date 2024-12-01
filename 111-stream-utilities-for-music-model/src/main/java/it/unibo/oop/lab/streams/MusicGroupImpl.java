package it.unibo.oop.lab.streams;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  This class handles music group by their songs and albums.
 */
public final class MusicGroupImpl implements MusicGroup {

    private final Map<String, Integer> albums = new HashMap<>();
    private final Set<Song> songs = new HashSet<>();

    /**
     * Adds albums to the map set.
     * @param albumName
     * @param year  when it's publicated
     */
    @Override
    public void addAlbum(final String albumName, final int year) {
        this.albums.put(albumName, year);
    }

    /**
     * Adds songs to the set of songs.
     * @param albumName
     * @param duration
     */
    @Override
    public void addSong(final String songName, final Optional<String> albumName, final double duration) {
        if (albumName.isPresent() && !this.albums.containsKey(albumName.get())) {
            throw new IllegalArgumentException("invalid album name");
        }
        this.songs.add(new MusicGroupImpl.Song(songName, albumName, duration));
    }

    /**
     * Method that gives an ordered stream of songs.
     * @return an ordered Song stream
     */
    @Override
    public Stream<String> orderedSongNames() {
        return this.songs.stream().map(s -> s.getSongName()).sorted();
    }

    /**
     * It gives a stream of albums.
     * @return album stream
     */
    @Override
    public Stream<String> albumNames() {
        return this.albums.keySet().stream();
    }

    /**
     * It gives a stream of albums that are publicated in
     * the specified year.
     * @param year 
     * @return album stream in that year
     */
    @Override
    public Stream<String> albumInYear(final int year) {
        return this.albums.entrySet().stream()
            .filter(a -> a.getValue() == year)
            .map(a -> a.getKey());
    }

    /**
     * It gives the number of songs in the specified album.
     * @param albumName
     * @return the number of songs
     */
    @Override
    public int countSongs(final String albumName) {
        return this.songs.stream()
            .filter(s -> s.getAlbumName().orElse("") == albumName)
            .mapToInt(s -> 1)
            .sum();
    }

    /**
     * It counts songs that aren't in any album.
     * @return the number of songs in no album
     */
    @Override
    public int countSongsInNoAlbum() {
        return this.countSongs("");
    }

    /**
     * With the album given in input, it calculates the avarage duration
     * of songs in the album specified.
     * @param albumName
     * @return an optional double that represents the avarage duration
     */
    @Override
    public OptionalDouble averageDurationOfSongs(final String albumName) {
        return this.songs.stream()
            .filter(s -> s.getAlbumName().orElse("") == albumName)
            .mapToDouble((s -> s.getDuration()))
            .average();
    }

    /**
     * It gives the song with the longest duration.
     * @return the longest song
     */
    @Override
    public Optional<String> longestSong() {
        return Optional.of(
            this.songs.stream()
                .max( (s1, s2) -> Double.compare(s1.getDuration(), s2.getDuration()))
                .get()
                .getSongName());
    }


    @Override
    public Optional<String> longestAlbum() {
        return this.songs.stream()
            .collect(Collectors.groupingBy(Song::getAlbumName, HashMap::new, Collectors.summingDouble(Song::getDuration)))
            .entrySet().stream()
            .collect(Collectors.maxBy(Comparator.comparingDouble(e -> e.getValue()) ))
            .get().getKey();
    }

    private static final class Song {

        private final String songName;
        private final Optional<String> albumName;
        private final double duration;
        private int hash;

        Song(final String name, final Optional<String> album, final double len) {
            super();
            this.songName = name;
            this.albumName = album;
            this.duration = len;
        }

        public String getSongName() {
            return songName;
        }

        public Optional<String> getAlbumName() {
            return albumName;
        }

        public double getDuration() {
            return duration;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = songName.hashCode() ^ albumName.hashCode() ^ Double.hashCode(duration);
            }
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Song) {
                final Song other = (Song) obj;
                return albumName.equals(other.albumName) && songName.equals(other.songName)
                        && duration == other.duration;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Song [songName=" + songName + ", albumName=" + albumName + ", duration=" + duration + "]";
        }

    }

}
