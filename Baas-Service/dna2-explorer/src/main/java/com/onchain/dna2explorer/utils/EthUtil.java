package com.onchain.dna2explorer.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EthUtil {

    public static String getSHA256(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithms.SHA3_256);
        md.update(str.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = md.digest();
        return Hex.encodeHexString(bytes);
    }

    public static String buildMethodSignature(String methodName, List<AbiDefinition.NamedType> parameters) {
        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        String params = parameters.stream().map(EthUtil::getNamedTypeString).collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
    }

    public static String getNamedTypeString(AbiDefinition.NamedType namedType) {
        if (namedType.getComponents().isEmpty()) {
            return namedType.getType();
        }
        StringBuilder result = new StringBuilder();
        result.append("(");
        String params = namedType.getComponents().stream().map(EthUtil::getNamedTypeString).collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        // deal with tuple[]
        return namedType.getType().replace("tuple", result.toString());
    }

    public static String buildMethodFullName(String methodName, List<AbiDefinition.NamedType> parameters) {
        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        for (int i = 0; i < parameters.size(); i++) {
            result.append(parameters.get(i).isIndexed() ? "index_topic_" + (i + 1) + " " : "")
                    .append(parameters.get(i).getType())
                    .append(" ")
                    .append(parameters.get(i).getName())
                    .append(",")
                    .append(" ");
        }
        result.delete(result.length() - 2, result.length());
        result.append(")");
        return result.toString();
    }

    public static String buildMethodHash(String methodSignature) {
        byte[] input = methodSignature.getBytes(StandardCharsets.UTF_8);
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash);
    }

    public static String getMethodId(String methodHash) {
        return methodHash.substring(0, 10);
    }

    public static String callContractString(Web3j web3j, String contractAddress, String methodName, List<Type> args) throws ExecutionException, InterruptedException {
        return callContractSingle(web3j, contractAddress, methodName, args, new TypeReference<Utf8String>() {
        }).getValue().toString();
    }

    // BigInteger.toString()
    public static String callContractUint(Web3j web3j, String contractAddress, String methodName, List<Type> args) throws ExecutionException, InterruptedException {
        return callContractSingle(web3j, contractAddress, methodName, args, new TypeReference<Uint>() {
        }).getValue().toString();
    }

    public static String callContractAddress(Web3j web3j, String contractAddress, String methodName, List<Type> args) throws ExecutionException, InterruptedException {
        return callContractSingle(web3j, contractAddress, methodName, args, new TypeReference<Address>() {
        }).getValue().toString();
    }

    public static Type callContractSingle(Web3j web3j, String contractAddress, String methodName, List<Type> args, TypeReference<?> res)
            throws ExecutionException, InterruptedException {
        Function function = new Function(
                methodName,
                args,  // Solidity Types in smart contract functions
                Arrays.asList(res));
        String encodedFunction = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                        Transaction.createEthCallTransaction(null, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST)
                .sendAsync().get();
        return FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters()).get(0);
    }

    public static List<Type> callContractMulti(Web3j web3j, String contractAddress, String methodName, List<Type> args, List<TypeReference<?>> res)
            throws ExecutionException, InterruptedException {
        Function function = new Function(
                methodName,
                args,  // Solidity Types in smart contract functions
                res);
        String encodedFunction = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                        Transaction.createEthCallTransaction(null, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST)
                .sendAsync().get();
        return FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
    }

    public static String getAddressFromTopic(String hex) {
        return FunctionReturnDecoder.decodeIndexedValue(hex, new TypeReference<Address>() {
        }).toString();
    }

    public static String getUIntFromTopic(String hex) {
        return FunctionReturnDecoder.decodeIndexedValue(hex, new TypeReference<Uint>() {
        }).getValue().toString();
    }

    public static boolean isContractDeployer(String signature, String abi, String deployerAddr) {
        if (deployerAddr == null || deployerAddr.length() != 42 || signature == null || signature.length() != 132 || abi == null || abi.equals("")) {
            return false;
        }
        deployerAddr = deployerAddr.toLowerCase(Locale.ROOT);
        byte[] sigBytes = Numeric.hexStringToByteArray(signature);
        Sign.SignatureData signatureData = sigFromByteArray(sigBytes);
        String addressRecoverd = null;
        byte[] digest = Hash.sha3(abi.getBytes());
        byte[] data = getEthereumMessageHash(digest);
        for (int i = 0; i < 4; i++) {
            BigInteger pubkey = Sign.recoverFromSignature((byte) i,
                    new ECDSASignature(new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS())),
                    data);
            if (pubkey != null) {
                addressRecoverd = "0x" + Keys.getAddress(pubkey);
                if (addressRecoverd.equals(deployerAddr)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Sign.SignatureData sigFromByteArray(byte[] sig) {
        if (sig.length < 64 || sig.length > 65) return null;

        byte subv = sig[64];
        if (subv < 27) subv += 27;
        byte[] rBytes = Arrays.copyOfRange(sig, 0, 32);
        byte[] sBytes = Arrays.copyOfRange(sig, 32, 64);

        return new Sign.SignatureData(subv, rBytes, sBytes);
    }

    public static byte[] getEthereumMessageHash(byte[] message) {
        String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";
        byte[] prefix = MESSAGE_PREFIX.concat(String.valueOf(message.length)).getBytes();

        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);

        return Hash.sha3(result);
    }
}
