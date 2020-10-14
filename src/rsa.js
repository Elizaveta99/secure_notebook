

function generatePrime(bits = 1024) {
    //check - isPrime?
    return getRandomBigIng(bits)
}


function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}


function getRandomBigIng(bits) {
    let bigInt = 0n
    let cycles = Math.floor(bits / 32)
    while (--cycles) {
        //console.log(bigInt)
        bigInt = (bigInt << 32n) + BigInt(getRandomInt(Number.MAX_SAFE_INTEGER))
    }
    bigInt = bigInt | (1n << (BigInt(bits) - 1n)) | 1n
    return bigInt
}


function generateWithCondition(q, e, bits) {
    while (q % e === 1n) {
        q = generatePrime(bits)
    }
    return q
}


function extendedAlgorithmEuclidBigInt(e, L) {
    let a = L
    let b = e
    let d, x, y = [0n, 0n, 0n]
    if (b === 0n) {
        d = a
        x = 1n
        y = 0n
        return [d, x, y]
    }
    let x2 = 1n, x1 = 0n, y2 = 0n, y1 = 1n
    while (b > 0n) {
        const q = a / b
        const r = a - q * b
        x = x2 - q * x1
        y = y2 - q * y1
        a = b, b = r, x2 = x1, x1 = x, y2 = y1, y1 = y
    }
    d = a, x = x2, y = y2
    // if d > 1 then modular inversion doesn't exist, else y = modular inversion
    return [d, x, y]
}


export function generateRsaKeyPair(bits = 1024, e = 65537n) {
    let p = 1n, q = 1n
    p = generateWithCondition(p, e, bits)
    q = generateWithCondition(q, e, bits)
    const L = (p - 1n) * (q - 1n)
    let d = extendedAlgorithmEuclidBigInt(e, L)[2]
    d = d >= 0n ? d : L + d
    const privateKey = {
        e,
        p: generateWithCondition(p, e, bits),
        q: generateWithCondition(q, e, bits),
        N: p * q,
        L,
        d
    }
    const publicKey = {
        e: e,
        N: privateKey.N,
    }
    return {publicKey, privateKey}
}