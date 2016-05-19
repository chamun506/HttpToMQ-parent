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
public class SaveModifyHeader extends CommonJavaCompute {
	
	private static final Logger logger = LogManager.getLogger();

	/* (non-Javadoc)
	 * @see com.anz.common.compute.impl.CommonJsonJsonTransformCompute#getTransformer()
	 */

	@Override
	public void execute(MbMessageAssembly inAssembly,
			MbMessageAssembly outAssembly) throws Exception {
		
		logger.info("Inside execute");
		// Get root element
		MbElement root = outAssembly.getMessage().getRootElement();
		
		
		// Add header to outAssemply
		//root.getFirstChild().addAfter(MQMD);
		
		// Get message ID element
		MbElement msgId = root.getFirstElementByPath("/MQMD/MsgId");
		logger.info("{} = {}", msgId.getName(), msgId.getValueAsString());
		
		// Get Correlation ID element
		MbElement correlId = root.getFirstElementByPath("/MQMD/CorrelId");
		logger.info("{} = {}", correlId.getName(), correlId.getValueAsString());
		
		// Get Reply To Queue element
		MbElement replyToQ = root.getFirstElementByPath("/MQMD/ReplyToQ");
		
		// Get Reply To Queue element
		//MbElement replyToQ = root.getFirstElementByPath("/MQMD/ReplyToQ");
		
				
		correlId.setValue(msgId.getValue());
		logger.info("new correlID = {}", correlId.getValueAsString());
		
		CacheHandlerFactory.getInstance().updateCache("MqHeaderCache", correlId.getValueAsString(), replyToQ.getValueAsString());
		logger.info("Values stored in cache");
		
		logger.info("original replyToQ = {}", replyToQ.getValueAsString());
		String newReplyToQ = "RESPONSE";
		replyToQ.setValue(newReplyToQ);
		logger.info("new replyToQ = {}", replyToQ.getValueAsString());
		
	}

	@Override
	public TransformType getTransformationType() {
		// TODO Auto-generated method stub
		return TransformType.HTTP_MQ;
	}

}
