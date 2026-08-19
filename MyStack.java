/*
Author: Jason Ash
Professor: Dr. AL-Agha
Course: CSCI 2350, Programming and Data Structures, Summer E-Learn2
Date: 2026-07-28
File: MyStack.java
Description: Uses a custom MyArrayList that extends AbstractList class and both of them implement a List interface, MyStack implements Stack interface and uses MyArrayList as a composition to create a stack, and all of them use generics. I came up with the Stack interface and the MyStack class on my own after reading the respective section in our textbook. I am reusing my implementation here since it matches the methods in the template and Assignment Guidance Document.
Sources: My implementation for List, AbstractList, and MyArrayList closely follow what is in our textbook since they use generics but not comparable or iterator. I'm not citing AI because I only used that as a starting point for a previous version of MyArrayList that used raw, object values and not generics. Now that I figured out how to implement it with generics, that citation no longer applies. I also followed a suggestion in the third chapter on sorted and unsorted lists in Object-Oriented Data Structures by N. Dale, D.T. Joyce, and C. Weems on returning a copy of the object that was gotten or removed from a list to ensure information hidding and better encapsulation.
*/

interface List<E>
{
	// Append an element to the end of the list
	void add(E e);

	// Insert elements at a specified index
	void add(int index, E e);

	// Remove all elements from the list
	void clear();

	// Check if element exists
	boolean contains(E e);

	// Retrieve element at specified index
	E get(int index);

	// Get index of specified element
	int indexOf(E e);
	
	// Check if list is empty
	boolean isEmpty();

	// Get last occurrence of element
	int lastIndexOf(E e);

	// Remove element
	boolean remove(E e);

	// Remove element at index
	E remove(int index);

	// Replace element
	Object set(int index, E e);

	// Get number of elements
	int size();
}

abstract class AbstractList<E> implements List<E>
{
	// Stores the number of elements
	protected int size = 0;

	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public boolean remove(E e)
	{
		if (indexOf(e) >= 0)
		{
			remove(indexOf(e));
			return true;
		}
		else
		{
			return false;
		}
	}

	// Append an element to the end of the list
	public abstract void add(E e);

	// Insert elements at a specified index
	public abstract void add(int index, E e);

	// Remove all elements from the list
	public abstract void clear();

	// Check if element exists
	public abstract boolean contains(E e);

	// Retrieve element at specified index
	public abstract E get(int index);

	// Get index of specified element
	public abstract int indexOf(E e);
	
	// Get last occurrence of element
	public abstract int lastIndexOf(E e);

	// Remove element at index
	public abstract E remove(int index);

	// Replace element
	public abstract Object set(int index, E e);
}

class MyArrayList<E> extends AbstractList<E> implements List<E>
{
	public static final int INITIAL_CAPACITY = 16;

	private E[] data = (E[])(new Object[INITIAL_CAPACITY]);

	// Number of elements in the list
	// private int current = 0;

	// Create an empty list with no-arg constructor
	public MyArrayList()
	{
	}

	// Create a list from an array of objects
	public MyArrayList(E[] e)
	{
		E item;
		for (int i = 0; i < e.length; i++)
		{
			item = e[i];
			// Warning: don't use super(objects)!
			add(item);
		}
	}

	// Add a new element at the specified index
	@Override
	public void add(int index, E e)
	{
		if (index < 0 || index > size)
		{
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}

		ensureCapacity();

		// Move the elements to the right after the specified index
		for (int i = size - 1; i >= index; i--)
		{
			data[i + 1] = data[i];
		}

		// Insert new element at data[index]
		E item = e;
		data[index] = item;

		// Increase size by 1
		size++;
	}

	@Override
	public void add(E e)
	{
		ensureCapacity();
		E item = e;
		data[size] = item;
		size++;
	}

	// A helper method that creates a new larger array by doubling the current size + 1
	private void ensureCapacity()
	{
		if(size >= data.length)
		{
			E[] newData = (E[])(new Object[size * 2 + 1]);
			copy(data, newData);
			data = newData;
		}
	}

	private void copy(E[] myData, E[] resizedData)
	{	
		// Avoid ArrayIndexOutOfBounds error
		int maxIndex = 0;
		maxIndex = (myData.length <= resizedData.length) ? myData.length : resizedData.length;

		for(int i = 0; i < maxIndex; i++)
		{
			resizedData[i] = data[i]; 
		} // end for loop
	} // end copy()

	// Clear the list. Dangerous: use only if intended
	@Override
	public void clear()
	{
		data = (E[])(new Object[INITIAL_CAPACITY]);
		size = 0;
	}

	// Return true if this list contains the element
	@Override
	public boolean contains(E e)
	{
		for(int i = 0; i < size; i++)
		{
			if (e.equals(data[i]))
			{
				return true;
			}
		}
		return false;
	}

