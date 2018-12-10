pragma solidity ^0.4.15;

contract MultiTypes {
    uint8 i;
    bytes32 b;
    string s;
    address a;
    bool bl;

    function MultiTypes(uint8 _i, bytes32 _b, string _s, address _a, bool _bl) public {
        i = _i;
        b = _b;
        s = _s;
        a = _a;
        bl = _bl;
    }

    function get() public constant returns (uint8, bytes32, string, address, bool) {
        return (i,b,s,a,bl);
    }
}
