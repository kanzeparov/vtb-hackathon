import pywaves as pw

# ======================================================================
# NODE = "https://testnode1.wavesnodes.com"
# CHAIN = 'testnet'
# pw.setNode(node=NODE, chain=CHAIN)
# DAPPADDRESS = "3MwXroS3i9z6TujTDqcWeCUrrmmE44RvTKc"
# ======================================================================
PRICE = 100000000


# MAX1_ADDRESS = "3Mpi62dU5eMt6Z52nr8oatJoYK1ztu5RfV2"
# MAX2_ADDRESS = "3N8dFuxQbnj7c6vzZ4uqoojweTXtmUp5JUS"
# ORACLE_PRIVATE_KEY = "F3D81RNfGgiJd4wzLz3jrGUKCpceo1Hnrfq8BjWGimPX"
# args2 = [
#     {"type": "string", "value": "game"},
#     {"type": "string", "value": MAX1_ADDRESS},
#     {"type": "string", "value": MAX2_ADDRESS}
# ]
#
# MAX2_PRIVATEKEY = "CtsaSnAPoVMupqGyk4b4qpgXKH2nSs4rG7GujdPeXvco"
# MAX1_PRIVATEKEY = "4FRCTx52VzFKTk1F38AHfsXKFhwChd9gKf4Ef3LzzpY4"

args = [
    {"type": "integer", "value": 1, },
    {"type": "integer", "value": 2, },
    {"type": "integer", "value": 3, },
    {"type": "integer", "value": 4, },
    {"type": "integer", "value": 5, },
    {"type": "integer", "value": 6, },
    {"type": "integer", "value": 10, },
    {"type": "integer", "value": 18, },
    {"type": "integer", "value": 19, },
    {"type": "integer", "value": 20, },
    {"type": "integer", "value": 39, },
    {"type": "integer", "value": 100, }
]


def invoke_remote_script(funcName, args, price,privateKey):
    address = pw.Address(privateKey=privateKey)
    tx = address.invokeScript(DAPPADDRESS, funcName, args,
                              [{"amount": price, "assetId": None}])
    print(tx)
    return tx


def create_patent(privateKey, coords):
    FUNC = "patent"
    tx = invoke_remote_script(FUNC, coords, PRICE, privateKey)
    print(FUNC,tx.get("id"))
    return {"id": tx.get("id")}


def buy_patent(privateKey, coords):
    FUNC = "buyPatent"
    PATENT_PRICE = 200000000
    tx = invoke_remote_script(FUNC, coords, PATENT_PRICE, privateKey)
    print(FUNC,tx.get("id"))
    return {"id": tx.get("id")}


def withdraw(privateKey):
    FUNC = "withdraw"
    address = pw.Address(privateKey=privateKey)
    tx = address.invokeScript(DAPPADDRESS, FUNC, [], [])
    print(FUNC, tx.get("id"))
    return {"id": tx.get("id")}


def send_game_report(privateKey, args):
    FUNC = "gameReport"
    address = pw.Address(privateKey=privateKey)
    tx = address.invokeScript(DAPPADDRESS, FUNC, args, [])
    print(FUNC, tx.get("id"))
    return {"id": tx.get("id")}


def insure(privateKey):
    FUNC = "insure"
    INSURENCE_PRICE = int(PRICE * 0.07)
    address = pw.Address(privateKey=privateKey)
    tx = address.invokeScript(DAPPADDRESS, FUNC, [], [{"amount": INSURENCE_PRICE, "assetId": None}])
    print(FUNC, tx.get("id"))
    return {"id": tx.get("id")}

if __name__ == '__main__':
    import time
    create_patent(MAX1_PRIVATEKEY, args)
    time.sleep(5)
    buy_patent(MAX2_PRIVATEKEY, args)
    time.sleep(5)
    withdraw(MAX1_PRIVATEKEY)
    time.sleep(5)
    send_game_report(ORACLE_PRIVATE_KEY,args2)
    time.sleep(5)
    insure(MAX1_PRIVATEKEY)