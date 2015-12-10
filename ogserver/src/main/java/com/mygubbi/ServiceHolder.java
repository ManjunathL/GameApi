package com.mygubbi;

import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.SequenceIdGenerator;

public class ServiceHolder
{
	private static ConfigHolder configHolder;
	private static SequenceIdGenerator sequenceIdGenerator;

	public static ConfigHolder getConfigHolder()
	{
		return configHolder;
	}

	public static SequenceIdGenerator getSequenceIdGenerator()
	{
		return sequenceIdGenerator;
	}
}
