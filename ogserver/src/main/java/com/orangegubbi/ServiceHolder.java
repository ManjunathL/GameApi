package com.orangegubbi;

import com.orangegubbi.config.ConfigHolder;
import com.orangegubbi.db.SequenceIdGenerator;

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
