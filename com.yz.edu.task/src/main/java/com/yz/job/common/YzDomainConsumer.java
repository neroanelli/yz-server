package com.yz.job.common;

import com.yz.edu.cmd.BaseCommand;
import com.yz.edu.domain.YzBaseDomain;

/**
 * 
 * @author lingdian 
 *
 * @param <K>
 * @param <V>
 */
public interface YzDomainConsumer<K extends BaseCommand, V extends YzBaseDomain> {

	/**
	 * 
	 * @param cmd
	 * @param domain
	 */
	public void consumer(K cmd, V domain);
}
