/**
 *
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
 * File name: 	Instance.java
 * Package: cs.man.ac.uk.data
 * Created:	September 30th, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.data;

import java.util.Vector;

/**
 * The basic data instance.
 * 
 * @author Rob Lyon
 */
public class Instance implements I_Instance
{
	/**
	 *	Variables
	 */

	/**
	 * The class label for this instance.
	 */
	private int classLabel = -1;

	/**
	 * The training weight for this instance.
	 */
	private double weight = 1.0;

	/**
	 * The features that describe this data instance.
	 */
	private Vector<Double> features = new Vector<Double>();

	/**
	 *	Methods
	 */

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#getClassLabel()
	 */
	@Override
	public int getClassLabel() { return this.classLabel;}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#getFeature(int)
	 */
	@Override
	public double getFeature(int f)
	{
		if(this.features != null)
		{
			if(f > 0 && f < this.features.size())
				return features.get(f);
			else
				return Double.NaN;
		}
		else
			return Double.NaN;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#getFeatures()
	 */
	@Override
	public Vector<Double> getFeatures()
	{
		if(this.features != null)
		{
			if(this.features.size() > 0)
				return features;
			else
				return new Vector<Double>();
		}
		else
			return new Vector<Double>();
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#setClasslabel(int)
	 */
	@Override
	public void setClasslabel(int i) { this.classLabel = i; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#setFeature(int, double)
	 */
	@Override
	public boolean setFeature(int f, double value) 
	{
		if(this.features != null)
		{
			if(f > 0 && f < this.features.size())
			{	
				features.set(f, value);
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#setFeatures(double[])
	 */
	@Override
	public boolean setFeatures(double[] values)
	{
		this.features.clear();

		for(int i=0;i<values.length;i++)
			this.features.add(values[i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#addFeature(double)
	 */
	@Override
	public void addFeature(double f) { this.features.add(f); }
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#removeFeature(int)
	 */
	@Override
	public void removeFeature(int f) { this.features.remove(f); }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String value = "";

		for (int i=0;i<features.size();i++)
			value += features.get(i).toString() + ",";

		value += this.classLabel;

		return value;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#getFeatureCount()
	 */
	@Override
	public int getFeatureCount() 
	{
		if(features != null)
			return features.size();
		else
			return 0;
	}

	/*
	 *	MOA METHODS COMPATIBILITY.
	 */


	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#weight()
	 */
	public double weight(){ return this.weight;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#classIsMissing()
	 */
	public boolean classIsMissing()
	{
		if(this.classLabel == -1)
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#isMissing(int)
	 */
	public boolean isMissing(int attIndex)
	{
		if(features != null)
		{
			if (Double.isNaN(features.elementAt(attIndex)))
			{
				return true;
			}
			return false;
		}
		else
			return true;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#numAttributes()
	 */
	public int numAttributes() { return this.features.size(); }
	
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#classValue()
	 */
	public double classValue() { return (double) this.classLabel; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_Instance#value(int)
	 */
	public double value(int attIndex)
	{
		if(features != null)
			return features.elementAt(attIndex);
		else
			return Double.NaN;
	}
}