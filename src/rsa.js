function isPrime(n, k=100) {
    return true
}

const primesList = [23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997,]

function generatePrime(bits = 1024) {

    // let primeBigInt
    // let isPrimeBoolean = false
    // while (!isPrimeBoolean) {
    //     primeBigInt = getRandomBigInt(bits)
    //     isPrimeBoolean = isPrime(primeBigInt, k=10)
    // }
    const primeBigInt = primeMock()
    return primeBigInt
}

function primeMock() {
    return Math.floor(Math.random()*primesList.length)
}


function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}


function getRandomBigInt(bits = 1024) {
    let bigInt = 0
    let cycles = Math.floor(bits / 32)
    while (--cycles) {
        //console.log(bigInt)
        bigInt = (bigInt << 32) + BigInt(getRandomInt(Number.MAX_SAFE_INTEGER))
    }
    bigInt = bigInt | (1 << (BigInt(bits) - 1)) | 1
    return bigInt
}


function generateWithCondition(q, e, bits) {
    while (q % e === 1) {
        q = generatePrime(bits)
    }
    return q
}


function extendedAlgorithmEuclidBigInt(e, L) {
    let a = L
    let b = e
    let d, x, y = [0, 0, 0]
    if (b === 0) {
        d = a
        x = 1
        y = 0
        return [d, x, y]
    }
    let x2 = 1, x1 = 0, y2 = 0, y1 = 1
    while (b > 0) {
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


export function generateRsaKeyPair(bits = 1024, e = 3) {
    let p = 1, q = 1
    //console.log('00 F9 95 80 5A E6 FD 12 52 C3 5D 2D 0E 14 5F 04 E4 6C F6 F2 99 5B 57 DA C9 EC CA 0F B5 34 46 43 71 24 29 2C 9F 7E DC CA E3 A0 F3 32 37 04 D1 D2 FF FC E9 76 A7 FA 0C 6D FF 14 7F A2 47 D7 96 00 0F '.split(' ').join(''))
    p = generatePrime() // BigInt('10089492364999512835528429168273061510287964919442299334768281502367854564710584724649660742971582377959348319842916472073757800913736762843723817350561391')//generateWithCondition(p, e, bits)
    q = generatePrime() //BigInt('13071774209124879114739371138051483271182095049775000070504380743054194773449051729532086204556431401764732513058022331121386757945185678811876726461038607')//generateWithCondition(q, e, bits)
    const l = (p - 1) * (q - 1)
    let d = extendedAlgorithmEuclidBigInt(e, l)[2]
    d = d >= 0 ? d : L + d
    const privateKey = {
        e,
        p: generateWithCondition(p, e, bits),
        q: generateWithCondition(q, e, bits),
        n: p * q,
        l,
        d
    }
    const publicKey = {
        e: e,
        n: privateKey.n,
    }
    return {publicKey, privateKey}
}

//!!!only Node.js!!!
function powToMod(base, power, module) {
    if(power === 1) return base;
    if(power % 2 === 0) return powToMod(base, power / 2, module) ** 2 % module;
    return powToMod(base, power - 1, module) * base % module;
}

export function decrypt(message, d, n) {
    return message**d % n
}
