//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.0;

contract Proof {

    struct Content {
        string value;
    }

    string public proofName;
    address public govAddress;

    mapping (string => string) public proofs;

    event Set(string _key, string _value);
    event Remove(string _key);
    event SetGov(address _govAddress);

    modifier onlyAllowGov() {
        require(msg.sender == govAddress, "!gov");
        _;
    }

    constructor(string memory _proofName, address _govAddress) {
        proofName = _proofName;
        govAddress = _govAddress;
    }

    function setGov(address _govAddress) public onlyAllowGov {
        govAddress = _govAddress;
        emit SetGov(_govAddress);
    }

    function set(string memory _key, string memory _value) public onlyAllowGov {
        proofs[_key] = _value;
        emit Set(_key, _value);
    }

    function remove(string memory _key) public onlyAllowGov {
        delete proofs[_key];
        emit Remove(_key);
    }

    function get(string memory _key) public view returns(string memory){
        return proofs[_key];
    }
}