package kes5219.utils.misc;

//A class used for ObjLoader
public class Triple<T1, T2, T3> {
	public final T1 first;
	public final T2 second;
	public final T3 third;
	
	public Triple(T1 value1, T2 value2, T3 value3)
	{
		first = value1;
		second = value2;
		third = value3;
	}
	
	public Triple(T1 value1, T2 value2)
	{
		first = value1;
		second = value2;
		third = null;
	}
}
