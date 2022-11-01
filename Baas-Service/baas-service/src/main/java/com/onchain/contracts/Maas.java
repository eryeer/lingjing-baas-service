package com.onchain.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class Maas extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_BLOCKACCOUNT = "blockAccount";

    public static final String FUNC_CHANGEOWNER = "changeOwner";

    public static final String FUNC_ENABLEGASMANAGE = "enableGasManage";

    public static final String FUNC_GETADMINLIST = "getAdminList";

    public static final String FUNC_GETBLACKLIST = "getBlacklist";

    public static final String FUNC_GETGASMANAGERLIST = "getGasManagerList";

    public static final String FUNC_GETGASUSERLIST = "getGasUserList";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_ISADMIN = "isAdmin";

    public static final String FUNC_ISBLOCKED = "isBlocked";

    public static final String FUNC_ISGASMANAGEENABLED = "isGasManageEnabled";

    public static final String FUNC_ISGASMANAGER = "isGasManager";

    public static final String FUNC_ISGASUSER = "isGasUser";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SETADMINS = "setAdmins";

    public static final String FUNC_SETGASMANAGER = "setGasManager";

    public static final String FUNC_SETGASUSERS = "setGasUsers";

    public static final Event BLOCKACCOUNT_EVENT = new Event("BlockAccount",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Bool>() {
            }));
    ;

    public static final Event CHANGEOWNER_EVENT = new Event("ChangeOwner",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }));
    ;

    public static final Event ENABLEGASMANAGE_EVENT = new Event("EnableGasManage",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
            }));
    ;

    public static final Event SETADMINS_EVENT = new Event("SetAdmins",
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {
            }, new TypeReference<Bool>() {
            }));
    ;

    public static final Event SETGASMANAGER_EVENT = new Event("SetGasManager",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Bool>() {
            }));
    ;

    public static final Event SETGASUSERS_EVENT = new Event("SetGasUsers",
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {
            }, new TypeReference<Bool>() {
            }));
    ;

    @Deprecated
    protected Maas(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Maas(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Maas(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Maas(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<BlockAccountEventResponse> getBlockAccountEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BLOCKACCOUNT_EVENT, transactionReceipt);
        ArrayList<BlockAccountEventResponse> responses = new ArrayList<BlockAccountEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BlockAccountEventResponse typedResponse = new BlockAccountEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.addr = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.doBlock = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BlockAccountEventResponse> blockAccountEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, BlockAccountEventResponse>() {
            @Override
            public BlockAccountEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BLOCKACCOUNT_EVENT, log);
                BlockAccountEventResponse typedResponse = new BlockAccountEventResponse();
                typedResponse.log = log;
                typedResponse.addr = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.doBlock = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BlockAccountEventResponse> blockAccountEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BLOCKACCOUNT_EVENT));
        return blockAccountEventFlowable(filter);
    }

    public List<ChangeOwnerEventResponse> getChangeOwnerEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CHANGEOWNER_EVENT, transactionReceipt);
        ArrayList<ChangeOwnerEventResponse> responses = new ArrayList<ChangeOwnerEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ChangeOwnerEventResponse typedResponse = new ChangeOwnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ChangeOwnerEventResponse> changeOwnerEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ChangeOwnerEventResponse>() {
            @Override
            public ChangeOwnerEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CHANGEOWNER_EVENT, log);
                ChangeOwnerEventResponse typedResponse = new ChangeOwnerEventResponse();
                typedResponse.log = log;
                typedResponse.oldOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ChangeOwnerEventResponse> changeOwnerEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CHANGEOWNER_EVENT));
        return changeOwnerEventFlowable(filter);
    }

    public List<EnableGasManageEventResponse> getEnableGasManageEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ENABLEGASMANAGE_EVENT, transactionReceipt);
        ArrayList<EnableGasManageEventResponse> responses = new ArrayList<EnableGasManageEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EnableGasManageEventResponse typedResponse = new EnableGasManageEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.doEnable = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<EnableGasManageEventResponse> enableGasManageEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, EnableGasManageEventResponse>() {
            @Override
            public EnableGasManageEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ENABLEGASMANAGE_EVENT, log);
                EnableGasManageEventResponse typedResponse = new EnableGasManageEventResponse();
                typedResponse.log = log;
                typedResponse.doEnable = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<EnableGasManageEventResponse> enableGasManageEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ENABLEGASMANAGE_EVENT));
        return enableGasManageEventFlowable(filter);
    }

    public List<SetAdminsEventResponse> getSetAdminsEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SETADMINS_EVENT, transactionReceipt);
        ArrayList<SetAdminsEventResponse> responses = new ArrayList<SetAdminsEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetAdminsEventResponse typedResponse = new SetAdminsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.addrs = (List<String>) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.addOrRemove = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SetAdminsEventResponse> setAdminsEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SetAdminsEventResponse>() {
            @Override
            public SetAdminsEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SETADMINS_EVENT, log);
                SetAdminsEventResponse typedResponse = new SetAdminsEventResponse();
                typedResponse.log = log;
                typedResponse.addrs = (List<String>) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.addOrRemove = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SetAdminsEventResponse> setAdminsEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETADMINS_EVENT));
        return setAdminsEventFlowable(filter);
    }

    public List<SetGasManagerEventResponse> getSetGasManagerEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SETGASMANAGER_EVENT, transactionReceipt);
        ArrayList<SetGasManagerEventResponse> responses = new ArrayList<SetGasManagerEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetGasManagerEventResponse typedResponse = new SetGasManagerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.addr = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.isManager = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SetGasManagerEventResponse> setGasManagerEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SetGasManagerEventResponse>() {
            @Override
            public SetGasManagerEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SETGASMANAGER_EVENT, log);
                SetGasManagerEventResponse typedResponse = new SetGasManagerEventResponse();
                typedResponse.log = log;
                typedResponse.addr = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.isManager = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SetGasManagerEventResponse> setGasManagerEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETGASMANAGER_EVENT));
        return setGasManagerEventFlowable(filter);
    }

    public List<SetGasUsersEventResponse> getSetGasUsersEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SETGASUSERS_EVENT, transactionReceipt);
        ArrayList<SetGasUsersEventResponse> responses = new ArrayList<SetGasUsersEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetGasUsersEventResponse typedResponse = new SetGasUsersEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.addrs = (List<String>) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.addOrRemove = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SetGasUsersEventResponse> setGasUsersEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SetGasUsersEventResponse>() {
            @Override
            public SetGasUsersEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SETGASUSERS_EVENT, log);
                SetGasUsersEventResponse typedResponse = new SetGasUsersEventResponse();
                typedResponse.log = log;
                typedResponse.addrs = (List<String>) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.addOrRemove = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SetGasUsersEventResponse> setGasUsersEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETGASUSERS_EVENT));
        return setGasUsersEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> blockAccount(String addr, Boolean doBlock) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BLOCKACCOUNT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, addr),
                        new org.web3j.abi.datatypes.Bool(doBlock)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> changeOwner(String addr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CHANGEOWNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, addr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> enableGasManage(Boolean doEnable) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ENABLEGASMANAGE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Bool(doEnable)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getAdminList() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETADMINLIST,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getBlacklist() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBLACKLIST,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getGasManagerList() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETGASMANAGERLIST,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getGasUserList() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETGASUSERLIST,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getOwner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETOWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isAdmin(String addr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISADMIN,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isBlocked(String addr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISBLOCKED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isGasManageEnabled() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISGASMANAGEENABLED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isGasManager(String addr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISGASMANAGER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isGasUser(String addr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISGASUSER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> name() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setAdmins(List<String> addrs, Boolean addOrRemove) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETADMINS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                                org.web3j.abi.datatypes.Address.class,
                                org.web3j.abi.Utils.typeMap(addrs, org.web3j.abi.datatypes.Address.class)),
                        new org.web3j.abi.datatypes.Bool(addOrRemove)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setGasManager(String addr, Boolean isManager) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETGASMANAGER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, addr),
                        new org.web3j.abi.datatypes.Bool(isManager)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setGasUsers(List<String> addrs, Boolean addOrRemove) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETGASUSERS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                                org.web3j.abi.datatypes.Address.class,
                                org.web3j.abi.Utils.typeMap(addrs, org.web3j.abi.datatypes.Address.class)),
                        new org.web3j.abi.datatypes.Bool(addOrRemove)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Maas load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Maas(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Maas load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Maas(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Maas load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Maas(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Maas load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Maas(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class BlockAccountEventResponse extends BaseEventResponse {
        public String addr;

        public Boolean doBlock;
    }

    public static class ChangeOwnerEventResponse extends BaseEventResponse {
        public String oldOwner;

        public String newOwner;
    }

    public static class EnableGasManageEventResponse extends BaseEventResponse {
        public Boolean doEnable;
    }

    public static class SetAdminsEventResponse extends BaseEventResponse {
        public List<String> addrs;

        public Boolean addOrRemove;
    }

    public static class SetGasManagerEventResponse extends BaseEventResponse {
        public String addr;

        public Boolean isManager;
    }

    public static class SetGasUsersEventResponse extends BaseEventResponse {
        public List<String> addrs;

        public Boolean addOrRemove;
    }
}
