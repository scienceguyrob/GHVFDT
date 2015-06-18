/**
 * This file is part of GHVFDT.
 *
 * GHVFDT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GHVFDT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GHVFDT.  If not, see <http://www.gnu.org/licenses/>.
 *
 * File name: 	AutoExpandVector.java
 * Package: cs.man.ac.uk.utils
 * Created:	September 30th, 2013
 * Author:	Richard Kirkby (rkirkby@cs.waikato.ac.nz), Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */

package cs.man.ac.uk.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Vector with the capability of automatic expansion.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class AutoExpandVector<T> extends ArrayList<T> 
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * Used or serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public AutoExpandVector() { super(0); }

	/**
	 * Creates a new expandable vector.
	 * @param size the size of the vector to create.
	 */
	public AutoExpandVector(int size) { super(size); }

	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int pos, T obj) 
	{
		if (pos > size()) 
		{
			while (pos > size()) 
				add(null);

			trimToSize();
		}
		super.add(pos, obj);
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#get(int)
	 */
	@Override
	public T get(int pos) 
	{ 
		return ((pos >= 0) && (pos < size())) ? super.get(pos) : null;
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#set(int, java.lang.Object)
	 */
	@Override
	public T set(int pos, T obj) 
	{
		if (pos >= size()) 
		{
			add(pos, obj);
			return null;
		}
		return super.set(pos, obj);
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(T arg0) 
	{
		boolean result = super.add(arg0);
		trimToSize();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends T> arg0) 
	{
		boolean result = super.addAll(arg0);
		trimToSize();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		boolean result = super.addAll(arg0, arg1);
		trimToSize();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#clear()
	 */
	@Override
	public void clear() 
	{
		super.clear();
		trimToSize();
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#remove(int)
	 */
	@Override
	public T remove(int arg0) 
	{
		T result = super.remove(arg0);
		trimToSize();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object arg0) 
	{
		boolean result = super.remove(arg0);
		trimToSize();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#removeRange(int, int)
	 */
	@Override
	protected void removeRange(int arg0, int arg1) 
	{
		super.removeRange(arg0, arg1);
		trimToSize();
	}
}