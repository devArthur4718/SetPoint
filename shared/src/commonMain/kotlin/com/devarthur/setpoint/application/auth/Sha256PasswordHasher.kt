package com.devarthur.setpoint.application.auth

/**
 * Implementação de [PasswordHasher] usando SHA-256 em Kotlin puro (KMP).
 * Adequado para MVP local; para produção considere bcrypt/argon2 via expect/actual.
 */
class Sha256PasswordHasher : PasswordHasher {
    override fun hash(plainPassword: String): String {
        val bytes = plainPassword.encodeToByteArray()
        val digest = sha256(bytes)
        return digest.joinToString("") { b -> (b.toInt() and 0xff).toString(16).padStart(2, '0') }
    }

    override fun verify(plainPassword: String, storedHash: String): Boolean {
        val computed = hash(plainPassword)
        return computed.equals(storedHash, ignoreCase = true)
    }

    private fun sha256(input: ByteArray): ByteArray {
        val k = LONG_K
        val h = IntArray(8).apply {
            LONG_H.forEachIndexed { i, v -> this[i] = v }
        }
        val pre = pad(input)
        var offset = 0
        while (offset < pre.size) {
            val w = IntArray(64)
            for (t in 0..15) {
                w[t] = ((pre[offset].toInt() and 0xff) shl 24) or
                    ((pre[offset + 1].toInt() and 0xff) shl 16) or
                    ((pre[offset + 2].toInt() and 0xff) shl 8) or
                    (pre[offset + 3].toInt() and 0xff)
                offset += 4
            }
            for (t in 16..63) {
                val s0 = rotr(w[t - 15], 7) xor rotr(w[t - 15], 18) xor (w[t - 15] ushr 3)
                val s1 = rotr(w[t - 2], 17) xor rotr(w[t - 2], 19) xor (w[t - 2] ushr 10)
                w[t] = w[t - 16] + s0 + w[t - 7] + s1
            }
            var a = h[0]; var b = h[1]; var c = h[2]; var d = h[3]
            var e = h[4]; var f = h[5]; var g = h[6]; var h7 = h[7]
            for (t in 0..63) {
                val s1 = rotr(e, 6) xor rotr(e, 11) xor rotr(e, 25)
                val ch = (e and f) xor (e.inv() and g)
                val t1 = h7 + s1 + ch + k[t] + w[t]
                val s0 = rotr(a, 2) xor rotr(a, 13) xor rotr(a, 22)
                val maj = (a and b) xor (a and c) xor (b and c)
                val t2 = s0 + maj
                h7 = g; g = f; f = e; e = d + t1; d = c; c = b; b = a; a = t1 + t2
            }
            h[0] = h[0] + a; h[1] = h[1] + b; h[2] = h[2] + c; h[3] = h[3] + d
            h[4] = h[4] + e; h[5] = h[5] + f; h[6] = h[6] + g; h[7] = h[7] + h7
        }
        return ByteArray(32) { i ->
            val intIndex = 7 - (i / 4)
            val shift = (3 - (i % 4)) * 8
            (h[intIndex] ushr shift).toByte()
        }
    }

    private fun rotr(x: Int, n: Int): Int = (x ushr n) or (x shl (32 - n))

    private fun pad(input: ByteArray): ByteArray {
        val len = input.size
        val padLen = ((len + 9 + 63) / 64) * 64
        val out = ByteArray(padLen)
        input.copyInto(out)
        out[len] = 0x80.toByte()
        val bitLen = len * 8L
        for (i in 0..7) out[padLen - 8 + i] = ((bitLen ushr (56 - i * 8)).toInt() and 0xff).toByte()
        return out
    }

    private companion object {
        private fun int32(vararg values: Long): IntArray = IntArray(values.size) { values[it].toInt() }
        val LONG_H = int32(
            0x6a09e667L, 0xbb67ae85L, 0x3c6ef372L, 0xa54ff53aL,
            0x510e527fL, 0x9b05688cL, 0x1f83d9abL, 0x5be0cd19L
        )
        val LONG_K = int32(
            0x428a2f98L, 0x71374491L, 0xb5c0fbcfL, 0xe9b5dba5L, 0x3956c25bL, 0x59f111f1L, 0x923f82a4L, 0xab1c5ed5L,
            0xd807aa98L, 0x12835b01L, 0x243185beL, 0x550c7dc3L, 0x72be5d74L, 0x80deb1feL, 0x9bdc06a7L, 0xc19bf174L,
            0xe49b69c1L, 0xefbe4786L, 0x0fc19dc6L, 0x240ca1ccL, 0x2de92c6fL, 0x4a7484aaL, 0x5cb0a9dcL, 0x76f988daL,
            0x983e5152L, 0xa831c66dL, 0xb00327c8L, 0xbf597fc7L, 0xc6e00bf3L, 0xd5a79147L, 0x06ca6351L, 0x14292967L,
            0x27b70a85L, 0x2e1b2138L, 0x4d2c6dfcL, 0x53380d13L, 0x650a7354L, 0x766a0abbL, 0x81c2c92eL, 0x92722c85L,
            0xa2bfe8a1L, 0xa81a664bL, 0xc24b8b70L, 0xc76c51a3L, 0xd192e819L, 0xd6990624L, 0xf40e3585L, 0x106aa070L,
            0x19a4c116L, 0x1e376c08L, 0x2748774cL, 0x34b0bcb5L, 0x391c0cb3L, 0x4ed8aa4aL, 0x5b9cca4fL, 0x682e6ff3L,
            0x748f82eeL, 0x78a5636fL, 0x84c87814L, 0x8cc70208L, 0x90befffaL, 0xa4506cebL, 0xbef9a3f7L, 0xc67178f2L
        )
    }
}
