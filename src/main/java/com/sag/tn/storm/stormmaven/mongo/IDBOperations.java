package com.sag.tn.storm.stormmaven.mongo;

import java.util.List;

public interface IDBOperations<T> {
	
	public void insert(T o);

	public void update();

	public List<T> getAll();

	public Object get(T o);

	public void deleteAll();

	public void delete(T o);

}
