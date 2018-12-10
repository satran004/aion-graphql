package org.satran.blockchain.graphql.impl.aion.util;

import java.util.List;
import org.aion.solidity.Abi.Entry.Param;
import org.aion.solidity.Abi.Function;
import org.aion.solidity.SolidityType;
import org.satran.blockchain.graphql.model.input.ContractFunction;

public class AbiUtil {

    public static boolean isSameFunction(Function function, ContractFunction inputFunction) {

        String fnName = function.name;

        if(inputFunction.getName() == null || !inputFunction.getName().equals(fnName)) {
            return false;
        }

        List<Param> abiParams = function.inputs;

        List<org.satran.blockchain.graphql.model.input.Param> inputParams = inputFunction.getParams();

        if(abiParams.size() != inputParams.size())
            return false;

        if(abiParams.size() == 0) {
            if(inputParams.size() == 0)
                return true;
            else
                return false;
        }

        for(int i=0; i<abiParams.size(); i++) {

            SolidityType abiType = abiParams.get(i).type;

            SolidityType inputType = SolidityType.getType(inputParams.get(i).getType());

            if(!abiType.getName().equals(inputType.getName())) {
                return false;
            }
        }

        return true;

    }

}
