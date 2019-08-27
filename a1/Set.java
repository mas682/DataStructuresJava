package cs445.a1;

// There are no abstract methods to override in this interface, but declaring
// that you implement it allows objects of this type to be written out to disk
// or over the network.
import java.io.Serializable;
import java.util.Arrays;

/**
 * Set is an interface that describes the operations of the ADT set. A set is a
 * homogeneous collection of objects. It is unordered, there are no limits on
 * the number of items it can store, and it cannot contain duplicate items.
 *
 */
public class Set<E> implements SetInterface<E>
{	
	private E[] set;
	private int numOfEntries;
	private boolean initialized = false;
	private static final int DEFAULT_CAPACITY = 25;
	private static final int MAX_CAPACITY = 10000;
	
	/** Creates an empty set whose initial capacity is 25. */
	public Set()
	{
		this(DEFAULT_CAPACITY);
	}
	
	/** Creates an empty set having a given capacity.
		@param capacity The integer capacity. */
	public Set(int capacity)
	{
		if(capacity <= MAX_CAPACITY)
		{
			@SuppressWarnings("unchecked")
			E[] tempSet = (E[])new Object[capacity];
			set = tempSet;
			numOfEntries = 0;
			initialized = true;
		}
		else
			throw new IllegalStateException("Attempt to create a set " +
											  "whose capacity exceeds " +
											  "allowed maximim.");
	}
	
	/** Creates a set and assigns an array of entries to it.
		@param entries The array assigned to a set.*/
	public Set(E[] entries) throws SetFullException
	{
		int capacity = entries.length;
		int index = 0;
		
		@SuppressWarnings("unchecked")
		E[] temp = (E[])new Object[capacity];
		
		for(int i = 0; i < capacity; i++)
		{
			if(entries[i] == (null))
			{
				break;
			}
			//checking to see if the entrie is already in the temporary bag
			else if(getIndexOf(entries[i],temp) <= -1)
			{
				temp[index] = entries[i];
				index++;
			}
		}
		if(index <= MAX_CAPACITY)
		{
		@SuppressWarnings("unchecked")
		E[] tempSet = (E[])new Object[index];
		tempSet = temp;
		set = tempSet;
		numOfEntries = index;
		initialized = true;
		}
		else
		{
			throw new SetFullException("Attempt to create a set " +
											  "whose capacity exceeds " +
											  "allowed maximim.");
		}
	}
	
	
    /**
     * Determines the current number of entries in this set.
     *
     * @return  The integer number of entries currently in this set
     */
    public int getCurrentSize()
	{
		return numOfEntries;
	}

    /**
     * Determines whether this set is empty.
     *
     * @return  true if this set is empty; false if not
     */
    public boolean isEmpty()
	{
		if(numOfEntries <= 0)//double check this method
			return true;
		else
			return false;
	}

    /**
     * Adds a new entry to this set, avoiding duplicates.
     *
     * <p> If newEntry is not null, this set does not contain newEntry, and this
     * set has available capacity (if fixed), then add modifies the set so that
     * it contains newEntry. All other entries remain unmodified. Duplicates are
     * determined using the .equals() method.
     *
     * <p> If newEntry is null, then add throws IllegalArgumentException without
     * modifying the set. If this set already contains newEntry, then add
     * returns false without modifying the set. If this set has a capacity
     * limit, and does not have available capacity, then add throws
     * SetFullException without modifying the set.
     *
     * @param newEntry  The object to be added as a new entry
     * @return  true if the addition is successful; false if the item already is
     * in this set
     * @throws SetFullException  If this set has a fixed capacity and does not
     * have the capacity to store an additional entry
     * @throws IllegalArgumentException  If newEntry is null
     */
    public boolean add(E newEntry) throws SetFullException,
                                          IllegalArgumentException
	{
		checkInitialization();
		boolean result = true;
		
		if(newEntry == null)
		{
			throw new IllegalArgumentException("The entry is null.");
		}
		if(numOfEntries >= set.length)
		{
			doubleCapacity();
		}	
		 
		if(contains(newEntry) == false)
		{
			set[numOfEntries] = newEntry;
			numOfEntries++;	
		}
		return result;

	}	

