/**
 * 
 */
package com.anz.HttpToMQ.compute;

import com.anz.HttpToMQ.error.TransformFailureResponse;

import com.anz.common.compute.TransformType;
import com.anz.common.compute.impl.CommonErrorTransformCompute;
import com.anz.common.transform.ITransformer;
import com.ibm.broker.plugin.MbMessageAssembly;

/**
 * @author root
 *
 */
public class FailureTransformCompute extends CommonErrorTransformCompute {

	@Override
	public ITransformer<MbMessageAssembly, String> getTransformer() {
		return new TransformFailureResponse();
	}

	@Override
	public TransformType getTransformationType() {
		// TODO Auto-generated method stub
		return TransformType.HTTP_MQ;
	}

}
