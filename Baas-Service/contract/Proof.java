package com.onchain.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
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
public class Proof extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162000a4038038062000a40833981016040819052620000349162000134565b81516200004990600090602085019062000071565b50600180546001600160a01b0319166001600160a01b03929092169190911790555062000272565b8280546200007f906200021f565b90600052602060002090601f016020900481019282620000a35760008555620000ee565b82601f10620000be57805160ff1916838001178555620000ee565b82800160010185558215620000ee579182015b82811115620000ee578251825591602001919060010190620000d1565b50620000fc92915062000100565b5090565b5b80821115620000fc576000815560010162000101565b80516001600160a01b03811681146200012f57600080fd5b919050565b6000806040838503121562000147578182fd5b82516001600160401b03808211156200015e578384fd5b818501915085601f83011262000172578384fd5b8151818111156200018757620001876200025c565b604051601f8201601f19908116603f01168101908382118183101715620001b257620001b26200025c565b81604052828152602093508884848701011115620001ce578687fd5b8691505b82821015620001f15784820184015181830185015290830190620001d2565b828211156200020257868484830101525b95506200021491505085820162000117565b925050509250929050565b6002810460018216806200023457607f821691505b602082108114156200025657634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052604160045260246000fd5b6107be80620002826000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063693ec85e1161005b578063693ec85e146100de57806380599e4b146100f1578063cfad57a214610106578063e942b516146101195761007d565b806327d2fce5146100825780633b18f691146100a057806346008a07146100b3575b600080fd5b61008a61012c565b60405161009791906106a8565b60405180910390f35b61008a6100ae3660046105c4565b6101ba565b6001546100c6906001600160a01b031681565b6040516001600160a01b039091168152602001610097565b61008a6100ec3660046105c4565b6101de565b6101046100ff3660046105c4565b61028e565b005b610104610114366004610596565b610325565b6101046101273660046105ff565b61039d565b6000805461013990610737565b80601f016020809104026020016040519081016040528092919081815260200182805461016590610737565b80156101b25780601f10610187576101008083540402835291602001916101b2565b820191906000526020600020905b81548152906001019060200180831161019557829003601f168201915b505050505081565b80516020818301810180516002825292820191909301209152805461013990610737565b60606002826040516101f0919061068c565b9081526020016040518091039020805461020990610737565b80601f016020809104026020016040519081016040528092919081815260200182805461023590610737565b80156102825780601f1061025757610100808354040283529160200191610282565b820191906000526020600020905b81548152906001019060200180831161026557829003601f168201915b50505050509050919050565b6001546001600160a01b031633146102c15760405162461bcd60e51b81526004016102b8906106e9565b60405180910390fd5b6002816040516102d1919061068c565b908152602001604051809103902060006102eb9190610437565b7f834a2d47e948021d7136fb7275b3f1e1feae6333c0d683e8c13f901667defd8c8160405161031a91906106a8565b60405180910390a150565b6001546001600160a01b0316331461034f5760405162461bcd60e51b81526004016102b8906106e9565b600180546001600160a01b0319166001600160a01b0383169081179091556040519081527f91a8c1cc2d4a3bb60738481947a00cbb9899c822916694cf8bb1d68172fdcd549060200161031a565b6001546001600160a01b031633146103c75760405162461bcd60e51b81526004016102b8906106e9565b806002836040516103d8919061068c565b908152602001604051809103902090805190602001906103f9929190610476565b507fddc5a395ff29c22c0e109c1b1e032440d25c3f9452ffe7327b9dbb2f30fa632a828260405161042b9291906106bb565b60405180910390a15050565b50805461044390610737565b6000825580601f106104555750610473565b601f01602090049060005260206000209081019061047391906104fa565b50565b82805461048290610737565b90600052602060002090601f0160209004810192826104a457600085556104ea565b82601f106104bd57805160ff19168380011785556104ea565b828001600101855582156104ea579182015b828111156104ea5782518255916020019190600101906104cf565b506104f69291506104fa565b5090565b5b808211156104f657600081556001016104fb565b600082601f83011261051f578081fd5b813567ffffffffffffffff8082111561053a5761053a610772565b604051601f8301601f19908116603f0116810190828211818310171561056257610562610772565b8160405283815286602085880101111561057a578485fd5b8360208701602083013792830160200193909352509392505050565b6000602082840312156105a7578081fd5b81356001600160a01b03811681146105bd578182fd5b9392505050565b6000602082840312156105d5578081fd5b813567ffffffffffffffff8111156105eb578182fd5b6105f78482850161050f565b949350505050565b60008060408385031215610611578081fd5b823567ffffffffffffffff80821115610628578283fd5b6106348683870161050f565b93506020850135915080821115610649578283fd5b506106568582860161050f565b9150509250929050565b60008151808452610678816020860160208601610707565b601f01601f19169290920160200192915050565b6000825161069e818460208701610707565b9190910192915050565b6000602082526105bd6020830184610660565b6000604082526106ce6040830185610660565b82810360208401526106e08185610660565b95945050505050565b60208082526004908201526310b3b7bb60e11b604082015260600190565b60005b8381101561072257818101518382015260200161070a565b83811115610731576000848401525b50505050565b60028104600182168061074b57607f821691505b6020821081141561076c57634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052604160045260246000fdfea2646970667358221220843b146ffabe61a907d3aab520d3dfbd9cfa95004420d9a3a85f863d0aec4a0b64736f6c63430008020033";

    public static final String FUNC_GET = "get";

    public static final String FUNC_GOVADDRESS = "govAddress";

    public static final String FUNC_PROOFNAME = "proofName";

    public static final String FUNC_PROOFS = "proofs";

    public static final String FUNC_REMOVE = "remove";

    public static final String FUNC_SET = "set";

    public static final String FUNC_SETGOV = "setGov";

    public static final Event REMOVE_EVENT = new Event("Remove", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event SET_EVENT = new Event("Set", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event SETGOV_EVENT = new Event("SetGov", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected Proof(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Proof(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Proof(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Proof(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<RemoveEventResponse> getRemoveEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REMOVE_EVENT, transactionReceipt);
        ArrayList<RemoveEventResponse> responses = new ArrayList<RemoveEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RemoveEventResponse typedResponse = new RemoveEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._key = (String) eventValues.getNonIndexedValues().get(0).getValue();
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
                return typedResponse;
            }
        });
    }

    public Flowable<SetEventResponse> setEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SET_EVENT));
        return setEventFlowable(filter);
    }

    public List<SetGovEventResponse> getSetGovEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SETGOV_EVENT, transactionReceipt);
        ArrayList<SetGovEventResponse> responses = new ArrayList<SetGovEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetGovEventResponse typedResponse = new SetGovEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._govAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SetGovEventResponse> setGovEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SetGovEventResponse>() {
            @Override
            public SetGovEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SETGOV_EVENT, log);
                SetGovEventResponse typedResponse = new SetGovEventResponse();
                typedResponse.log = log;
                typedResponse._govAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SetGovEventResponse> setGovEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETGOV_EVENT));
        return setGovEventFlowable(filter);
    }

    public RemoteFunctionCall<String> get(String _key) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_key)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> govAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GOVADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> proofName() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PROOFNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> proofs(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PROOFS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> remove(String _key) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_key)), 
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

    public RemoteFunctionCall<TransactionReceipt> setGov(String _govAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETGOV, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _govAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Proof load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Proof(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Proof load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Proof(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Proof load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Proof(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Proof load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Proof(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Proof> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _proofName, String _govAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_proofName), 
                new org.web3j.abi.datatypes.Address(160, _govAddress)));
        return deployRemoteCall(Proof.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Proof> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _proofName, String _govAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_proofName), 
                new org.web3j.abi.datatypes.Address(160, _govAddress)));
        return deployRemoteCall(Proof.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Proof> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _proofName, String _govAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_proofName), 
                new org.web3j.abi.datatypes.Address(160, _govAddress)));
        return deployRemoteCall(Proof.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Proof> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _proofName, String _govAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_proofName), 
                new org.web3j.abi.datatypes.Address(160, _govAddress)));
        return deployRemoteCall(Proof.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class RemoveEventResponse extends BaseEventResponse {
        public String _key;
    }

    public static class SetEventResponse extends BaseEventResponse {
        public String _key;

        public String _value;
    }

    public static class SetGovEventResponse extends BaseEventResponse {
        public String _govAddress;
    }
}
