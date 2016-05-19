/**
 * 
 */
package com.anz.HttpToMQ.compute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.anz.HttpToMQ.transform.PreTransformBLSample;

import com.anz.common.cache.impl.CacheHandlerFactory;
import com.anz.common.compute.TransformType;
import com.anz.common.compute.impl.CommonJavaCompute;
import com.anz.common.transform.ITransformer;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbMessageAssembly;

/**
 * @author sanketsw
 *
 */
public class RetrieveOriginalHeader extends CommonJavaCompute {
	
	private static final Logger logger = LogManager.getLogger();

	/* (non-Javadoc)
	 * @see com.anz.common.compute.impl.CommonJsonJsonTransformCompute#getTransformer()
	 */

	@Override
	public void execute(MbMessageAssembly inAssembly,
			MbMessageAssembly outAssembly) throws Exception {
		
		
		
		MbElement root = outAssembly.getMessage().getRootElement();
		
		MbElement correlId = root.getFirstElementByPath("/MQMD/CorrelId");
		
		MbElement replyToQ = root.getFirstElementByPath("/MQMD/ReplyToQ");
				
		String originalReplyToQ = CacheHandlerFactory.getInstance().lookupCache("MQHeaderCache", correlId.getValueAsString());
		
		if(originalReplyToQ != null){
			logger.info("RETURN TO Q RETRIEVED FROM CACHE");
			logger.info("temp reply to Q = {}", replyToQ.getValueAsString());
			replyToQ.setValue(originalReplyToQ);
			logger.info("original reply to Q = {}", replyToQ.getValueAsString());
		} else {
			//TODO: Error statments
			logger.info("ERROR: original Reply To Q not found in cache");
		}
		
		
		
	}

	@Override
	public TransformType getTransformationType() {
		// TODO Auto-generated method stub
		return TransformType.HTTP_MQ;
	}

}
