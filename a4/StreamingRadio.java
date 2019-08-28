package cs445.a4;

/**
 * This abstract data type represents the backend for a streaming radio service.
 * It stores the songs, stations, and users in the system, as well as the
 * ratings that users assign to songs.
 */
public interface StreamingRadio {

    /**
     * The abstract methods below are declared as void methods with no
     * parameters. You need to expand each declaration to specify a return type
     * and parameters, as necessary. You also need to include a detailed comment
     * for each abstract method describing its effect, its return value, any
     * corner cases that the client may need to consider, any exceptions the
     * method may throw (including a description of the circumstances under
     * which this will happen), and so on. You should include enough details
     * that a client could use this data structure without ever being surprised
     * or not knowing what will happen, even though they haven't read the
     * implementation.
     */

    /**
     * Adds a new song to the system at a given location.
	 * Songs at positions higher than the given postion are at the next
	 * higher position within the system. The systems size will then be
	 * increased by 1. Throws a indexOutOfBoundsException if the given position
	 * to add the song to is less than one or if the givenposition is larger than the 
	 * systems size + 1.
	 *
	 * @param givenPosition An integer that specifies the desired position of
	 * the new song.
	 * @param newSong The song to be added as a new entry.
	 * @throws NullPointerException if the song to add is null.
	 * @throws IndexOutOfBoundsException if either givenPosition < 1 or
	 * givenPosition > the systems size + 1.
     */
    public void addSong(int givenPosition, Song newSong);

    /**
     * Removes an existing song at a given position from the system.
	 * Songs originally at positions higher than the song
	 * are at the next lower position within the system, and the system's
	 * size is decreased by 1. This method will throw an IndexOutOfBoundsException
	 * if the position of the song to be removed is less than 1 or if the position
	 * of the song to be removed is greater than the size of the system + 1.
	 *
	 * @param givenPosition An integer that specifies the desired position of
	 * the song to be removed.
	 * @return The song being removed from the system.
	 * @throws NullPointerException if the system is null or the song to be removed is null.
	 * @throws IndexOutOfBoundsException if either givenPosition < 1 or
	 * givenPosition > the systems size + 1.
     */
	 
    public Song removeSong(int givenPosition);

    /**
     * Adds an existing song to the playlist for an existing radio station
	 * to the end of the playlist.  Entries currently in the station are unaffected.
	 * The stations song size is increased by 1. If the station already contains
	 * the song, the song is not added.
	 *
	 * @param newSong the song to be added to the station.
	 * @param theStation the station that is receveving the new song.
	 * @throws NullPointerException if either the song or the station are null.
     */
    public void addToStation(Song newSong, Station theStation);

    /**
     * Removes a song from the playlist for a radio station. Songs originally
	 * at positions higher in the playlist are at the next lower position within
	 * the playlist, and the playlists size is decreaed by 1. If the station does not
	 * have any songs in it, a NullPointerException will be thrown.
	 *
	 * @param theSong The song to be removed from the playlist of a given station.
	 * @param theStation The station that is having the song removed from its playlist.
	 * @return The song to be removed from the station.
	 * @throws NullPointerException if the playlist does not have
	 * at least one song in it.
     */
    public Song removeFromStation(Song theSong, Station theStation);

    /**
     * Sets a user's rating for a song, as a number of stars from 1 to 5.
	 * If the user has not previously rated the song, the rating will be changed
	 * to the number of stars that the user chooses and value will be returned.
	 * If the user has already rated this song, the rating is changed to the
	 * new rating that the user has given it.  If the user does not rate the song,
	 * the method will return null.
	 *
	 * @param theUser user who is placing the rating.
	 * @param theSong song for which the user is rating.
	 * @return an integer that corrolates to the number of stars that the 
	 * user has rated the song as, or null if user does not rate the song.
	 * @throws NullPointerException if either the user or the song is null.
     */
    public int rateSong(User theUser, Song theSong);

    /**
     * Clears a user's rating on a song. If this user has rated this song and
     * the rating has not already been cleared, then the rating is cleared and
     * the state will appear as if the rating was never made. If there is no
     * such rating on record (either because this user has not rated this song,
     * or because the rating has already been cleared), then this method will
     * throw an IllegalArgumentException.
     *
     * @param theUser user whose rating should be cleared.
     * @param theSong song from which the user's rating should be cleared.
     * @throws IllegalArgumentException if the user does not currently have a
     * rating on record for the song.
     * @throws NullPointerException if either the user or the song is null.
     */
    public void clearRating(User theUser, Song theSong)
    throws IllegalArgumentException, NullPointerException;

    /**
     * Predicts the rating a user will assign to a song that they have not yet
     * rated, as a number of stars from 1 to 5. If the song has not been previously
	 * rated by the user, the predicted number of stars will be returned.
	 * If the song passed to the method  has already been rated by the user,
	 * the method will throw an IllegalArgumentException. 
	 * 
	 * @param theUser user whose rating should be predicted.
	 * @param theSong song from which the predicted rating should be placed on.
	 * @return An integer from 1-5 that is the prediction of the rating.
	 * @throws NullPointerException if either the user or the song is null.
	 * @throws IllegalArgumentException if the song passed to the method has already
	 * been rated by the user.
     */
    public int predictRating(Song theSong, User theUser);

    /**
     * Suggests a song for a user that they are predicted to like.
	 * If this user has previously rated the song an IllegalArgumentException
	 * will be thrown as the method should not suggest songs that the user already
	 * has listened to.  If the user has not rated the song, the method will return the
	 * song to suggest to the user.
	 *
	 * @param theSong the song that is to be suggested to the user.
	 * @param theUser user who is receving the predicted song.
	 * @return The song for which is recommended for the user.
	 * @throws NullPointerException if the user is null or if the song is null.
	 * @throws IllegalArgumentException if the song passed to the method has been
	 * previously been rated by the user.
     */
    public Song suggestSong(User theUser, Song theSong);

}

