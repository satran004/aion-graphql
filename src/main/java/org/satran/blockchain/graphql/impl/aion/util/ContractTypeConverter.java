package org.satran.blockchain.graphql.impl.aion.util;

import org.aion.api.IContract;
import org.aion.api.IUtils;
import org.aion.api.sol.IAddress;
import org.aion.api.sol.IBool;
import org.aion.api.sol.IBytes;
import org.aion.api.sol.ISString;
import org.aion.api.type.ContractResponse;
import org.aion.base.type.Address;
import org.apache.commons.lang3.StringUtils;
import org.satran.blockchain.graphql.exception.DataConversionException;
import org.satran.blockchain.graphql.model.ContractResponseBean;
import org.satran.blockchain.graphql.model.Output;
import org.satran.blockchain.graphql.model.Param;
import org.satran.blockchain.graphql.model.SolidityType;
import org.satran.blockchain.graphql.model.input.ContractFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.satran.blockchain.graphql.impl.aion.util.TypeUtil.*;
import static org.satran.blockchain.graphql.impl.aion.util.TypeUtil.toBytes;
import static org.satran.blockchain.graphql.impl.aion.util.TypeUtil.toIInt;

public class ContractTypeConverter {

    private static Logger logger = LoggerFactory.getLogger(ContractTypeConverter.class);

    public static void populateParams(ContractFunction contractFunction, IContract contract) {

        if(contractFunction.getParams() == null)
            return;

        //set params
        for(Param param: contractFunction.getParams()) {

            boolean isArray = false;
            if(param.getValues() != null && param.getValues().size() > 0)
                isArray = true;

            if(param.getType() == SolidityType._address) {

                if(isArray)
                    contract.setParam(IAddress.copyFrom(toStringList(param.getValues())));
                else
                    contract.setParam(IAddress.copyFrom(String.valueOf(param.getValue())));

            } else if(param.getType() == SolidityType._bool) {

                if(isArray)
                    contract.setParam(IBool.copyFrom(toBooleanList(param.getValues())));
                else
                    contract.setParam(IBool.copyFrom(toBoolean(param.getValue())));

            } else if(param.getType() == SolidityType._bytes) {

                if(isArray)
                    contract.setParam(IBytes.copyFrom(toBytesList(param.getValues())));
                else
                    contract.setParam(IBytes.copyFrom(toBytes(String.valueOf(param.getValue()))));

            } else if(param.getType() == SolidityType._dbytes) {

                contract.setParam(IBytes.copyFrom(toBytes(String.valueOf(param.getValue()))));

            } else if(param.getType() == SolidityType._int) {

                if(isArray)
                    contract.setParam(toIInt(param.getValues()));
                else
                    contract.setParam(toIInt(param.getValue()));

            } else if(param.getType() == SolidityType._uint) {

                if(isArray)
                    contract.setParam(toIInt(param.getValues()));
                else
                    contract.setParam(toIInt(param.getValue()));

            } else if(param.getType() == SolidityType._string) {
                contract.setParam(ISString.copyFrom(String.valueOf(param.getValue())));
            } else
                throw new DataConversionException("Unable to convert input parameters : " + param);
        }
    }

    public static void populateOutputs(ContractFunction contractFunction,
                                 ContractResponse contractResponse, ContractResponseBean responseBean) {

        List<Output> outputParams = contractFunction.getOutputs();

        if(outputParams == null) {
            if(logger.isDebugEnabled()) {
                logger.debug("No output param defined. Please define output attribute in graphql query");
            }
            return;
        }

        List<Object> resultData = new ArrayList<>();

        if(logger.isDebugEnabled())
            logger.debug("** Print output **");

        for(int i=0; i<contractResponse.getData().size(); i++) {

            Object outputData = contractResponse.getData().get(i);

            if(logger.isDebugEnabled())
                logger.debug(outputData.getClass().toString());

            if(outputParams.size() <= i)
                return;

            Output outputParam = outputParams.get(i);

            if(outputData instanceof Collection) {
                Collection dataColl = ((Collection)outputData);

                Collection dataList =  (List) dataColl.stream()
                        .map(obj -> convertOutput(outputParam, outputData))
                        .collect(Collectors.toList());

                resultData.add(dataList);
            } else if(outputData instanceof Map) {

                Map rMap = new HashMap();

                ((Map)(outputData))
                        .forEach( (k,v) -> {
                             rMap.put(convertOutput(outputParam, k), convertOutput(outputParam, v));
                        } );

                resultData.add(rMap);

            } else {
                Object value = convertOutput(outputParam, outputData);
                resultData.add(value);
            }
        }

        responseBean.setData(resultData);

    }

    public static Object convertOutput(Output outputParam, Object outputData) {
//
//        boolean isArray = outputParam.isArray();

        if(outputData !=  null) {
            if (logger.isDebugEnabled())
                logger.debug("Output type : " + outputData.getClass());
        } else
            return null;
//
        if(outputParam.getType() == SolidityType._address) {

            return Address.wrap((byte [])outputData);

        } else if(outputParam.getType() == SolidityType._bool) {

            return (Boolean)outputData;

        } else if(outputParam.getType() == SolidityType._bytes) {

            return byteToString((byte[])outputData, outputParam.getEncoding());

        } else if(outputParam.getType() == SolidityType._dbytes) {

            return byteToString((byte[])outputData, outputParam.getEncoding());

        } else if(outputParam.getType() == SolidityType._int) {

            return (Integer)outputData;

        } else if(outputParam.getType() == SolidityType._uint) {

            return (Integer)outputData;

        } else if(outputParam.getType() == SolidityType._string) {

            return String.valueOf(outputData);

        }

        return null;
    }

    public static String byteToString(byte[] bytes, String enc) {
        if(bytes == null)
            return null;

        if(StringUtils.isEmpty(enc)) {
            return new String(bytes);
        } if(Output.HEX_TYPE.equalsIgnoreCase(enc)) {
            return IUtils.bytes2Hex(bytes);
        } else if(Output.BASE64_TYPE.equalsIgnoreCase(enc)) {
            return Base64.getEncoder().encodeToString(bytes);
        } else {
            try {
                return new String(bytes, enc);
            } catch (Exception e) {
                logger.error("Unable to encode {} from byte to string with encoding {}", bytes, enc);
                return new String(bytes); //try to encode with default utf-8
            }
        }
    }

}
