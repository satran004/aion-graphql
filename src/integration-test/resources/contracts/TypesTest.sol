pragma solidity ^0.4.15;

contract TypesTest {
    uint8 i;
    bytes32 b;
    address a;
    bool bl;

    function TypesTest(uint8 _i, address _a, bool _bl) public {
        i = _i;
        a = _a;
        bl = _bl;
    }

    function get() public constant returns (uint8, address,  bool) {
        return (i, a, bl);
    }
}