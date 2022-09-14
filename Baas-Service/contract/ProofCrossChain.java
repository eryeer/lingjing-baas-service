package com.onchain.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class ProofCrossChain extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162000abf38038062000abf8339810160408190526200003491620000f7565b80516200004990600090602084019062000051565b505062000220565b8280546200005f90620001cd565b90600052602060002090601f016020900481019282620000835760008555620000ce565b82601f106200009e57805160ff1916838001178555620000ce565b82800160010185558215620000ce579182015b82811115620000ce578251825591602001919060010190620000b1565b50620000dc929150620000e0565b5090565b5b80821115620000dc5760008155600101620000e1565b600060208083850312156200010a578182fd5b82516001600160401b038082111562000121578384fd5b818501915085601f83011262000135578384fd5b8151818111156200014a576200014a6200020a565b604051601f8201601f19908116603f011681019083821181831017156200017557620001756200020a565b8160405282815288868487010111156200018d578687fd5b8693505b82841015620001b0578484018601518185018701529285019262000191565b82841115620001c157868684830101525b98975050505050505050565b600281046001821680620001e257607f821691505b602082108114156200020457634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052604160045260246000fd5b61088f80620002306000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063693ec85e1161005b578063693ec85e146100d6578063779211fc146100e957806380599e4b146100fc578063e942b5161461010f5761007d565b806327d2fce5146100825780633b18f691146100a05780634521ea90146100c1575b600080fd5b61008a610122565b6040516100979190610762565b60405180910390f35b6100b36100ae3660046105c1565b6101b0565b60405161009792919061077c565b6100d46100cf3660046105fc565b610262565b005b61008a6100e43660046105c1565b6102d7565b6100d46100f73660046106a9565b610389565b6100d461010a3660046105c1565b610437565b6100d461011d366004610648565b610445565b6000805461012f90610808565b80601f016020809104026020016040519081016040528092919081815260200182805461015b90610808565b80156101a85780601f1061017d576101008083540402835291602001916101a8565b820191906000526020600020905b81548152906001019060200180831161018b57829003601f168201915b505050505081565b80516020818301810180516001825292820191909301209152805481906101d690610808565b80601f016020809104026020016040519081016040528092919081815260200182805461020290610808565b801561024f5780601f106102245761010080835404028352916020019161024f565b820191906000526020600020905b81548152906001019060200180831161023257829003601f168201915b5050506001909301549192505060ff1682565b6001826040516102729190610746565b908152604051908190036020019020600061028d8282610455565b50600101805460ff191690556040517f2c4b2a61a55ee8577c66d536d9a2ecfe94bbcf59ac809e407d4aa8434c91f764906102cb908490849061077c565b60405180910390a15050565b60606001826040516102e99190610746565b908152604051908190036020019020805461030390610808565b80601f016020809104026020016040519081016040528092919081815260200182805461032f90610808565b801561037c5780601f106103515761010080835404028352916020019161037c565b820191906000526020600020905b81548152906001019060200180831161035f57829003601f168201915b505050505090505b919050565b60405180604001604052808381526020018215158152506001846040516103b09190610746565b908152602001604051809103902060008201518160000190805190602001906103da929190610491565b50602091909101516001909101805460ff19169115159190911790556040517f12578ae8a5ebf74d58042032577a409bbe23c284ad379946441a284a55592adc9061042a908590859085906107a0565b60405180910390a1505050565b610442816000610262565b50565b61045182826000610389565b5050565b50805461046190610808565b6000825580601f106104735750610442565b601f0160209004906000526020600020908101906104429190610515565b82805461049d90610808565b90600052602060002090601f0160209004810192826104bf5760008555610505565b82601f106104d857805160ff1916838001178555610505565b82800160010185558215610505579182015b828111156105055782518255916020019190600101906104ea565b50610511929150610515565b5090565b5b808211156105115760008155600101610516565b8035801515811461038457600080fd5b600082601f83011261054a578081fd5b813567ffffffffffffffff8082111561056557610565610843565b604051601f8301601f19908116603f0116810190828211818310171561058d5761058d610843565b816040528381528660208588010111156105a5578485fd5b8360208701602083013792830160200193909352509392505050565b6000602082840312156105d2578081fd5b813567ffffffffffffffff8111156105e8578182fd5b6105f48482850161053a565b949350505050565b6000806040838503121561060e578081fd5b823567ffffffffffffffff811115610624578182fd5b6106308582860161053a565b92505061063f6020840161052a565b90509250929050565b6000806040838503121561065a578182fd5b823567ffffffffffffffff80821115610671578384fd5b61067d8683870161053a565b93506020850135915080821115610692578283fd5b5061069f8582860161053a565b9150509250929050565b6000806000606084860312156106bd578081fd5b833567ffffffffffffffff808211156106d4578283fd5b6106e08783880161053a565b945060208601359150808211156106f5578283fd5b506107028682870161053a565b9250506107116040850161052a565b90509250925092565b600081518084526107328160208601602086016107d8565b601f01601f19169290920160200192915050565b600082516107588184602087016107d8565b9190910192915050565b600060208252610775602083018461071a565b9392505050565b60006040825261078f604083018561071a565b905082151560208301529392505050565b6000606082526107b3606083018661071a565b82810360208401526107c5818661071a565b9150508215156040830152949350505050565b60005b838110156107f35781810151838201526020016107db565b83811115610802576000848401525b50505050565b60028104600182168061081c57607f821691505b6020821081141561083d57634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052604160045260246000fdfea26469706673582212201becb2bea079281ff779e57740885d06cc9d251b3f14bf6a9488cc51fbaa6d1c64736f6c63430008020033";

    public static final String FUNC_GET = "get";

    public static final String FUNC_PROOFNAME = "proofName";

    public static final String FUNC_PROOFS = "proofs";

    public static final String FUNC_REMOVE = "remove";

    public static final String FUNC_REMOVECROSSCHAIN = "removeCrossChain";

    public static final String FUNC_SET = "set";

    public static final String FUNC_SETCROSSCHAIN = "setCrossChain";

    public static final Event REMOVE_EVENT = new Event("Remove", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event SET_EVENT = new Event("Set", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
    ;

    @Deprecated
    protected ProofCrossChain(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ProofCrossChain(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ProofCrossChain(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ProofCrossChain(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<RemoveEventResponse> getRemoveEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REMOVE_EVENT, transactionReceipt);
        ArrayList<RemoveEventResponse> responses = new ArrayList<RemoveEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RemoveEventResponse typedResponse = new RemoveEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._key = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._isCrossChain = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RemoveEventResponse> removeEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RemoveEventResponse>() {
            @Override
            public RemoveEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REMOVE_EVENT, log);
                RemoveEventResponse typedResponse = new RemoveEventResponse();
                typedResponse.log = log;
                typedResponse._key = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._isCrossChain = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RemoveEventResponse> removeEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REMOVE_EVENT));
        return removeEventFlowable(filter);
    }

    public List<SetEventResponse> getSetEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SET_EVENT, transactionReceipt);
        ArrayList<SetEventResponse> responses = new ArrayList<SetEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetEventResponse typedResponse = new SetEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._key = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._value = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._isCrossChain = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SetEventResponse> setEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SetEventResponse>() {
            @Override
            public SetEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SET_EVENT, log);
                SetEventResponse typedResponse = new SetEventResponse();
                typedResponse.log = log;
                typedResponse._key = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._value = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._isCrossChain = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SetEventResponse> setEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SET_EVENT));
        return setEventFlowable(filter);
    }

    public RemoteFunctionCall<String> get(String _key) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_key)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> proofName() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PROOFNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple2<String, Boolean>> proofs(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PROOFS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple2<String, Boolean>>(function,
                new Callable<Tuple2<String, Boolean>>() {
                    @Override
                    public Tuple2<String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, Boolean>(
                                (String) results.get(0).getValue(), 
                                (Boolean) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> remove(String _key) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_key)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeCrossChain(String _key, Boolean _isCrossChain) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVECROSSCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_key), 
                new org.web3j.abi.datatypes.Bool(_isCrossChain)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> set(String _key, String _value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_key), 
                new org.web3j.abi.datatypes.Utf8String(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setCrossChain(String _key, String _value, Boolean _isCrossChain) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETCROSSCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_key), 
                new org.web3j.abi.datatypes.Utf8String(_value), 
                new org.web3j.abi.datatypes.Bool(_isCrossChain)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ProofCrossChain load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProofCrossChain(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ProofCrossChain load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProofCrossChain(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ProofCrossChain load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ProofCrossChain(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ProofCrossChain load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ProofCrossChain(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ProofCrossChain> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _proofName) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_proofName)));
        return deployRemoteCall(ProofCrossChain.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ProofCrossChain> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _proofName) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_proofName)));
        return deployRemoteCall(ProofCrossChain.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ProofCrossChain> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _proofName) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_proofName)));
        return deployRemoteCall(ProofCrossChain.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ProofCrossChain> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _proofName) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_proofName)));
        return deployRemoteCall(ProofCrossChain.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class RemoveEventResponse extends BaseEventResponse {
        public String _key;

        public Boolean _isCrossChain;
    }

    public static class SetEventResponse extends BaseEventResponse {
        public String _key;

        public String _value;

        public Boolean _isCrossChain;
    }
}
