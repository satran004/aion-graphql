pragma solidity ^0.4.15;

contract StringTypeTest {
    string s;
    address a;

    function TypesTest(address a, string) public {
        a = _a;
        s = _s;
    }

    function get() public constant returns (address, string) {
        return (a, s);
    }
}