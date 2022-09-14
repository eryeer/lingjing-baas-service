//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.0;

contract Voting {

    string[] public candidateList;

    mapping (string => uint8) public votesReceived;

    uint256 public candidateCount;
    uint256 public totalVotes;

    event VOTE(string candidate, address voterId);

    constructor(string[] memory candidateNames) {
        candidateList = candidateNames;
        candidateCount = candidateList.length;
    }

    // This function returns the total votes a candidate has received so far.
    function totalVotesFor(string memory candidate) view public returns (uint8) {
        require(validCandidate(candidate), "candidate is invalid");
        return votesReceived[candidate];
    }

    // This function increments the vote count for the specified candidate.
    // This is equivalent to casting a vote.
    function voteForCandidate(string memory candidate) public {
        require(validCandidate(candidate), "candidate is invalid");
        votesReceived[candidate] += 1;
        totalVotes += 1;

        emit VOTE(candidate, msg.sender);
    }

    // This function will help to check whether target candidate is in the candidateList.
    function validCandidate(string memory candidate) view public returns (bool) {
        for (uint i = 0; i < candidateList.length; i++) {
            if (keccak256(abi.encodePacked(candidateList[i])) == keccak256(abi.encodePacked(candidate))) {
                return true;
            }
        }
        return false;
    }
}