    /**
     * Removes a specific entry from this set, if possible.
     *
     * <p> If this set contains the entry, remove will modify the set so that it
     * no longer contains entry. All other entries remain unmodified.
     * Identifying this entry is accomplished using the .equals() method.
     *
     * <p> If this set does not contain entry, remove will return false without
     * modifying the set. If entry is null, then remove throws
     * IllegalArgumentException without modifying the set.
     *
     * @param entry  The entry to be removed
     * @return  true if the removal was successful; false if not
     * @throws IllegalArgumentException  If entry is null
     */
    public boolean remove(E entry) throws IllegalArgumentException
	{
		checkInitialization();
		if(entry == null)
		{
			throw new IllegalArgumentException("The entry to be removed is null.");
		}	
		int pos = getIndexOf(entry, set);
		if(pos < 0)
		{
			return false;
		}
		else
		{
			E result = removeEntry(pos);
			return entry.equals(result);
		}
	}	

    /**
     * Removes an arbitrary entry from this set, if possible.
     *
     * <p> If this set contains at least one entry, remove will modify the set
     * so that it no longer contains one of its entries. All other entries
     * remain unmodified. The removed entry will be returned.
     *
     * <p> If this set is empty, remove will return null without modifying the
     * set. Because null cannot be added, a return value of null will never
     * indicate a successful removal.
     *
     * @return  The removed entry if the removal was successful; null otherwise
     */
    public E remove()
	{
		checkInitialization();
		E result;
		
		if(isEmpty())
		{
			result = null;	
		}
		else
		{
			result = removeEntry(numOfEntries - 1);
		}
		return result;
	}

    /**
     * Removes all entries from this set.
     *
     * <p> If this set is already empty, clear will not modify the set.
     * Otherwise, the set will be modified so that it contains no entries.
     */
    public void clear()
	{
		while(!isEmpty())
		{	
			remove();
		}
	}

    /**
     * Tests whether this set contains a given entry. Equality is determined
     * using the .equals() method.
     *
     * <p> If this set contains entry, then contains returns true. Otherwise
     * (including if this set is empty), contains returns false. If entry is
     * null, then remove throws IllegalArgumentException. The method never
     * modifies this set.
     *
     * @param entry  The entry to locate
     * @return  true if this set contains entry; false if not
     * @throws IllegalArgumentException  If entry is null
     */
    public boolean contains(E entry) throws IllegalArgumentException
	{
		if(entry.equals(null))
		{
			throw new IllegalArgumentException("The entry is null.");
		}
		checkInitialization();
		return getIndexOf(entry, set) > -1;
	}
    /**
     * Retrieves all entries that are in this set.
     *
     * <p> An array is returned that contains a reference to each of the entries
     * in this set. The returned array's length will be equal to the number of
     * elements in this set, and thus the array will contain no null values.
     *
     * <p> If the implementation of set is array-backed, toArray will not return
     * the private backing array. Instead, a new array will be allocated with
     * the appropriate capacity.
     *
     * @return  A newly-allocated array of all the entries in this set
     */
    public E[] toArray()
	{
		checkInitialization();
		
		@SuppressWarnings("unchecked")
		E[] result = (E[])new Object[numOfEntries];
		
		for(int index = 0; index < numOfEntries; index++)
		{
			result[index] = set[index];
		}
		return result;
	}
	
	public boolean isFull()
	{
		if(numOfEntries == MAX_CAPACITY)
			return true;
		else
			return false;
	}
	
	 // Removes and returns the entry at a given index within the array.
     // If no such entry exists, returns null.
     // Precondition: 0 <= givenIndex < numberOfEntries.
     // Precondition: checkInitialization has been called.
	private E removeEntry(int givenIndex)
	{
		E result = null;
      
		if (!isEmpty() && (givenIndex >= 0))
		{
         result = set[givenIndex];          // Entry to remove
         int lastIndex = numOfEntries - 1;
         set[givenIndex] = set[lastIndex];  // Replace entry to remove with last entry
         set[lastIndex] = null;             // Remove reference to last entry
         numOfEntries--;
		} // end if
      
      return result;
	}
	 
	 // Locates a given entry within the set.
	 // Returns the index of the entry, if located,
     // or -1 otherwise.
     // Precondition: checkInitialization has been called.
	private int getIndexOf(E anEntry, E[] entries)
	{
		int where = -1;
		boolean found = false;      
		int index = 0;
      
      while (!found && (index < entries.length))
		{
			if (anEntry.equals(entries[index]))
			{
				found = true;
				where = index;
			} // end if
         index++;
		} // end while
      
      // Assertion: If where > -1, anEntry is in the array bag, and it
      // equals bag[where]; otherwise, anEntry is not in the array.
		return where;
	}
	
	//throws an exception if this object is not initialized.
	private void checkInitialization()
	{
		if(!initialized)
			throw new SecurityException("Set object is not initialized properly.");
	}
	
	private void doubleCapacity() 
	{
		int newLength = 2 * set.length;
		set = Arrays.copyOf(set, newLength);
	}		
}
