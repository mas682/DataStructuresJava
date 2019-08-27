package cs445.a1;

// There are no abstract methods to override in this interface, but declaring
// that you implement it allows objects of this type to be written out to disk
// or over the network.
import java.io.Serializable;


public class Profile implements ProfileInterface
{
	private Set<ProfileInterface> profiles;
	private String name;
	private String about;
	private int numOfFollowers = 0;
	
	public Profile()
	{
		name = "";
		about = "";
		profiles = new Set<ProfileInterface>();
		
	}
	
	public Profile(String userName, String blurb)
	{
		if(userName == null)
		{
			name = "";
		}
		else if(blurb == null)
		{
			about = "";
		}
		else
		{
			name = userName;
			about = blurb;
		}
		profiles = new Set<>();
		
	}
	
    /**
     * Sets this profile's name.
     *
     * <p> If newName is not null, then setName modifies this profile so that
     * its name is newName. If newName is null, then setName throws
     * IllegalArgumentException without modifying the profile, for example:
     *
     * <p> {@code throw new IllegalArgumentException("Name cannot be null")}
     *
     * @param newName  The new name
     * @throws IllegalArgumentException  If newName is null
     */
    public void setName(String newName) throws IllegalArgumentException
	{
		if(newName == null)
		{
			throw new IllegalArgumentException("Name cannot be null");
		}
		else
			name = newName;
		
	}

    /**
     * Gets this profile's name.
     *
     * @return  The name
     */
    public String getName()
	{
		return name;
	}

    /**
     * Sets this profile's "about me" blurb.
     *
     * <p> If newAbout is not null, then setAbout modifies this profile so that
     * its about blurb is newAbout. If newAbout is null, then setAbout throws
     * IllegalArgumentException without modifying the profile.
     *
     * @param newAbout  The new blurb
     * @throws IllegalArgumentException  If newAbout is null
     */
    public void setAbout(String newAbout) throws IllegalArgumentException
	{
		if(newAbout == null)
		{
			throw new IllegalArgumentException("About me cannot be null");
		}
		else
			about = newAbout;
	}

    /**
     * Gets this profile's "about me" blurb
     *
     * @return  The blurb
     */
    public String getAbout()
	{
		return about;
	}

    /**
     * Adds another profile to this profile's following set.
     *
     * <p> If this profile's following set is at capacity, or if other is null,
     * then follow returns false without modifying the profile. Otherwise, other
     * is added to this profile's following set and follow returns true. If this
     * profile already followed other, then follow returns true even though no
     * changes were needed.
     *
     * @param other  The profile to follow
     * @return  True if successful, false otherwise
     */
    public boolean follow(ProfileInterface other)
	{
		boolean added = true;
		
		if(other == null || profiles.isFull())
		{
			added = false;
		}
		else
		{
			
			try{
			added = profiles.add(other);
			numOfFollowers++;
			}
			catch(SetFullException e)
			{
			}
		}
		return added;
	}
	

    /**
     * Removes the specified profile from this profile's following set.
     *
     * <p> If this profile's following set does not contain other, or if other
     * is null, then unfollow returns false without modifying the profile.
     * Otherwise, this profile in modified in such a way that other is removed
     * from this profile's following set.
     *
     * @param other  The profile to follow
     * @return  True if successful, false otherwise
     */
    public boolean unfollow(ProfileInterface other)
	{
		boolean removed = true;
		
		if(other == null|| profiles.contains(other) == false)
		{
			removed = false;
		}
		else
		{
			removed = profiles.remove(other);
			numOfFollowers--;
		}
		return removed;
	}

    /**
     * Returns a preview of this profile's following set.
     *
     * <p> The howMany parameter is a maximum desired size. The returned array
     * may be less than the requested size if this profile is following fewer
     * than howMany other profiles. Clients of this method must be careful to
     * check the size of the returned array to avoid
     * ArrayIndexOutOfBoundsException.
     *
     * <p> Specifically, following returns an array of size min(howMany, [number
     * of profiles that this profile is following]). This array is populated
     * with arbitrary profiles that this profile follows.
     *
     * @param howMany  The maximum number of profiles to return
     * @return  An array of size &le;howMany, containing profiles that this
     * profile follows
     */
    public ProfileInterface[] following(int howMany)
	{
		Object[] followers = profiles.toArray();
		int size = 0;
		int numProfiles = followers.length;
		
		if(numProfiles < howMany)
			size = numProfiles;
		else
			size = howMany;
		
		ProfileInterface[] preview = new ProfileInterface[howMany];
		for(int i = 0; i < size; i++)
		{
			preview[i] = (ProfileInterface)followers[i];
		}
		
		return preview;		
	}

	
	public int getNumFollowers()
	{
		return numOfFollowers;
	}
    /**
     * Recommends a profile for this profile to follow. This returns a profile
     * followed by one of this profile's followed profiles. Should not recommend
     * this profile to follow someone they already follow, and should not
     * recommend to follow oneself.
     *
     * <p> For example, if this profile is Alex, and Alex follows Bart, and Bart
     * follows Crissy, this method might return Crissy.
     *
     * @return  The profile to suggest, or null if no suitable profile is found
     * (only if all of this profile's followees' followees are already followed
     * or this profile itself).
     */
    public ProfileInterface recommend()
	{
		boolean found = false;
		ProfileInterface person = null;
		int iterations = 0;


		ProfileInterface[] friends = new ProfileInterface[numOfFollowers];
		friends = following(numOfFollowers);
		while((iterations <= numOfFollowers) && (found == false))
		{
		for(int i = 0; i < numOfFollowers; i++)
		{
		ProfileInterface friend = friends[i];
		int num = ((Profile)friend).getNumFollowers();	

		ProfileInterface [] recommendedPeople = new ProfileInterface[num];
		recommendedPeople = friend.following(num);
		int index = 0;
		while((index <= num) && (found == false)){
		for(int j = 0; j < num; j++)
		{
			ProfileInterface recommended = recommendedPeople[j];
			if(this == recommended){	
				index ++;
			
			}
			else if(profiles.contains(recommended)){
				index++;
			}
			else{
				found = true;
				person = recommended;
				break;

			}
		}//end inner for loop
		}//end inner while loop
		}//end outter for loop
		iterations++;
		}//end outter while loop
		if(found == false)
			return null;
		else
			return person;
	}

}










