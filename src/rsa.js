function isPrime(n, k=100) {
    return true
}

function generatePrime(bits = 1024) {
    //check - isPrime?
    let primeBigInt
    let isPrimeBoolean = false
    while (!isPrimeBoolean) {
        primeBigInt = getRandomBigInt(bits)
        isPrimeBoolean = isPrime(primeBigInt, k=10)
    }
    return primeBigInt
}


function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}


function getRandomBigInt(bits = 1024) {
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


function generateRsaKeyPair(bits = 1024, e = 65537n) {
    let p = 1n, q = 1n
    //console.log('00 F9 95 80 5A E6 FD 12 52 C3 5D 2D 0E 14 5F 04 E4 6C F6 F2 99 5B 57 DA C9 EC CA 0F B5 34 46 43 71 24 29 2C 9F 7E DC CA E3 A0 F3 32 37 04 D1 D2 FF FC E9 76 A7 FA 0C 6D FF 14 7F A2 47 D7 96 00 0F '.split(' ').join(''))
    p = generatePrime() // BigInt('10089492364999512835528429168273061510287964919442299334768281502367854564710584724649660742971582377959348319842916472073757800913736762843723817350561391')//generateWithCondition(p, e, bits)
    q = generatePrime() //BigInt('13071774209124879114739371138051483271182095049775000070504380743054194773449051729532086204556431401764732513058022331121386757945185678811876726461038607')//generateWithCondition(q, e, bits)
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

function powToMod(base, power, module) {
    if(power === 1n) return base;
    if(power % 2n === 0n) return powToMod(base, power / 2n, module) ** 2n % module;
    return powToMod(base, power - 1n, module) * base % module;
}
