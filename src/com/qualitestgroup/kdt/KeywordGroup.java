package com.qualitestgroup.kdt;

import com.qualitestgroup.kdt.exceptions.KDTException;

/**
 * Abstract class for storing a group of keywords. Individual keywords should be
 * defined as public static nested classes that extend {@link Keyword}.
 * 
 * @author Matthew Swircenski
 *
 */
public abstract class KeywordGroup {

	public Keyword getK(String Keyword) throws KDTException {
		// log("In class " + this.getClass().getName());
		String kwclass = this.getClass().getName() + "$" + Keyword;
		try {
			// log("Looking for " + kwclass);
			Class<? extends Keyword> bb = Class.forName(kwclass).asSubclass(Keyword.class);
			Keyword ret = bb.newInstance();
			return ret;
		} catch (Exception e) {
			// e.printStackTrace();
			throw new KDTException("Could not find keyword " + Keyword, e);
		}
	}

}