	// Return the element at the specified index
	@Override
	public E get(int index)
	{
		checkIndex(index);
		return data[index];
	}

	// Helper method that throws an IndexOutOfBoundsException if the index is out-of-bounds
	private void checkIndex(int index)
	{
		if (index < 0 || index >= size)
		{
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
	}

	// Return the index of the first matching element in this list. Return -1 if there is no match.
	@Override
	public int indexOf(E e)
	{
		for (int i = 0; i < size; i++)
		{
			if (e.equals(data[i]))
			{
				return i;
			}
		}

		return -1;
	}

	// Return the index of the last matching element in this list. Return -1 if there is no match
	@Override
	public int lastIndexOf(E e)
	{
		for(int i = size - 1; i >=0; i--)
		{
			if(e.equals(data[i]))
			{
				return i;
			}
		}

		return -1;
	}

	// Remove the element at the specified position in this list. Shift any subsequent elements to the left. Return the element that was removed from the list.
	@Override
	public E remove(int index)
	{
		checkIndex(index);
		E e = data[index];
		
		// Shift data to the left
		for(int j = index; j < size - 1; j++)
		{
			data[j] = data[j + 1];
		}

		// This element is now null
		data[size - 1] = null;

		// Decrement size
		size--;
		// current--;
		
		return e;
	}

	// Replace the element at the specified position in this list with the specified element
	@Override
	public Object set(int index, E e)
	{
		checkIndex(index);
		ensureCapacity();
		E old = data[index];
		data[index] = e;
		return old;
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder("[");

		for (int i = 0; i < size; i++)
		{
			result.append(String.valueOf(data[i]));
			if (i < size - 1)
			{
				result.append(", ");
			}
		}
		return result.toString() + "]";
	}

	// Trims the capacity to current size
	public void trimToSize()
	{
		if(size != data.length)
		{
			E[] newData = (E[])(new Object[size]);
			copy(data, newData);
			data = newData;
		}
	}
}

// A stack interface for MyStack that defines what a stack should do

interface Stack<E>
{
	// Push an object onto the stack
	public void push(E item);

	// Pop an object off the stack by removing it and returning it to the caller
	public E pop();

	// Return the top element from the stack without removing it
	public E peek();

	// Returns true or false based on whether the stack is empty
	public boolean isEmpty();

	// Returns the size of the stack
	public int size();

	// Returns the contents of the stack as a string
	@Override
	public String toString();
}

// This version of MyStack uses MyArrayList as a composition to make a stack

public class MyStack<E> implements Stack<E>
{	
	private MyArrayList<E> stack = new MyArrayList<>();

	@Override
	public void push(E item)
	{
		stack.add(item);
	}

	@Override
	public E pop()
	{
		return stack.remove(stack.size() - 1);
	}

	@Override
	public E peek()
	{
		return stack.get(stack.size() - 1);
	}

	@Override
	public boolean isEmpty()
	{
		return stack.isEmpty();
	}
	
	@Override
	public int size()
	{
		return stack.size();
	}

	@Override
	public String toString()
	{
		return stack.toString();
	}

	public static void main(String[] args)
	{
		MyStack<String> stack = new MyStack<>();
	
		System.out.println("Is the stack empty? " + stack.isEmpty());

		stack.push("Hello");
		System.out.println("Stack: " + stack.toString());
		stack.push("Canada");
		System.out.println("Stack: " + stack.toString());
		stack.push("Greenland");
		System.out.println("Stack: " + stack.toString());
		System.out.println("The stack size is now " + stack.size());
		
		System.out.println("Peeking at the element on the top of the stack, it is " + stack.peek());

		System.out.println("Is the stack empty? " + stack.isEmpty());
		
		System.out.println("Stack: " + stack.toString());

		// While the stack isn't empty, pop the items off the stack by removing them and returning them back to the caller.
		while(!stack.isEmpty())
		{
			System.out.println("Popping " + stack.pop() + " off the stack.");
		}

		System.out.println("Is the stack empty now? " + stack.isEmpty());
		System.out.println("The size of the stack is now " + stack.size());
				
		stack.push("Panama");
		System.out.println("Stack: " + stack.toString());
		System.out.println("The stack size is now " + stack.size());
		System.out.println("Is the stack empty? " + stack.isEmpty());
		System.out.println("Peeking at the element on the top of the stack, it is " + stack.peek());
	}
/*
Works Cited:
Dale, Nell, Joyce, Daniel T., and Weems, Chip. Object-Oriented Data Structures Using Java. Jones and Bartlett Learning, 2002. 
Liang, Y. Daniel. Introduction to Java Programming and Data Structures. 13th ed., Pearson Education Limited, 2024.
*/
} // end class MyStack
