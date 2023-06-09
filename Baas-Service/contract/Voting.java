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
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
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
public class Voting extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162000aa238038062000aa28339810160408190526200003491620001c3565b80516200004990600090602084019062000057565b50506000546002556200036b565b828054828255906000526020600020908101928215620000a9579160200282015b82811115620000a9578251805162000098918491602090910190620000bb565b509160200191906001019062000078565b50620000b792915062000146565b5090565b828054620000c99062000318565b90600052602060002090601f016020900481019282620000ed576000855562000138565b82601f106200010857805160ff191683800117855562000138565b8280016001018555821562000138579182015b82811115620001385782518255916020019190600101906200011b565b50620000b792915062000167565b80821115620000b75760006200015d82826200017e565b5060010162000146565b5b80821115620000b7576000815560010162000168565b5080546200018c9062000318565b6000825580601f10620001a05750620001c0565b601f016020900490600052602060002090810190620001c0919062000167565b50565b60006020808385031215620001d6578182fd5b82516001600160401b0380821115620001ed578384fd5b8185019150601f868184011262000202578485fd5b82518281111562000217576200021762000355565b620002268586830201620002e5565b81815285810190858701885b84811015620002d557815188018c603f8201126200024e578a8bfd5b898101518881111562000265576200026562000355565b62000278818901601f19168c01620002e5565b81815260408f818486010111156200028e578d8efd5b8d5b83811015620002ad578481018201518382018f01528d0162000290565b83811115620002be578e8e85850101525b505086525050928801929088019060010162000232565b50909a9950505050505050505050565b604051601f8201601f191681016001600160401b038111828210171562000310576200031062000355565b604052919050565b6002810460018216806200032d57607f821691505b602082108114156200034f57634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052604160045260246000fd5b610727806200037b6000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c80638d4b44d91161005b5780638d4b44d9146100e6578063a9a981a314610114578063b13c744b1461011d578063e89927ef1461013d5761007d565b80630d15fd77146100825780634a3bf3771461009e578063538c91b2146100c3575b600080fd5b61008b60035481565b6040519081526020015b60405180910390f35b6100b16100ac366004610419565b610152565b60405160ff9091168152602001610095565b6100d66100d1366004610419565b6101d0565b6040519015158152602001610095565b6100b16100f4366004610419565b805160208183018101805160018252928201919093012091525460ff1681565b61008b60025481565b61013061012b3660046104c3565b610281565b60405161009591906105be565b61015061014b366004610419565b61032d565b005b600061015d826101d0565b6101a55760405162461bcd60e51b815260206004820152601460248201527318d85b991a59185d19481a5cc81a5b9d985b1a5960621b60448201526064015b60405180910390fd5b6001826040516101b59190610507565b9081526040519081900360200190205460ff1690505b919050565b6000805b60005481101561027857826040516020016101ef9190610507565b604051602081830303815290604052805190602001206000828154811061022657634e487b7160e01b600052603260045260246000fd5b906000526020600020016040516020016102409190610523565b6040516020818303038152906040528051906020012014156102665760019150506101cb565b80610270816106aa565b9150506101d4565b50600092915050565b6000818154811061029157600080fd5b9060005260206000200160009150905080546102ac9061066f565b80601f01602080910402602001604051908101604052809291908181526020018280546102d89061066f565b80156103255780601f106102fa57610100808354040283529160200191610325565b820191906000526020600020905b81548152906001019060200180831161030857829003601f168201915b505050505081565b610336816101d0565b6103795760405162461bcd60e51b815260206004820152601460248201527318d85b991a59185d19481a5cc81a5b9d985b1a5960621b604482015260640161019c565b6001808260405161038a9190610507565b90815260405190819003602001902080546000906103ac90849060ff1661061a565b92506101000a81548160ff021916908360ff1602179055506001600360008282546103d79190610602565b90915550506040517f877cd47bbf47a162b34be0d8dc765f17cbe6086842779cd4fb69a87d6c0b6a209061040e90839033906105d8565b60405180910390a150565b60006020828403121561042a578081fd5b813567ffffffffffffffff80821115610441578283fd5b818401915084601f830112610454578283fd5b813581811115610466576104666106db565b604051601f8201601f19908116603f0116810190838211818310171561048e5761048e6106db565b816040528281528760208487010111156104a6578586fd5b826020860160208301379182016020019490945295945050505050565b6000602082840312156104d4578081fd5b5035919050565b600081518084526104f381602086016020860161063f565b601f01601f19169290920160200192915050565b6000825161051981846020870161063f565b9190910192915050565b815460009081906002810460018083168061053f57607f831692505b602080841082141561055f57634e487b7160e01b87526022600452602487fd5b8180156105735760018114610584576105b0565b60ff198616895284890196506105b0565b60008a815260209020885b868110156105a85781548b82015290850190830161058f565b505084890196505b509498975050505050505050565b6000602082526105d160208301846104db565b9392505050565b6000604082526105eb60408301856104db565b905060018060a01b03831660208301529392505050565b60008219821115610615576106156106c5565b500190565b600060ff821660ff84168060ff03821115610637576106376106c5565b019392505050565b60005b8381101561065a578181015183820152602001610642565b83811115610669576000848401525b50505050565b60028104600182168061068357607f821691505b602082108114156106a457634e487b7160e01b600052602260045260246000fd5b50919050565b60006000198214156106be576106be6106c5565b5060010190565b634e487b7160e01b600052601160045260246000fd5b634e487b7160e01b600052604160045260246000fdfea26469706673582212209f320105a458507e085527f5c4161eab929b127edce74bf918d6753476aeea2c64736f6c63430008020033";

    public static final String FUNC_CANDIDATECOUNT = "candidateCount";

    public static final String FUNC_CANDIDATELIST = "candidateList";

    public static final String FUNC_TOTALVOTES = "totalVotes";

    public static final String FUNC_TOTALVOTESFOR = "totalVotesFor";

    public static final String FUNC_VALIDCANDIDATE = "validCandidate";

    public static final String FUNC_VOTEFORCANDIDATE = "voteForCandidate";

    public static final String FUNC_VOTESRECEIVED = "votesReceived";

    public static final Event VOTE_EVENT = new Event("VOTE", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<VOTEEventResponse> getVOTEEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(VOTE_EVENT, transactionReceipt);
        ArrayList<VOTEEventResponse> responses = new ArrayList<VOTEEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VOTEEventResponse typedResponse = new VOTEEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.candidate = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.voterId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VOTEEventResponse> vOTEEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VOTEEventResponse>() {
            @Override
            public VOTEEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTE_EVENT, log);
                VOTEEventResponse typedResponse = new VOTEEventResponse();
                typedResponse.log = log;
                typedResponse.candidate = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.voterId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VOTEEventResponse> vOTEEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTE_EVENT));
        return vOTEEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> candidateCount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CANDIDATECOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> candidateList(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CANDIDATELIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalVotes() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALVOTES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> totalVotesFor(String candidate) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALVOTESFOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(candidate)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> validCandidate(String candidate) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VALIDCANDIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(candidate)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> voteForCandidate(String candidate) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VOTEFORCANDIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(candidate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> votesReceived(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VOTESRECEIVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, List<String> candidateNames) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(candidateNames, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Voting.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, List<String> candidateNames) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(candidateNames, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Voting.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, List<String> candidateNames) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(candidateNames, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Voting.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, List<String> candidateNames) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(candidateNames, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Voting.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class VOTEEventResponse extends BaseEventResponse {
        public String candidate;

        public String voterId;
    }
}
