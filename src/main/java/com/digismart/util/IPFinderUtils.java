package com.digismart.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.digismart.model.IPAddress;

public class IPFinderUtils {
	private Iterator<IPAddress> it;
	private List<IPAddress> list;

	private List<IPAddress> removedIps = new ArrayList<>();

	public IPFinderUtils(List<IPAddress> list) {
		this.list = list;
		it = list.iterator();
	}

	public IPAddress next() {
		// if we get to the end, start again
		try {
			if (!it.hasNext()) {
				list.removeAll(removedIps);
				it = list.iterator();
			}
			IPAddress infra = it.next();
			return infra;
		} catch (NoSuchElementException e) {
			return null;
		}

	}

	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	public void remove(IPAddress ip) {
		removedIps.add(ip);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IPFinderUtils [it=");
		builder.append(it);
		builder.append(", list=");
		builder.append(list);
		builder.append(", removedIps=");
		builder.append(removedIps);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
